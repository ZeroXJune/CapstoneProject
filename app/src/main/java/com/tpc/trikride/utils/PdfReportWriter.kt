package com.tpc.trikride.utils

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import com.tpc.trikride.models.Complaint
import com.tpc.trikride.models.ComplaintStatus
import com.tpc.trikride.models.Driver
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.RideStatus
import com.tpc.trikride.models.User
import java.io.OutputStream
import java.util.Calendar

/**
 * Builds the printable reports.
 *
 * Uses Android's own [PdfDocument], so no library and no cost. Pages are Letter
 * landscape at 72 points to the inch — landscape because the ride table has
 * seventeen columns, and squeezing those onto portrait means either a font
 * nobody can read or dropping the columns that make the table worth having.
 *
 * Each report opens with a summary page: the headline figures as stat tiles,
 * then charts, then the detail table paginated behind it with its header
 * repeated on every page. Someone who reads only the first page should come
 * away with the answer; the table is there for whoever wants to check it.
 */
object PdfReportWriter {

    private const val PAGE_WIDTH = 792
    private const val PAGE_HEIGHT = 612
    private const val MARGIN = 36f
    private const val ROW_HEIGHT = 14f
    private const val HEADER_HEIGHT = 16f

    /** Space left of a plot for its y-axis numbers, which hang outside the frame. */
    private const val Y_LABEL_GUTTER = 20f
    private const val COL_GAP = 28f
    private const val ROW_GAP = 26f

    /** Lowest a chart may reach before it runs into the footer. */
    private const val CHART_FLOOR = PAGE_HEIGHT - 34f

    private data class Column(val title: String, val weight: Float, val rightAligned: Boolean = false)

    /**
     * A chart's frame within a grid of them.
     *
     * [yLabels] shifts the left edge inward for charts that number their y-axis,
     * so the numbers land in the gutter instead of over the neighbour.
     */
    private fun cell(
        left: Float,
        top: Float,
        right: Float,
        height: Float,
        yLabels: Boolean = false
    ) = RectF(left + if (yLabels) Y_LABEL_GUTTER else 0f, top, right, top + height)

    /** Splits what is left of the page below [top] into [rows] equal bands. */
    private fun rowHeight(top: Float, rows: Int): Float =
        (CHART_FLOOR - top - ROW_GAP * (rows - 1)) / rows

    // ---------------------------------------------------------------- Rides

    fun writeRideReport(
        out: OutputStream,
        rides: List<Ride>,
        complaints: List<Complaint>,
        usersById: Map<String, User>,
        period: ReportPeriod
    ) {
        val doc = PdfDocument()
        val summary = ReportBuilder.summarise(rides, complaints, period)
        val inPeriod = ReportBuilder.ridesIn(rides, period)

        val page = startPage(doc, 1)
        val canvas = page.canvas
        var y = drawHeader(canvas, "Ride Activity Report", period)

        y = drawStatTiles(
            canvas, y,
            listOf(
                Triple("Total rides", "${summary.totalRides}", null),
                Triple("Completed", "${summary.completed}", completionNote(summary)),
                Triple("Cancelled / no-show", "${summary.cancelled}", null),
                Triple("Gross fares", peso(summary.grossFares), null),
                Triple("Average fare", peso(summary.averageFare), "per completed ride"),
                Triple("Passengers served", "${summary.uniquePassengers}", null),
                Triple("Drivers with a ride", "${summary.activeDrivers}", null)
            )
        )

        // Four charts in two rows: when the demand fell, at what hour, to where,
        // and on which days of the week.
        val perDay = ReportBuilder.ridesPerDay(rides, period)
        val byHour = ReportBuilder.ridesByHour(rides, period)
        val destinations = ReportBuilder.topDestinations(rides, period)
        val byWeekday = ReportBuilder.ridesByWeekday(rides, period)

        val top1 = y + 20f
        val band = rowHeight(top1, rows = 2)
        val top2 = top1 + band + ROW_GAP
        val usable = PAGE_WIDTH - MARGIN * 2

        // Row one leans wide-left: a month of days needs the room, twenty-four
        // hours do not. Row two the same way, for destination names that run long.
        val wide = (usable - COL_GAP) * 0.62f
        val narrowLeft = MARGIN + wide + COL_GAP
        val half = (usable - COL_GAP) * 0.58f
        val weekdayLeft = MARGIN + half + COL_GAP

        PdfChart.columnChart(
            canvas,
            cell(MARGIN, top1, MARGIN + wide, band, yLabels = true),
            title = if (period.bucketsByDay) "Rides per day" else "Rides per month",
            subtitle = "when the demand fell",
            values = perDay.map { it.second },
            labels = perDay.map { it.first },
            labelEvery = if (perDay.size > 16) 3 else 1
        )

        PdfChart.columnChart(
            canvas,
            cell(narrowLeft, top1, PAGE_WIDTH - MARGIN, band, yLabels = true),
            title = "Rides by hour of day",
            subtitle = "when to have drivers waiting",
            values = byHour,
            labels = (0..23).map { "%02d".format(it) },
            labelEvery = 3
        )

        PdfChart.barChart(
            canvas,
            cell(MARGIN, top2, MARGIN + half, band),
            title = "Most-booked destinations",
            subtitle = "busiest first",
            rows = destinations
        )

        PdfChart.columnChart(
            canvas,
            cell(weekdayLeft, top2, PAGE_WIDTH - MARGIN, band, yLabels = true),
            title = "Rides by day of the week",
            subtitle = "which days carry the load",
            values = byWeekday.map { it.second },
            labels = byWeekday.map { it.first }
        )

        drawFooter(canvas, 1)
        doc.finishPage(page)

        // The detail table, from page two.
        val columns = listOf(
            Column("Requested", 1.15f),
            Column("Passenger", 1.2f),
            Column("Driver", 1.2f),
            Column("Pickup", 1.5f),
            Column("Destination", 1.7f),
            Column("Pax", 0.35f, rightAligned = true),
            Column("Rate", 0.6f),
            Column("Status", 0.85f),
            Column("Fare", 0.6f, rightAligned = true)
        )
        val rows = inPeriod.map { ride ->
            val passenger = usersById[ride.passengerId]
            val driver = usersById[ride.driverId]
            listOf(
                shortDateTime(ride.requestedAt),
                displayName(passenger),
                if (ride.driverId.isBlank()) "Unassigned" else displayName(driver),
                ride.pickupLocation.address,
                ride.dropoffLocation.address,
                "${ride.passengerCount}",
                if (ride.fareType.name == "DISCOUNTED") "Disc." else "Regular",
                prettyStatus(ride.status),
                peso(if (ride.actualFare > 0) ride.actualFare else ride.estimatedFare)
            )
        }
        writeTablePages(doc, "Ride Activity Report", period, columns, rows, startingPage = 2)

        doc.writeTo(out)
        doc.close()
    }

    // -------------------------------------------------------------- Drivers

    fun writeDriverReport(
        out: OutputStream,
        rides: List<Ride>,
        drivers: List<Driver>,
        usersById: Map<String, User>,
        period: ReportPeriod
    ) {
        val doc = PdfDocument()
        val inPeriod = ReportBuilder.ridesIn(rides, period).filter { it.driverId.isNotBlank() }
        val byDriver = inPeriod.groupBy { it.driverId }
        val driversById = drivers.associateBy { it.userId }
        val completed = inPeriod.count { it.status == RideStatus.COMPLETED }
        val gross = inPeriod.filter { it.status == RideStatus.COMPLETED }
            .sumOf { if (it.actualFare > 0) it.actualFare else it.estimatedFare }

        val page = startPage(doc, 1)
        val canvas = page.canvas
        var y = drawHeader(canvas, "Driver Performance Report", period)

        y = drawStatTiles(
            canvas, y,
            listOf(
                Triple("Drivers with a ride", "${byDriver.size}", "of ${drivers.size} registered"),
                Triple("Rides accepted", "${inPeriod.size}", null),
                Triple("Completed", "$completed", null),
                Triple("Gross fares", peso(gross), null),
                Triple(
                    "Average per driver",
                    if (byDriver.isEmpty()) "0" else "%.1f".format(inPeriod.size.toDouble() / byDriver.size),
                    "rides"
                )
            )
        )

        // Volume, then money, then whether the rides were actually finished.
        val top1 = y + 20f
        val band = rowHeight(top1, rows = 2)
        val top2 = top1 + band + ROW_GAP
        val usable = PAGE_WIDTH - MARGIN * 2
        val leftWidth = (usable - COL_GAP) * 0.55f
        val rightLeft = MARGIN + leftWidth + COL_GAP

        PdfChart.barChart(
            canvas,
            cell(MARGIN, top1, MARGIN + leftWidth, band),
            title = "Rides accepted per driver",
            subtitle = "busiest first",
            rows = ReportBuilder.ridesPerDriver(rides, usersById, period, limit = 8)
        )

        PdfChart.barChart(
            canvas,
            cell(rightLeft, top1, PAGE_WIDTH - MARGIN, band),
            title = "Gross fares per driver",
            subtitle = "completed rides only, in pesos",
            rows = ReportBuilder.grossPerDriver(rides, usersById, period)
        )

        PdfChart.barChart(
            canvas,
            cell(MARGIN, top2, MARGIN + leftWidth, band),
            title = "Completion rate per driver",
            subtitle = "share of accepted rides finished, three rides or more",
            rows = ReportBuilder.completionRatePerDriver(rides, usersById, period),
            valueSuffix = "%",
            axisMax = 100
        )

        val byApproval = ReportBuilder.driversByVerification(drivers)
        PdfChart.columnChart(
            canvas,
            cell(rightLeft, top2, PAGE_WIDTH - MARGIN, band, yLabels = true),
            title = "Registered fleet by approval state",
            subtitle = "the whole roster as it stands today",
            values = byApproval.map { it.second },
            labels = byApproval.map { it.first }
        )

        drawFooter(canvas, 1)
        doc.finishPage(page)

        val columns = listOf(
            Column("Driver", 1.5f),
            Column("Mobile", 1.0f),
            Column("Tricycle", 0.9f),
            Column("Verification", 0.9f),
            Column("Accepted", 0.6f, rightAligned = true),
            Column("Completed", 0.7f, rightAligned = true),
            Column("Cancelled", 0.7f, rightAligned = true),
            Column("Gross", 0.8f, rightAligned = true),
            Column("Rating", 0.55f, rightAligned = true)
        )
        val rows = byDriver.entries.sortedByDescending { it.value.size }.map { (id, driverRides) ->
            val user = usersById[id]
            val record = driversById[id]
            val done = driverRides.filter { it.status == RideStatus.COMPLETED }
            val g = done.sumOf { if (it.actualFare > 0) it.actualFare else it.estimatedFare }
            listOf(
                displayName(user),
                user?.phoneNumber.orEmpty(),
                record?.tricycleNumber.orEmpty(),
                record?.verificationStatus?.name?.lowercase()?.replaceFirstChar { it.uppercase() }.orEmpty(),
                "${driverRides.size}",
                "${done.size}",
                "${driverRides.count { it.status == RideStatus.CANCELLED || it.status == RideStatus.NO_SHOW }}",
                peso(g),
                "%.1f".format(record?.rating ?: 0.0)
            )
        }
        writeTablePages(doc, "Driver Performance Report", period, columns, rows, startingPage = 2)

        doc.writeTo(out)
        doc.close()
    }

    // ------------------------------------------------------------- Concerns

    fun writeConcernReport(
        out: OutputStream,
        complaints: List<Complaint>,
        usersById: Map<String, User>,
        period: ReportPeriod
    ) {
        val doc = PdfDocument()
        val inPeriod = ReportBuilder.complaintsIn(complaints, period)

        val page = startPage(doc, 1)
        val canvas = page.canvas
        var y = drawHeader(canvas, "Concerns and Complaints Report", period)

        val resolved = inPeriod.count { it.status == ComplaintStatus.RESOLVED }
        y = drawStatTiles(
            canvas, y,
            listOf(
                Triple("Filed", "${inPeriod.size}", null),
                Triple("Resolved", "$resolved", resolutionNote(inPeriod.size, resolved)),
                Triple("In review", "${inPeriod.count { it.status == ComplaintStatus.IN_REVIEW }}", null),
                Triple("Still open", "${inPeriod.count { it.status == ComplaintStatus.OPEN }}", null)
            )
        )

        val top1 = y + 20f
        val band = rowHeight(top1, rows = 2)
        val top2 = top1 + band + ROW_GAP
        val usable = PAGE_WIDTH - MARGIN * 2
        val leftWidth = (usable - COL_GAP) * 0.58f
        val rightLeft = MARGIN + leftWidth + COL_GAP
        val perMonth = concernsPerMonth(inPeriod)

        PdfChart.barChart(
            canvas,
            cell(MARGIN, top1, MARGIN + leftWidth, band),
            title = "What was reported",
            subtitle = "by category, most common first",
            rows = ReportBuilder.concernsByCategory(complaints, period)
        )

        PdfChart.columnChart(
            canvas,
            cell(rightLeft, top1, PAGE_WIDTH - MARGIN, band, yLabels = true),
            title = "Concerns per month",
            subtitle = "when they were filed",
            values = perMonth.map { it.second },
            labels = perMonth.map { it.first }
        )

        val buckets = ReportBuilder.resolutionTimeBuckets(complaints, period)
        PdfChart.columnChart(
            canvas,
            cell(MARGIN, top2, MARGIN + leftWidth, band, yLabels = true),
            title = "How long a concern took to close",
            subtitle = "resolved concerns only; open ones have no duration yet",
            values = buckets.map { it.second },
            labels = buckets.map { it.first }
        )

        val byReporter = ReportBuilder.concernsByReporter(complaints, period)
        PdfChart.columnChart(
            canvas,
            cell(rightLeft, top2, PAGE_WIDTH - MARGIN, band, yLabels = true),
            title = "Who raised them",
            subtitle = "passengers and drivers both file here",
            values = byReporter.map { it.second },
            labels = byReporter.map { it.first }
        )

        drawFooter(canvas, 1)
        doc.finishPage(page)

        val columns = listOf(
            Column("Filed", 1.0f),
            Column("Reported by", 1.2f),
            Column("Role", 0.7f),
            Column("Category", 1.1f),
            Column("Description", 2.6f),
            Column("Status", 0.8f),
            Column("Admin note", 1.8f),
            Column("Resolved", 1.0f)
        )
        val rows = inPeriod.map { c ->
            listOf(
                shortDateTime(c.createdAt),
                ReportBuilder.reporterName(c, usersById),
                c.reporterType.name.lowercase().replaceFirstChar { it.uppercase() },
                c.category,
                c.description,
                c.status.name.replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() },
                c.adminNote,
                shortDateTime(c.resolvedAt)
            )
        }
        writeTablePages(doc, "Concerns and Complaints Report", period, columns, rows, startingPage = 2)

        doc.writeTo(out)
        doc.close()
    }

    // ---------------------------------------------------------------- Parts

    private fun startPage(doc: PdfDocument, number: Int): PdfDocument.Page =
        doc.startPage(PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, number).create())

    private fun drawHeader(canvas: Canvas, title: String, period: ReportPeriod): Float {
        canvas.drawText("TrikRide", MARGIN, MARGIN + 4f, PdfChart.paint(PdfChart.SERIES, 13f, bold = true))
        canvas.drawText(
            "Talibon Polytechnic College",
            MARGIN, MARGIN + 15f, PdfChart.paint(PdfChart.INK_MUTED, 7f)
        )
        canvas.drawText(
            title,
            PAGE_WIDTH - MARGIN, MARGIN + 4f,
            PdfChart.paint(PdfChart.INK_PRIMARY, 13f, bold = true, align = Paint.Align.RIGHT)
        )
        canvas.drawText(
            "${period.label}   ·   generated ${today()}",
            PAGE_WIDTH - MARGIN, MARGIN + 15f,
            PdfChart.paint(PdfChart.INK_SECONDARY, 7f, align = Paint.Align.RIGHT)
        )
        val ruleY = MARGIN + 23f
        canvas.drawLine(
            MARGIN, ruleY, PAGE_WIDTH - MARGIN, ruleY,
            PdfChart.paint(PdfChart.AXIS).apply { strokeWidth = 0.6f }
        )
        return ruleY + 12f
    }

    private fun drawFooter(canvas: Canvas, page: Int) {
        canvas.drawText(
            "Page $page",
            PAGE_WIDTH - MARGIN, PAGE_HEIGHT - 18f,
            PdfChart.paint(PdfChart.INK_MUTED, 6.5f, align = Paint.Align.RIGHT)
        )
        canvas.drawText(
            "Fares follow the schedule published by the Federation of Tricycle Operators " +
                "and Drivers Association of Talibon.",
            MARGIN, PAGE_HEIGHT - 18f, PdfChart.paint(PdfChart.INK_MUTED, 6.5f)
        )
    }

    private fun drawStatTiles(canvas: Canvas, top: Float, tiles: List<Triple<String, String, String?>>): Float {
        val gap = 8f
        val width = (PAGE_WIDTH - MARGIN * 2 - gap * (tiles.size - 1)) / tiles.size
        val height = 54f
        tiles.forEachIndexed { i, (label, value, note) ->
            val left = MARGIN + (width + gap) * i
            PdfChart.statTile(canvas, RectF(left, top, left + width, top + height), label, value, note)
        }
        return top + height
    }

    /** Writes the detail table across as many pages as it needs. */
    private fun writeTablePages(
        doc: PdfDocument,
        title: String,
        period: ReportPeriod,
        columns: List<Column>,
        rows: List<List<String>>,
        startingPage: Int
    ) {
        if (rows.isEmpty()) return

        val totalWeight = columns.sumOf { it.weight.toDouble() }.toFloat()
        val usable = PAGE_WIDTH - MARGIN * 2
        val widths = columns.map { usable * (it.weight / totalWeight) }

        var index = 0
        var pageNumber = startingPage
        while (index < rows.size) {
            val page = startPage(doc, pageNumber)
            val canvas = page.canvas
            var y = drawHeader(canvas, title, period)
            y += 4f

            canvas.drawText(
                "Detail — ${rows.size} record${if (rows.size == 1) "" else "s"}",
                MARGIN, y, PdfChart.paint(PdfChart.INK_PRIMARY, 9f, bold = true)
            )
            y += 12f

            y = drawTableHeader(canvas, y, columns, widths)

            val bottomLimit = PAGE_HEIGHT - 34f
            while (index < rows.size && y + ROW_HEIGHT < bottomLimit) {
                drawTableRow(canvas, y, columns, widths, rows[index], index)
                y += ROW_HEIGHT
                index++
            }

            drawFooter(canvas, pageNumber)
            doc.finishPage(page)
            pageNumber++
        }
    }

    private fun drawTableHeader(canvas: Canvas, top: Float, columns: List<Column>, widths: List<Float>): Float {
        val fill = PdfChart.paint(PdfChart.TILE_FILL).apply { style = Paint.Style.FILL }
        canvas.drawRect(RectF(MARGIN, top, PAGE_WIDTH - MARGIN, top + HEADER_HEIGHT), fill)
        var x = MARGIN
        columns.forEachIndexed { i, col ->
            val p = PdfChart.paint(
                PdfChart.INK_SECONDARY, 6.5f, bold = true,
                align = if (col.rightAligned) Paint.Align.RIGHT else Paint.Align.LEFT
            )
            canvas.drawText(
                col.title.uppercase(),
                if (col.rightAligned) x + widths[i] - 4f else x + 4f,
                top + 11f, p
            )
            x += widths[i]
        }
        return top + HEADER_HEIGHT
    }

    private fun drawTableRow(
        canvas: Canvas,
        top: Float,
        columns: List<Column>,
        widths: List<Float>,
        row: List<String>,
        index: Int
    ) {
        // A very light band every other row, so the eye can track across a wide
        // table without needing rules between every column.
        if (index % 2 == 1) {
            canvas.drawRect(
                RectF(MARGIN, top, PAGE_WIDTH - MARGIN, top + ROW_HEIGHT),
                PdfChart.paint(0xFFFAFAF8.toInt()).apply { style = Paint.Style.FILL }
            )
        }
        var x = MARGIN
        columns.forEachIndexed { i, col ->
            val p = PdfChart.paint(
                PdfChart.INK_PRIMARY, 7f,
                align = if (col.rightAligned) Paint.Align.RIGHT else Paint.Align.LEFT
            )
            val text = PdfChart.ellipsize(row.getOrElse(i) { "" }, p, widths[i] - 8f)
            canvas.drawText(
                text,
                if (col.rightAligned) x + widths[i] - 4f else x + 4f,
                top + 9.5f, p
            )
            x += widths[i]
        }
    }

    // ----------------------------------------------------------- Formatting

    private fun peso(value: Double) = "P%,.2f".format(value)

    private fun completionNote(s: ReportSummary): String? =
        if (s.totalRides == 0) null
        else "%.0f%% of all rides".format(s.completed * 100.0 / s.totalRides)

    private fun resolutionNote(filed: Int, resolved: Int): String? =
        if (filed == 0) null else "%.0f%% of those filed".format(resolved * 100.0 / filed)

    private fun displayName(user: User?): String = when {
        user == null -> "Unknown"
        else -> listOf(user.firstName, user.lastName).filter { it.isNotBlank() }
            .joinToString(" ").ifBlank { user.email }
    }

    private fun prettyStatus(status: RideStatus): String =
        status.name.replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() }

    private fun shortDateTime(raw: String): String {
        val ms = raw.toLongOrNull() ?: return ""
        val cal = Calendar.getInstance().apply { timeInMillis = ms }
        return "%02d %s %02d:%02d".format(
            cal.get(Calendar.DAY_OF_MONTH),
            ReportPeriod.MONTH_NAMES[cal.get(Calendar.MONTH)].take(3),
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE)
        )
    }

    private fun today(): String {
        val cal = Calendar.getInstance()
        return "%d %s %d".format(
            cal.get(Calendar.DAY_OF_MONTH),
            ReportPeriod.MONTH_NAMES[cal.get(Calendar.MONTH)],
            cal.get(Calendar.YEAR)
        )
    }

    /**
     * Concerns per month, oldest first. Keyed by year and month rather than
     * month alone: over a year boundary two Januaries would otherwise fall in
     * the same bucket and label.
     */
    private fun concernsPerMonth(complaints: List<Complaint>): List<Pair<String, Int>> {
        val counts = sortedMapOf<Int, Int>()
        complaints.forEach { c ->
            val ms = c.createdAt.toLongOrNull() ?: return@forEach
            val cal = Calendar.getInstance().apply { timeInMillis = ms }
            val key = cal.get(Calendar.YEAR) * 12 + cal.get(Calendar.MONTH)
            counts[key] = (counts[key] ?: 0) + 1
        }
        val spansYears = counts.keys.map { it / 12 }.distinct().size > 1
        return counts.map { (key, n) ->
            val name = ReportPeriod.MONTH_NAMES[key % 12].take(3)
            (if (spansYears) "$name ${"%02d".format((key / 12) % 100)}" else name) to n
        }
    }
}
