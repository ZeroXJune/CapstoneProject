package com.tpc.trikride.utils

import com.tpc.trikride.models.Complaint
import com.tpc.trikride.models.ComplaintStatus
import com.tpc.trikride.models.Driver
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.RideStatus
import com.tpc.trikride.models.User
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

/** The stretch of time a report covers. */
sealed class ReportPeriod {
    data class Month(val year: Int, val month: Int) : ReportPeriod()
    data class Year(val year: Int) : ReportPeriod()
    data object AllTime : ReportPeriod()

    /**
     * Any two dates the admin picks, both days included.
     *
     * [startMillis] and [endMillis] are stored as the first and last instant of
     * the chosen days rather than as whatever midnight the picker handed back.
     * A range whose end is midnight would leave out everything that happened on
     * the last day, which is not what anyone means by "up to the 15th".
     */
    data class Custom(val startMillis: Long, val endMillis: Long) : ReportPeriod()

    val label: String
        get() = when (this) {
            is Month -> "${MONTH_NAMES[month]} $year"
            is Year -> "$year"
            AllTime -> "All time"
            is Custom -> "${shortDate(startMillis)} to ${shortDate(endMillis)}"
        }

    /** Safe for a filename on any platform the admin might open this on. */
    val slug: String
        get() = when (this) {
            is Month -> "%04d-%02d".format(year, month + 1)
            is Year -> "%04d".format(year)
            AllTime -> "all-time"
            is Custom -> "${fileDate(startMillis)}-to-${fileDate(endMillis)}"
        }

    /**
     * Whether a chart of this period should have one bar per day or per month.
     *
     * A month is always daily. A custom range follows its own length: two
     * months of days is a readable axis, a year of them is a smear.
     */
    val bucketsByDay: Boolean
        get() = when (this) {
            is Month -> true
            is Custom -> endMillis - startMillis <= 62L * 86_400_000L
            else -> false
        }

    fun contains(epochMillis: Long): Boolean {
        if (this is AllTime) return true
        if (this is Custom) return epochMillis in startMillis..endMillis
        val cal = Calendar.getInstance().apply { timeInMillis = epochMillis }
        return when (this) {
            is Month -> cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) == month
            is Year -> cal.get(Calendar.YEAR) == year
            else -> true
        }
    }

    companion object {
        val MONTH_NAMES = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )

        private fun shortDate(ms: Long): String {
            val cal = Calendar.getInstance().apply { timeInMillis = ms }
            return "%d %s %d".format(
                cal.get(Calendar.DAY_OF_MONTH),
                MONTH_NAMES[cal.get(Calendar.MONTH)].take(3),
                cal.get(Calendar.YEAR)
            )
        }

        private fun fileDate(ms: Long): String {
            val cal = Calendar.getInstance().apply { timeInMillis = ms }
            return "%04d%02d%02d".format(
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH)
            )
        }

        /**
         * Turns two dates chosen in the date picker into a range that covers
         * both days in full, in the device's own time zone.
         *
         * Material's picker reports a selection as midnight UTC on the chosen
         * day, so the calendar date is read back in UTC and only then rebuilt
         * locally. Treating the picker's instant as a local one moves the range
         * a day in any zone behind UTC. A pair chosen back to front still means
         * the days between them, so it is ordered first.
         */
        fun customRange(firstPickedUtc: Long, secondPickedUtc: Long): Custom {
            val from = minOf(firstPickedUtc, secondPickedUtc)
            val to = maxOf(firstPickedUtc, secondPickedUtc)
            return Custom(localDay(from, endOfDay = false), localDay(to, endOfDay = true))
        }

        private fun localDay(pickedUtc: Long, endOfDay: Boolean): Long {
            val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                timeInMillis = pickedUtc
            }
            return Calendar.getInstance().apply {
                clear()
                set(
                    utc.get(Calendar.YEAR),
                    utc.get(Calendar.MONTH),
                    utc.get(Calendar.DAY_OF_MONTH),
                    if (endOfDay) 23 else 0,
                    if (endOfDay) 59 else 0,
                    if (endOfDay) 59 else 0
                )
                set(Calendar.MILLISECOND, if (endOfDay) 999 else 0)
            }.timeInMillis
        }
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

    // --- Aggregations for the charts in the PDF reports ---------------------

    /** Rides per calendar day across the period, oldest first. */
    fun ridesPerDay(rides: List<Ride>, period: ReportPeriod): List<Pair<String, Int>> {
        val inPeriod = ridesIn(rides, period)
        if (inPeriod.isEmpty()) return emptyList()

        val counts = sortedMapOf<String, Int>()
        val labels = mutableMapOf<String, String>()
        inPeriod.forEach { ride ->
            val ms = millis(ride.requestedAt) ?: return@forEach
            val cal = Calendar.getInstance().apply { timeInMillis = ms }
            val key = "%04d-%02d-%02d".format(
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH)
            )
            counts[key] = (counts[key] ?: 0) + 1
            // A short period is labelled by day number; a long one by month.
            labels[key] = if (period.bucketsByDay) {
                cal.get(Calendar.DAY_OF_MONTH).toString()
            } else {
                ReportPeriod.MONTH_NAMES[cal.get(Calendar.MONTH)].take(3)
            }
        }

        // Across a year, all time, or a long custom range, collapse to one bar per month.
        if (!period.bucketsByDay) {
            val byMonth = linkedMapOf<String, Int>()
            counts.forEach { (key, n) ->
                val month = key.substring(0, 7)
                byMonth[month] = (byMonth[month] ?: 0) + n
            }
            // A span crossing new year carries the year on the label, or two
            // separate Januaries both read as "Jan".
            val spansYears = byMonth.keys.map { it.substring(0, 4) }.distinct().size > 1
            return byMonth.map { (month, n) ->
                val index = month.substring(5).toInt() - 1
                val name = ReportPeriod.MONTH_NAMES[index].take(3)
                (if (spansYears) "$name ${month.substring(2, 4)}" else name) to n
            }
        }
        return counts.map { (key, n) -> (labels[key] ?: "") to n }
    }

    /** Rides started in each hour of the day, 0 through 23. */
    fun ridesByHour(rides: List<Ride>, period: ReportPeriod): List<Int> {
        val hours = IntArray(24)
        ridesIn(rides, period).forEach { ride ->
            millis(ride.requestedAt)?.let { ms ->
                val cal = Calendar.getInstance().apply { timeInMillis = ms }
                hours[cal.get(Calendar.HOUR_OF_DAY)]++
            }
        }
        return hours.toList()
    }

    /** Rides per weekday, Monday through Sunday. */
    fun ridesByWeekday(rides: List<Ride>, period: ReportPeriod): List<Pair<String, Int>> {
        val order = listOf(
            Calendar.MONDAY to "Mon", Calendar.TUESDAY to "Tue", Calendar.WEDNESDAY to "Wed",
            Calendar.THURSDAY to "Thu", Calendar.FRIDAY to "Fri", Calendar.SATURDAY to "Sat",
            Calendar.SUNDAY to "Sun"
        )
        val counts = mutableMapOf<Int, Int>()
        ridesIn(rides, period).forEach { ride ->
            millis(ride.requestedAt)?.let { ms ->
                val day = Calendar.getInstance().apply { timeInMillis = ms }.get(Calendar.DAY_OF_WEEK)
                counts[day] = (counts[day] ?: 0) + 1
            }
        }
        return order.map { (day, label) -> label to (counts[day] ?: 0) }
    }

    /** The destinations booked most often, busiest first. */
    fun topDestinations(rides: List<Ride>, period: ReportPeriod, limit: Int = 8): List<Pair<String, Int>> =
        ridesIn(rides, period)
            .map { it.dropoffLocation.address }
            .filter { it.isNotBlank() }
            .groupingBy { it }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .take(limit)
            .map { it.key to it.value }

    /** Rides accepted per driver, busiest first. */
    fun ridesPerDriver(
        rides: List<Ride>,
        usersById: Map<String, User>,
        period: ReportPeriod,
        limit: Int = 10
    ): List<Pair<String, Int>> =
        ridesIn(rides, period)
            .filter { it.driverId.isNotBlank() }
            .groupingBy { it.driverId }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .take(limit)
            .map { name(usersById[it.key]) to it.value }

    /** Gross fares earned per driver, in whole pesos, highest first. */
    fun grossPerDriver(
        rides: List<Ride>,
        usersById: Map<String, User>,
        period: ReportPeriod,
        limit: Int = 8
    ): List<Pair<String, Int>> =
        ridesIn(rides, period)
            .filter { it.driverId.isNotBlank() && it.status == RideStatus.COMPLETED }
            .groupBy { it.driverId }
            .mapValues { (_, list) ->
                list.sumOf { if (it.actualFare > 0) it.actualFare else it.estimatedFare }.toInt()
            }
            .entries
            .sortedByDescending { it.value }
            .take(limit)
            .map { name(usersById[it.key]) to it.value }

    /**
     * Share of each driver's accepted rides that they finished, as a percentage.
     *
     * Drivers below a handful of rides are left out: one cancellation out of two
     * rides reads as 50% and says nothing about the driver.
     */
    fun completionRatePerDriver(
        rides: List<Ride>,
        usersById: Map<String, User>,
        period: ReportPeriod,
        minimumRides: Int = 3,
        limit: Int = 8
    ): List<Pair<String, Int>> =
        ridesIn(rides, period)
            .filter { it.driverId.isNotBlank() }
            .groupBy { it.driverId }
            .filterValues { it.size >= minimumRides }
            .mapValues { (_, list) ->
                (list.count { it.status == RideStatus.COMPLETED } * 100.0 / list.size).toInt()
            }
            .entries
            .sortedByDescending { it.value }
            .take(limit)
            .map { name(usersById[it.key]) to it.value }

    /**
     * The registered fleet by verification state. A snapshot rather than a
     * period figure — a driver's approval is where it stands today, not
     * something that happened inside the reporting month.
     */
    fun driversByVerification(drivers: List<Driver>): List<Pair<String, Int>> {
        val counts = linkedMapOf("Approved" to 0, "Pending" to 0, "Rejected" to 0, "Expired" to 0)
        drivers.forEach { driver ->
            val key = driver.verificationStatus.name.lowercase().replaceFirstChar { it.uppercase() }
            counts[key] = (counts[key] ?: 0) + 1
        }
        return counts.toList()
    }

    /** Concerns filed per category, most common first. */
    fun concernsByCategory(complaints: List<Complaint>, period: ReportPeriod): List<Pair<String, Int>> =
        complaintsIn(complaints, period)
            .groupingBy { it.category }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .map { it.key to it.value }

    /** Concerns filed by the role of whoever raised them. */
    fun concernsByReporter(complaints: List<Complaint>, period: ReportPeriod): List<Pair<String, Int>> {
        val counts = linkedMapOf("Passenger" to 0, "Driver" to 0)
        complaintsIn(complaints, period).forEach { c ->
            val key = c.reporterType.name.lowercase().replaceFirstChar { it.uppercase() }
            counts[key] = (counts[key] ?: 0) + 1
        }
        return counts.toList()
    }

    /**
     * How long resolved concerns took, bucketed. Concerns still open are left
     * out rather than dropped into a final bucket: they have no duration yet,
     * and their count is already on the summary tiles.
     */
    fun resolutionTimeBuckets(complaints: List<Complaint>, period: ReportPeriod): List<Pair<String, Int>> {
        val buckets = linkedMapOf(
            "Same day" to 0, "1 day" to 0, "2-3 days" to 0, "4-7 days" to 0, "Over a week" to 0
        )
        complaintsIn(complaints, period).forEach { c ->
            if (c.status != ComplaintStatus.RESOLVED) return@forEach
            val filed = millis(c.createdAt) ?: return@forEach
            val closed = millis(c.resolvedAt) ?: return@forEach
            val days = ((closed - filed) / 86_400_000L).toInt()
            val key = when {
                days < 1 -> "Same day"
                days < 2 -> "1 day"
                days <= 3 -> "2-3 days"
                days <= 7 -> "4-7 days"
                else -> "Over a week"
            }
            buckets[key] = (buckets[key] ?: 0) + 1
        }
        return buckets.map { it.key to it.value }
    }

    fun fileName(kind: String, period: ReportPeriod, extension: String = "csv"): String =
        "trikride-$kind-${period.slug}.$extension".lowercase(Locale.US)
}
