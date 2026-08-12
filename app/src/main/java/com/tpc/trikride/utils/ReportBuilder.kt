package com.tpc.trikride.utils

import com.tpc.trikride.models.Complaint
import com.tpc.trikride.models.ComplaintStatus
import com.tpc.trikride.models.Driver
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.RideStatus
import com.tpc.trikride.models.User
import java.util.Calendar
import java.util.Locale

/** The stretch of time a report covers. */
sealed class ReportPeriod {
    data class Month(val year: Int, val month: Int) : ReportPeriod()
    data class Year(val year: Int) : ReportPeriod()
    data object AllTime : ReportPeriod()

    val label: String
        get() = when (this) {
            is Month -> "${MONTH_NAMES[month]} $year"
            is Year -> "$year"
            AllTime -> "All time"
        }

    /** Safe for a filename on any platform the admin might open this on. */
    val slug: String
        get() = when (this) {
            is Month -> "%04d-%02d".format(year, month + 1)
            is Year -> "%04d".format(year)
            AllTime -> "all-time"
        }

    fun contains(epochMillis: Long): Boolean {
        if (this is AllTime) return true
        val cal = Calendar.getInstance().apply { timeInMillis = epochMillis }
        return when (this) {
            is Month -> cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) == month
            is Year -> cal.get(Calendar.YEAR) == year
            AllTime -> true
        }
    }

    companion object {
        val MONTH_NAMES = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
    }
}

/** Headline numbers shown above the export buttons. */
data class ReportSummary(
    val totalRides: Int = 0,
    val completed: Int = 0,
    val cancelled: Int = 0,
    val inProgress: Int = 0,
    val grossFares: Double = 0.0,
    val averageFare: Double = 0.0,
    val uniquePassengers: Int = 0,
    val activeDrivers: Int = 0,
    val complaintsFiled: Int = 0,
    val complaintsResolved: Int = 0
)

/**
 * Turns the admin's live data into month or year reports.
 *
 * Everything is written as CSV because that is what opens without argument in
 * Excel, Google Sheets and LibreOffice, which is where a capstone panel or the
 * drivers' association is going to want the numbers.
 *
 * Timestamps throughout the app are epoch milliseconds held as strings, so
 * anything unparseable is treated as outside every period rather than being
 * silently bucketed into the current month.
 */
object ReportBuilder {

    private fun millis(value: String): Long? = value.toLongOrNull()

    private fun readable(value: String): String {
        val ms = millis(value) ?: return ""
        val cal = Calendar.getInstance().apply { timeInMillis = ms }
        return "%04d-%02d-%02d %02d:%02d".format(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH),
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE)
        )
    }

    /** Wraps a value so commas, quotes and newlines inside it survive the round trip. */
    private fun cell(value: Any?): String {
        val text = value?.toString().orEmpty()
        if (text.isEmpty()) return ""
        val escaped = text.replace("\"", "\"\"")
        return "\"$escaped\""
    }

    private fun row(vararg values: Any?): String = values.joinToString(",") { cell(it) }

    private fun name(user: User?): String = when {
        user == null -> "Unknown"
        else -> listOf(user.firstName, user.lastName).filter { it.isNotBlank() }
            .joinToString(" ").ifBlank { user.email }
    }

    /** Which months and years the admin can pick, newest first, from real data. */
    fun availablePeriods(rides: List<Ride>): List<ReportPeriod> {
        val stamps = rides.mapNotNull { millis(it.requestedAt) }
        if (stamps.isEmpty()) return listOf(ReportPeriod.AllTime)

        val months = linkedSetOf<Pair<Int, Int>>()
        val years = linkedSetOf<Int>()
        stamps.sortedDescending().forEach { ms ->
            val cal = Calendar.getInstance().apply { timeInMillis = ms }
            months.add(cal.get(Calendar.YEAR) to cal.get(Calendar.MONTH))
            years.add(cal.get(Calendar.YEAR))
        }

        return buildList {
            add(ReportPeriod.AllTime)
            years.sortedDescending().forEach { add(ReportPeriod.Year(it)) }
            months.sortedWith(compareByDescending<Pair<Int, Int>> { it.first }.thenByDescending { it.second })
                .forEach { (y, m) -> add(ReportPeriod.Month(y, m)) }
        }
    }

    fun ridesIn(rides: List<Ride>, period: ReportPeriod): List<Ride> =
        rides.filter { ride -> millis(ride.requestedAt)?.let { period.contains(it) } == true }
            .sortedBy { millis(it.requestedAt) ?: 0L }

    fun complaintsIn(complaints: List<Complaint>, period: ReportPeriod): List<Complaint> =
        complaints.filter { c -> millis(c.createdAt)?.let { period.contains(it) } == true }
            .sortedBy { millis(it.createdAt) ?: 0L }

    fun summarise(
        rides: List<Ride>,
        complaints: List<Complaint>,
        period: ReportPeriod
    ): ReportSummary {
        val inPeriod = ridesIn(rides, period)
        val done = inPeriod.filter { it.status == RideStatus.COMPLETED }
        val gross = done.sumOf { if (it.actualFare > 0) it.actualFare else it.estimatedFare }
        val filed = complaintsIn(complaints, period)
        return ReportSummary(
            totalRides = inPeriod.size,
            completed = done.size,
            cancelled = inPeriod.count {
                it.status == RideStatus.CANCELLED || it.status == RideStatus.NO_SHOW
            },
            inProgress = inPeriod.count {
                it.status != RideStatus.COMPLETED &&
                    it.status != RideStatus.CANCELLED &&
                    it.status != RideStatus.NO_SHOW
            },
            grossFares = gross,
            averageFare = if (done.isEmpty()) 0.0 else gross / done.size,
            uniquePassengers = inPeriod.map { it.passengerId }.filter { it.isNotBlank() }.toSet().size,
            activeDrivers = inPeriod.map { it.driverId }.filter { it.isNotBlank() }.toSet().size,
            complaintsFiled = filed.size,
            complaintsResolved = filed.count { it.status == ComplaintStatus.RESOLVED }
        )
    }

    /** Every ride in the period, one per line, with a summary block on top. */
    fun ridesCsv(
        rides: List<Ride>,
        complaints: List<Complaint>,
        usersById: Map<String, User>,
        period: ReportPeriod
    ): String {
        val s = summarise(rides, complaints, period)
        val sb = StringBuilder()

        sb.appendLine(row("TrikRide ride activity report"))
        sb.appendLine(row("Talibon Polytechnic College"))
        sb.appendLine(row("Period", period.label))
        sb.appendLine(row("Generated", readable(System.currentTimeMillis().toString())))
        sb.appendLine()
        sb.appendLine(row("Total rides", s.totalRides))
        sb.appendLine(row("Completed", s.completed))
        sb.appendLine(row("Cancelled or no-show", s.cancelled))
        sb.appendLine(row("Still open", s.inProgress))
        sb.appendLine(row("Gross fares (PHP)", "%.2f".format(s.grossFares)))
        sb.appendLine(row("Average completed fare (PHP)", "%.2f".format(s.averageFare)))
        sb.appendLine(row("Passengers served", s.uniquePassengers))
        sb.appendLine(row("Drivers with at least one ride", s.activeDrivers))
        sb.appendLine()

        sb.appendLine(
            row(
                "Ride ID", "Requested", "Accepted", "Started", "Completed",
                "Passenger", "Passenger email", "Driver", "Driver email",
                "Pickup", "Destination", "Passengers", "Luggage",
                "Status", "Estimated fare", "Actual fare", "Notes"
            )
        )
        ridesIn(rides, period).forEach { ride ->
            val passenger = usersById[ride.passengerId]
            val driver = usersById[ride.driverId]
            sb.appendLine(
                row(
                    ride.id,
                    readable(ride.requestedAt),
                    readable(ride.acceptedAt),
                    readable(ride.startedAt),
                    readable(ride.completedAt),
                    name(passenger),
                    passenger?.email.orEmpty(),
                    if (ride.driverId.isBlank()) "Unassigned" else name(driver),
                    driver?.email.orEmpty(),
                    ride.pickupLocation.address,
                    ride.dropoffLocation.address,
                    ride.passengerCount,
                    ride.luggage,
                    ride.status.name,
                    "%.2f".format(ride.estimatedFare),
                    "%.2f".format(ride.actualFare),
                    ride.notes
                )
            )
        }
        return sb.toString()
    }

    /** Per-driver totals for the period, busiest first. */
    fun driversCsv(
        rides: List<Ride>,
        drivers: List<Driver>,
        usersById: Map<String, User>,
        period: ReportPeriod
    ): String {
        val inPeriod = ridesIn(rides, period).filter { it.driverId.isNotBlank() }
        val byDriver = inPeriod.groupBy { it.driverId }
        val driversById = drivers.associateBy { it.userId }

        val sb = StringBuilder()
        sb.appendLine(row("TrikRide driver performance report"))
        sb.appendLine(row("Period", period.label))
        sb.appendLine(row("Generated", readable(System.currentTimeMillis().toString())))
        sb.appendLine()
        sb.appendLine(
            row(
                "Driver", "Email", "Phone", "Tricycle number", "License number",
                "Verification", "Rides accepted", "Completed", "Cancelled",
                "Gross fares (PHP)", "Average fare (PHP)", "Rating"
            )
        )

        byDriver.entries
            .sortedByDescending { it.value.size }
            .forEach { (driverId, driverRides) ->
                val user = usersById[driverId]
                val record = driversById[driverId]
                val done = driverRides.filter { it.status == RideStatus.COMPLETED }
                val gross = done.sumOf { if (it.actualFare > 0) it.actualFare else it.estimatedFare }
                sb.appendLine(
                    row(
                        name(user),
                        user?.email.orEmpty(),
                        user?.phoneNumber.orEmpty(),
                        record?.tricycleNumber.orEmpty(),
                        record?.licenseNumber.orEmpty(),
                        record?.verificationStatus?.name.orEmpty(),
                        driverRides.size,
                        done.size,
                        driverRides.count {
                            it.status == RideStatus.CANCELLED || it.status == RideStatus.NO_SHOW
                        },
                        "%.2f".format(gross),
                        "%.2f".format(if (done.isEmpty()) 0.0 else gross / done.size),
                        "%.1f".format(record?.rating ?: 0.0)
                    )
                )
            }

        val idle = drivers.filter { it.userId !in byDriver.keys }
        if (idle.isNotEmpty()) {
            sb.appendLine()
            sb.appendLine(row("Drivers with no rides in this period"))
            sb.appendLine(row("Driver", "Email", "Tricycle number", "Verification"))
            idle.forEach { record ->
                val user = usersById[record.userId]
                sb.appendLine(
                    row(
                        name(user),
                        user?.email.orEmpty(),
                        record.tricycleNumber,
                        record.verificationStatus.name
                    )
                )
            }
        }
        return sb.toString()
    }

    /** Concerns filed in the period, with how they were handled. */
    fun complaintsCsv(complaints: List<Complaint>, period: ReportPeriod): String {
        val inPeriod = complaintsIn(complaints, period)
        val sb = StringBuilder()
        sb.appendLine(row("TrikRide concerns and complaints report"))
        sb.appendLine(row("Period", period.label))
        sb.appendLine(row("Generated", readable(System.currentTimeMillis().toString())))
        sb.appendLine()
        sb.appendLine(row("Total filed", inPeriod.size))
        sb.appendLine(row("Resolved", inPeriod.count { it.status == ComplaintStatus.RESOLVED }))
        sb.appendLine(row("Open", inPeriod.count { it.status == ComplaintStatus.OPEN }))
        sb.appendLine(row("In review", inPeriod.count { it.status == ComplaintStatus.IN_REVIEW }))
        sb.appendLine()

        val byCategory = inPeriod.groupingBy { it.category }.eachCount()
        if (byCategory.isNotEmpty()) {
            sb.appendLine(row("Category", "Count"))
            byCategory.entries.sortedByDescending { it.value }.forEach { (category, count) ->
                sb.appendLine(row(category, count))
            }
            sb.appendLine()
        }

        sb.appendLine(
            row(
                "Report ID", "Filed", "Resolved", "Filed by", "Account type",
                "Category", "Status", "Description", "Admin note"
            )
        )
        inPeriod.forEach { c ->
            sb.appendLine(
                row(
                    c.id,
                    readable(c.createdAt),
                    readable(c.resolvedAt),
                    c.reporterName,
                    c.reporterType.name,
                    c.category,
                    c.status.name,
                    c.description,
                    c.adminNote
                )
            )
        }
        return sb.toString()
    }

    fun fileName(kind: String, period: ReportPeriod): String =
        "trikride-$kind-${period.slug}.csv".lowercase(Locale.US)
}
