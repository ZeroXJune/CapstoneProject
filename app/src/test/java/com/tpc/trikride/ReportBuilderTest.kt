package com.tpc.trikride

import com.tpc.trikride.models.Complaint
import com.tpc.trikride.models.ComplaintStatus
import com.tpc.trikride.models.Location
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.RideStatus
import com.tpc.trikride.models.User
import com.tpc.trikride.utils.ReportBuilder
import com.tpc.trikride.utils.ReportPeriod
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class ReportBuilderTest {

    private fun at(year: Int, month: Int, day: Int): Long =
        Calendar.getInstance().apply {
            clear(); set(year, month, day, 12, 0, 0)
        }.timeInMillis

    private fun ride(
        id: String = "r1",
        status: RideStatus = RideStatus.COMPLETED,
        fare: Double = 40.0,
        requestedAt: Long = at(2026, Calendar.MARCH, 10),
        passengerId: String = "p1",
        driverId: String = "d1",
        notes: String = ""
    ) = Ride(
        id = id,
        passengerId = passengerId,
        driverId = driverId,
        pickupLocation = Location(address = "Pickup"),
        dropoffLocation = Location(address = "Market"),
        status = status,
        requestedAt = requestedAt.toString(),
        estimatedFare = fare,
        notes = notes
    )

    private val users = mapOf(
        "p1" to User(id = "p1", firstName = "Ana", email = "ana@example.com"),
        "d1" to User(id = "d1", firstName = "Ben", email = "ben@example.com")
    )

    // --- summary arithmetic -------------------------------------------------

    @Test
    fun `summarises completed rides and gross fares`() {
        val rides = listOf(
            ride("a", fare = 40.0),
            ride("b", fare = 60.0),
            ride("c", status = RideStatus.IN_PROGRESS, fare = 25.0)
        )
        val s = ReportBuilder.summarise(rides, emptyList(), ReportPeriod.AllTime)
        assertEquals(3, s.totalRides)
        assertEquals(2, s.completed)
        assertEquals(1, s.inProgress)
        assertEquals(100.0, s.grossFares, 0.001)
        assertEquals(50.0, s.averageFare, 0.001)
    }

    @Test
    fun `an empty period averages to zero rather than dividing by it`() {
        val s = ReportBuilder.summarise(emptyList(), emptyList(), ReportPeriod.AllTime)
        assertEquals(0.0, s.averageFare, 0.001)
        assertEquals(0, s.totalRides)
    }

    @Test
    fun `a ride with an unparseable timestamp falls outside every dated period`() {
        val rides = listOf(ride("a").copy(requestedAt = "not-a-number"))
        assertEquals(0, ReportBuilder.ridesIn(rides, ReportPeriod.Year(2026)).size)
        // AllTime still filters on a parseable stamp, so it is excluded there too.
        assertEquals(0, ReportBuilder.ridesIn(rides, ReportPeriod.AllTime).size)
    }

    @Test
    fun `a month period keeps only that month`() {
        val rides = listOf(
            ride("a", requestedAt = at(2026, Calendar.MARCH, 1)),
            ride("b", requestedAt = at(2026, Calendar.APRIL, 1))
        )
        val march = ReportBuilder.ridesIn(rides, ReportPeriod.Month(2026, Calendar.MARCH))
        assertEquals(listOf("a"), march.map { it.id })
    }

    @Test
    fun `a custom range includes both end days in full`() {
        val start = at(2026, Calendar.MARCH, 10)
        val end = at(2026, Calendar.MARCH, 12)
        val period = ReportPeriod.customRange(start, end)
        assertTrue(period.contains(start))
        assertTrue(period.contains(end))
    }

    @Test
    fun `a custom range picked back to front still means the days between`() {
        val a = at(2026, Calendar.MARCH, 12)
        val b = at(2026, Calendar.MARCH, 10)
        val period = ReportPeriod.customRange(a, b) as ReportPeriod.Custom
        assertTrue(period.startMillis < period.endMillis)
    }

    @Test
    fun `unique passengers and drivers ignore blanks`() {
        val rides = listOf(
            ride("a", passengerId = "p1", driverId = "d1"),
            ride("b", passengerId = "p1", driverId = ""),
            ride("c", passengerId = "p2", driverId = "d2")
        )
        val s = ReportBuilder.summarise(rides, emptyList(), ReportPeriod.AllTime)
        assertEquals(2, s.uniquePassengers)
        assertEquals(2, s.activeDrivers)
    }

    // --- CSV safety ---------------------------------------------------------

    /**
     * A ride note is typed by a passenger and read by an administrator in
     * Excel. A cell that begins `=`, `+`, `-` or `@` is evaluated as a formula
     * on the administrator's computer, so it has to be neutralised on the way
     * out. Quoting alone does not do it.
     */
    @Test
    fun `a note that looks like a formula is not written as one`() {
        val hostile = "=HYPERLINK(\"http://example.invalid\",\"click\")"
        val csv = ReportBuilder.ridesCsv(
            listOf(ride(notes = hostile)), emptyList(), users, ReportPeriod.AllTime
        )
        // Quotes inside the value are doubled by the CSV escaping, so the cell
        // as written is the prefixed text with its own quotes doubled.
        val written = "\"'" + hostile.replace("\"", "\"\"") + "\""
        assertFalse("formula reached the file unescaped", csv.contains("\"$hostile\""))
        assertTrue("expected the text-marker prefix", csv.contains(written))
    }

    @Test
    fun `every formula lead character is neutralised, including behind whitespace`() {
        listOf("=1+1", "+1", "-1", "@SUM(A1)", "\t=1+1", " =1+1").forEach { hostile ->
            val csv = ReportBuilder.ridesCsv(
                listOf(ride(notes = hostile)), emptyList(), users, ReportPeriod.AllTime
            )
            assertTrue("not neutralised: $hostile", csv.contains("\"'$hostile\""))
        }
    }

    @Test
    fun `ordinary text is not prefixed`() {
        val csv = ReportBuilder.ridesCsv(
            listOf(ride(notes = "Meet me by the gate")), emptyList(), users, ReportPeriod.AllTime
        )
        assertTrue(csv.contains("\"Meet me by the gate\""))
        assertFalse(csv.contains("\"'Meet me by the gate\""))
    }

    @Test
    fun `quotes and commas inside a value survive the round trip`() {
        val csv = ReportBuilder.ridesCsv(
            listOf(ride(notes = "He said \"hello\", then left")),
            emptyList(), users, ReportPeriod.AllTime
        )
        assertTrue(csv.contains("\"He said \"\"hello\"\", then left\""))
    }

    @Test
    fun `a hostile display name in a complaint is neutralised too`() {
        val complaint = Complaint(
            id = "c1",
            reporterId = "unknown",
            reporterName = "=cmd|'/c calc'!A0",
            category = "Other",
            description = "-1+1",
            status = ComplaintStatus.OPEN,
            createdAt = at(2026, Calendar.MARCH, 10).toString()
        )
        val csv = ReportBuilder.complaintsCsv(listOf(complaint), emptyList<User>().associateBy { it.id }, ReportPeriod.AllTime)
        assertTrue(csv.contains("\"'=cmd|'/c calc'!A0\""))
        assertTrue(csv.contains("\"'-1+1\""))
    }

    /** Money in a data file must parse as a number wherever it is opened. */
    @Test
    fun `fares are written with a dot decimal separator whatever the device locale`() {
        val previous = java.util.Locale.getDefault()
        try {
            java.util.Locale.setDefault(java.util.Locale.GERMANY)
            val csv = ReportBuilder.ridesCsv(
                listOf(ride(fare = 1234.5)), emptyList(), users, ReportPeriod.AllTime
            )
            assertTrue("expected 1234.50, got a locale-formatted number", csv.contains("1234.50"))
            assertFalse(csv.contains("1234,50"))
        } finally {
            java.util.Locale.setDefault(previous)
        }
    }

    @Test
    fun `a report filename is safe whatever the device locale`() {
        val previous = java.util.Locale.getDefault()
        try {
            java.util.Locale.setDefault(java.util.Locale.forLanguageTag("ar-EG-u-nu-arab"))
            val name = ReportBuilder.fileName("rides", ReportPeriod.Month(2026, Calendar.MARCH), "csv")
            assertEquals("trikride-rides-2026-03.csv", name)
        } finally {
            java.util.Locale.setDefault(previous)
        }
    }

    // --- aggregations -------------------------------------------------------

    @Test
    fun `rides by weekday always reports all seven days`() {
        val byDay = ReportBuilder.ridesByWeekday(listOf(ride()), ReportPeriod.AllTime)
        assertEquals(7, byDay.size)
        assertEquals(listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"), byDay.map { it.first })
    }

    @Test
    fun `rides by hour always reports twenty four buckets`() {
        assertEquals(24, ReportBuilder.ridesByHour(listOf(ride()), ReportPeriod.AllTime).size)
    }

    @Test
    fun `completion rate ignores drivers with too few rides to mean anything`() {
        val rides = listOf(
            ride("a", driverId = "d1"), ride("b", driverId = "d1"),
            ride("c", driverId = "d2"), ride("d", driverId = "d2"), ride("e", driverId = "d2")
        )
        val rates = ReportBuilder.completionRatePerDriver(rides, users, ReportPeriod.AllTime)
        assertEquals(1, rates.size)
    }

    @Test
    fun `available periods always offer all time even with no rides`() {
        assertEquals(listOf(ReportPeriod.AllTime), ReportBuilder.availablePeriods(emptyList()))
    }
}
