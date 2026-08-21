package com.tpc.trikride.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tpc.trikride.models.Complaint
import com.tpc.trikride.models.Driver
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.User
import com.tpc.trikride.ui.components.SectionCard
import com.tpc.trikride.ui.components.SimplePlaceholder
import com.tpc.trikride.utils.ReportBuilder
import com.tpc.trikride.utils.PdfReportWriter
import com.tpc.trikride.utils.ReportExporter
import com.tpc.trikride.utils.ReportPeriod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** Which of the three reports the admin is exporting. */
private enum class ReportKind(val slug: String, val title: String, val blurb: String) {
    RIDES("rides", "Ride activity", "Every booking in the period with fares, status and both parties."),
    DRIVERS("drivers", "Driver performance", "Rides, completions and gross fares per driver."),
    CONCERNS("concerns", "Concerns and complaints", "What was filed, the categories, and how each was closed.")
}

/**
 * Month and year reports the admin can save or send on.
 *
 * The period list is built from the rides that actually exist, so there are no
 * empty months to pick through, and the summary updates as soon as a period is
 * chosen — the admin can read the numbers here without exporting anything.
 */
@Composable
fun AdminReportsContent(
    rides: List<Ride>,
    drivers: List<Driver>,
    complaints: List<Complaint>,
    usersById: Map<String, User>
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val periods = remember(rides) { ReportBuilder.availablePeriods(rides) }
    // Not keyed on the ride list: rides arrive live, and re-keying here would
    // throw away a date range the admin had just finished picking.
    var period by remember { mutableStateOf<ReportPeriod>(ReportPeriod.AllTime) }
    var status by remember { mutableStateOf<String?>(null) }
    // A year of rides is a long table to draw, so the export runs off the main
    // thread and the buttons stay disabled until it finishes.
    var busy by remember { mutableStateOf(false) }
    var showRangePicker by remember { mutableStateOf(false) }

    if (showRangePicker) {
        DateRangeDialog(
            onDismiss = { showRangePicker = false },
            onConfirm = { start, end ->
                period = ReportPeriod.customRange(start, end)
                status = null
                showRangePicker = false
            }
        )
    }

    // Held between choosing a format and the file picker coming back.
    var pendingCsv by remember { mutableStateOf("") }
    var pendingPdf by remember { mutableStateOf<((java.io.OutputStream) -> Unit)?>(null) }

    val saveCsvLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        status = when {
            uri == null -> null
            ReportExporter.writeTo(context, uri, pendingCsv) -> "Spreadsheet saved."
            else -> "Could not write the file. Try Share instead."
        }
        pendingCsv = ""
    }

    val savePdfLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/pdf")
    ) { uri ->
        val render = pendingPdf
        pendingPdf = null
        if (uri == null || render == null) return@rememberLauncherForActivityResult
        busy = true
        status = "Building the PDF…"
        scope.launch {
            val written = withContext(Dispatchers.IO) {
                ReportExporter.writeTo(context, uri, render)
            }
            status = if (written) "PDF saved." else "Could not write the file. Try Share instead."
            busy = false
        }
    }

    val summary = remember(rides, complaints, period) {
        ReportBuilder.summarise(rides, complaints, period)
    }

    fun csvFor(kind: ReportKind): String = when (kind) {
        ReportKind.RIDES -> ReportBuilder.ridesCsv(rides, complaints, usersById, period)
        ReportKind.DRIVERS -> ReportBuilder.driversCsv(rides, drivers, usersById, period)
        ReportKind.CONCERNS -> ReportBuilder.complaintsCsv(complaints, period)
    }

    fun pdfFor(kind: ReportKind): (java.io.OutputStream) -> Unit = { out ->
        when (kind) {
            ReportKind.RIDES ->
                PdfReportWriter.writeRideReport(out, rides, complaints, usersById, period)
            ReportKind.DRIVERS ->
                PdfReportWriter.writeDriverReport(out, rides, drivers, usersById, period)
            ReportKind.CONCERNS ->
                PdfReportWriter.writeConcernReport(out, complaints, period)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column {
                Text(
                    "Reports",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Export a month, a year, or any range of dates as a printable " +
                        "PDF or a spreadsheet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            SectionCard {
                Column {
                    Text(
                        "Period",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    PeriodDropdown(
                        periods = periods,
                        selected = period,
                        onSelected = { period = it; status = null },
                        onCustomRequested = { showRangePicker = true }
                    )
                    if (period is ReportPeriod.Custom) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Both dates are included in full.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        if (rides.isEmpty()) {
            item {
                SimplePlaceholder(
                    icon = Icons.Filled.Description,
                    title = "No activity yet",
                    message = "Once rides start coming through, reports for any " +
                        "month, year, or range of dates will be available here."
                )
            }
            return@LazyColumn
        }

        item {
            SectionCard {
                Column {
                    Text(
                        period.label,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    SummaryLine("Total rides", "${summary.totalRides}")
                    SummaryLine("Completed", "${summary.completed}")
                    SummaryLine("Cancelled or no-show", "${summary.cancelled}")
                    SummaryLine("Still open", "${summary.inProgress}")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    SummaryLine("Gross fares", "P%.2f".format(summary.grossFares), strong = true)
                    SummaryLine("Average completed fare", "P%.2f".format(summary.averageFare))
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    SummaryLine("Passengers served", "${summary.uniquePassengers}")
                    SummaryLine("Drivers with a ride", "${summary.activeDrivers}")
                    SummaryLine(
                        "Concerns filed",
                        "${summary.complaintsFiled} (${summary.complaintsResolved} resolved)"
                    )
                }
            }
        }

        items(ReportKind.entries.size) { index ->
            val kind = ReportKind.entries[index]
            SectionCard {
                Column {
                    Text(
                        kind.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        kind.blurb,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "The PDF is the one to print or hand over: headline figures and " +
                            "charts first, then every record behind them. The spreadsheet " +
                            "is the same data for sorting and totalling in Excel.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    // Both formats, both ways out. Saving as PDF is the filled
                    // button because printing or handing over a copy is what
                    // this screen is usually opened for.
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = {
                                pendingPdf = pdfFor(kind)
                                status = null
                                savePdfLauncher.launch(
                                    ReportBuilder.fileName(kind.slug, period, "pdf")
                                )
                            },
                            enabled = !busy,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Filled.PictureAsPdf,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save PDF")
                        }
                        OutlinedButton(
                            onClick = {
                                val render = pdfFor(kind)
                                val name = ReportBuilder.fileName(kind.slug, period, "pdf")
                                val subject = "TrikRide ${kind.title} — ${period.label}"
                                busy = true
                                status = "Building the PDF…"
                                scope.launch {
                                    val file = withContext(Dispatchers.IO) {
                                        ReportExporter.renderToCache(context, name, render)
                                    }
                                    // The chooser has to be started from the main
                                    // thread, so only the drawing goes to IO.
                                    val sent = file != null &&
                                        ReportExporter.share(context, file, "application/pdf", subject)
                                    status = if (sent) null else "Could not prepare the file to send."
                                    busy = false
                                }
                            },
                            enabled = !busy,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Filled.Share,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Send PDF")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(
                            onClick = {
                                pendingCsv = csvFor(kind)
                                status = null
                                saveCsvLauncher.launch(ReportBuilder.fileName(kind.slug, period))
                            },
                            enabled = !busy,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Filled.Download,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save sheet")
                        }
                        OutlinedButton(
                            onClick = {
                                val sent = ReportExporter.share(
                                    context = context,
                                    fileName = ReportBuilder.fileName(kind.slug, period),
                                    content = csvFor(kind),
                                    subject = "TrikRide ${kind.title} — ${period.label}"
                                )
                                status = if (sent) null else "No app available to send the file."
                            },
                            enabled = !busy,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Filled.Description,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Send sheet")
                        }
                    }
                }
            }
        }

        status?.let { message ->
            item {
                Text(
                    message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PeriodDropdown(
    periods: List<ReportPeriod>,
    selected: ReportPeriod,
    onSelected: (ReportPeriod) -> Unit,
    onCustomRequested: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = selected.label,
            onValueChange = {},
            readOnly = true,
            label = { Text("Covering") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            periods.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.label) },
                    onClick = { onSelected(option); expanded = false }
                )
            }
            HorizontalDivider()
            // The months and years above come from the data. This one does not,
            // so it opens a picker rather than sitting in the same list.
            DropdownMenuItem(
                text = { Text("Choose exact dates…") },
                leadingIcon = {
                    Icon(
                        Icons.Filled.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                onClick = { expanded = false; onCustomRequested() }
            )
        }
    }
}

/**
 * The date-range picker behind "Choose exact dates".
 *
 * Confirm stays disabled until both ends are chosen: a range with only a start
 * has no meaning here, and running the report on a half-made selection is worse
 * than making the admin finish it.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateRangeDialog(
    onDismiss: () -> Unit,
    onConfirm: (Long, Long) -> Unit
) {
    val state = rememberDateRangePickerState()
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            val start = state.selectedStartDateMillis
            val end = state.selectedEndDateMillis
            TextButton(
                onClick = { if (start != null && end != null) onConfirm(start, end) },
                enabled = start != null && end != null
            ) { Text("Use these dates") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    ) {
        DateRangePicker(
            state = state,
            title = {
                Text(
                    "Report period",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 24.dp, top = 16.dp)
                )
            },
            headline = {
                Text(
                    "Pick the first and last day",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 24.dp, bottom = 8.dp)
                )
            },
            showModeToggle = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
        )
    }
}

@Composable
private fun SummaryLine(label: String, value: String, strong: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            value,
            style = if (strong) MaterialTheme.typography.titleMedium
            else MaterialTheme.typography.bodyMedium,
            fontWeight = if (strong) FontWeight.Bold else FontWeight.Medium,
            color = if (strong) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface
        )
    }
}
