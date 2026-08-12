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
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.tpc.trikride.utils.ReportExporter
import com.tpc.trikride.utils.ReportPeriod

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
    val periods = remember(rides) { ReportBuilder.availablePeriods(rides) }
    var period by remember(periods) { mutableStateOf(periods.first()) }
    var status by remember { mutableStateOf<String?>(null) }

    // Held between choosing "Save" and the file picker coming back.
    var pendingCsv by remember { mutableStateOf("") }

    val saveLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        status = when {
            uri == null -> null
            ReportExporter.writeTo(context, uri, pendingCsv) -> "Report saved."
            else -> "Could not write the file. Try Share instead."
        }
        pendingCsv = ""
    }

    val summary = remember(rides, complaints, period) {
        ReportBuilder.summarise(rides, complaints, period)
    }

    fun csvFor(kind: ReportKind): String = when (kind) {
        ReportKind.RIDES -> ReportBuilder.ridesCsv(rides, complaints, usersById, period)
        ReportKind.DRIVERS -> ReportBuilder.driversCsv(rides, drivers, usersById, period)
        ReportKind.CONCERNS -> ReportBuilder.complaintsCsv(complaints, period)
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
                    "Export a month or a year of activity as a spreadsheet.",
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
                        onSelected = { period = it; status = null }
                    )
                }
            }
        }

        if (rides.isEmpty()) {
            item {
                SimplePlaceholder(
                    icon = Icons.Filled.Description,
                    title = "No activity yet",
                    message = "Once rides start coming through, monthly and yearly " +
                        "reports will be available here."
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
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(
                            onClick = {
                                pendingCsv = csvFor(kind)
                                status = null
                                saveLauncher.launch(ReportBuilder.fileName(kind.slug, period))
                            },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Filled.Download,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save")
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
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Filled.Share,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Share")
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
    onSelected: (ReportPeriod) -> Unit
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
        }
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
