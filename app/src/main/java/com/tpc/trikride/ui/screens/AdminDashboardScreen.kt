package com.tpc.trikride.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tpc.trikride.models.Complaint
import com.tpc.trikride.models.ComplaintStatus
import com.tpc.trikride.models.Driver
import com.tpc.trikride.models.FareConfig
import com.tpc.trikride.models.FareStop
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.RideStatus
import com.tpc.trikride.models.User
import com.tpc.trikride.models.VerificationStatus
import com.tpc.trikride.ui.components.PrimaryButton
import com.tpc.trikride.ui.components.SectionCard
import com.tpc.trikride.utils.FareSeed
import com.tpc.trikride.utils.LicenceImage
import com.tpc.trikride.ui.theme.ErrorColor
import com.tpc.trikride.ui.theme.SuccessColor
import com.tpc.trikride.ui.theme.WarningColor
import com.tpc.trikride.viewmodels.AdminViewModel

private enum class AdminTab { VERIFY, CONCERNS, MONITOR, FARES, PROFILE }

@Composable
fun AdminDashboardScreen(
    userId: String,
    onSignOut: () -> Unit,
    viewModel: AdminViewModel = viewModel()
) {
    val drivers by viewModel.drivers.collectAsState()
    val users by viewModel.users.collectAsState()
    val rides by viewModel.rides.collectAsState()
    val fareConfig by viewModel.fareConfig.collectAsState()
    val fareStops by viewModel.fareStops.collectAsState()
    val fareSaved by viewModel.fareSaved.collectAsState()
    val importing by viewModel.importing.collectAsState()
    val complaints by viewModel.complaints.collectAsState()
    val licenceImages by viewModel.licenceImages.collectAsState()

    var tab by remember { mutableStateOf(AdminTab.VERIFY) }
    val usersById = remember(users) { users.associateBy { it.id } }
    val pendingCount = drivers.count { it.verificationStatus == VerificationStatus.PENDING }
    val openConcerns = complaints.count { it.status != ComplaintStatus.RESOLVED }

    Scaffold(
        bottomBar = {
            AdminBottomBar(
                selected = tab,
                onSelect = { tab = it },
                pending = pendingCount,
                concerns = openConcerns
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (tab) {
                AdminTab.VERIFY -> VerificationContent(
                    drivers = drivers,
                    usersById = usersById,
                    licenceImages = licenceImages,
                    onOpenLicence = viewModel::openLicenceImage,
                    onCloseLicence = viewModel::closeLicenceImage,
                    onApprove = viewModel::approveDriver,
                    onReject = viewModel::rejectDriver,
                    onRevoke = viewModel::revokeApproval
                )
                AdminTab.CONCERNS -> ConcernsContent(
                    complaints = complaints,
                    usersById = usersById,
                    onUpdate = viewModel::updateComplaint
                )
                AdminTab.MONITOR -> MonitorContent(
                    drivers = drivers,
                    rides = rides,
                    complaints = complaints,
                    usersById = usersById
                )
                AdminTab.FARES -> FareConfigContent(
                    config = fareConfig,
                    stops = fareStops,
                    saved = fareSaved,
                    importing = importing,
                    onSaveConfig = viewModel::saveFareConfig,
                    onSaveStop = viewModel::saveFareStop,
                    onDeleteStop = viewModel::deleteFareStop,
                    onImport = viewModel::importOfficialRates,
                    onAcknowledgeSaved = viewModel::acknowledgeFareSaved
                )
                AdminTab.PROFILE -> SettingsScreen(
                    userId = userId,
                    userType = com.tpc.trikride.models.UserType.ADMIN,
                    subtitle = "Talibon Polytechnic College",
                    onSignOut = onSignOut
                )
            }
        }
    }
}

@Composable
private fun AdminBottomBar(
    selected: AdminTab,
    onSelect: (AdminTab) -> Unit,
    pending: Int,
    concerns: Int
) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        NavigationBarItem(
            selected = selected == AdminTab.VERIFY,
            onClick = { onSelect(AdminTab.VERIFY) },
            icon = {
                BadgedBox(badge = { if (pending > 0) Badge { Text("$pending") } }) {
                    Icon(Icons.Filled.VerifiedUser, contentDescription = "Verify")
                }
            },
            label = { Text("Verify") }
        )
        NavigationBarItem(
            selected = selected == AdminTab.CONCERNS,
            onClick = { onSelect(AdminTab.CONCERNS) },
            icon = {
                BadgedBox(badge = { if (concerns > 0) Badge { Text("$concerns") } }) {
                    Icon(Icons.Filled.ReportProblem, contentDescription = "Concerns")
                }
            },
            label = { Text("Concerns") }
        )
        NavigationBarItem(
            selected = selected == AdminTab.MONITOR,
            onClick = { onSelect(AdminTab.MONITOR) },
            icon = { Icon(Icons.Filled.Insights, contentDescription = "Monitor") },
            label = { Text("Monitor") }
        )
        NavigationBarItem(
            selected = selected == AdminTab.FARES,
            onClick = { onSelect(AdminTab.FARES) },
            icon = { Icon(Icons.Filled.Payments, contentDescription = "Fares") },
            label = { Text("Fares") }
        )
        NavigationBarItem(
            selected = selected == AdminTab.PROFILE,
            onClick = { onSelect(AdminTab.PROFILE) },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}

@Composable
private fun ConcernsContent(
    complaints: List<Complaint>,
    usersById: Map<String, User>,
    onUpdate: (Complaint, ComplaintStatus, String) -> Unit
) {
    val open = complaints.filter { it.status != ComplaintStatus.RESOLVED }
        .sortedByDescending { it.createdAt.toLongOrNull() ?: 0L }
    val resolved = complaints.filter { it.status == ComplaintStatus.RESOLVED }
        .sortedByDescending { it.createdAt.toLongOrNull() ?: 0L }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text("Concerns & Complaints", style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold)
        Text("Reports submitted by passengers and drivers.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(20.dp))

        Text("Open (${open.size})", style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        if (open.isEmpty()) {
            SectionCard {
                Text("No open concerns.", style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            open.forEach { c ->
                ComplaintCard(c, usersById[c.reporterId], onUpdate)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        if (resolved.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text("Resolved (${resolved.size})", style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            resolved.forEach { c ->
                ComplaintCard(c, usersById[c.reporterId], onUpdate)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun ComplaintCard(
    complaint: Complaint,
    reporter: User?,
    onUpdate: (Complaint, ComplaintStatus, String) -> Unit
) {
    var note by remember(complaint.id) { mutableStateOf(complaint.adminNote) }
    var expanded by remember(complaint.id) { mutableStateOf(false) }

    SectionCard {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(complaint.category, style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold)
                    val who = reporter?.firstName?.takeIf { it.isNotBlank() }
                        ?: complaint.reporterName.takeIf { it.isNotBlank() }
                        ?: "Unknown"
                    val role = complaint.reporterType.name.lowercase()
                        .replaceFirstChar { it.uppercase() }
                    Text(
                        "$who ($role)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                ComplaintStatusChip(complaint.status)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(complaint.description, style = MaterialTheme.typography.bodyMedium)

            if (complaint.adminNote.isNotBlank() && !expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Note: ${complaint.adminNote}", style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(modifier = Modifier.height(12.dp))
            if (!expanded) {
                OutlinedButton(
                    onClick = { expanded = true },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Respond") }
            } else {
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Note to the reporter") },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(
                        onClick = {
                            onUpdate(complaint, ComplaintStatus.IN_REVIEW, note.trim())
                            expanded = false
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                    ) { Text("In review") }
                    Button(
                        onClick = {
                            onUpdate(complaint, ComplaintStatus.RESOLVED, note.trim())
                            expanded = false
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                    ) { Text("Resolve") }
                }
            }
        }
    }
}

@Composable
private fun ComplaintStatusChip(status: ComplaintStatus) {
    val (color, label) = when (status) {
        ComplaintStatus.OPEN -> WarningColor to "Open"
        ComplaintStatus.IN_REVIEW -> SuccessColor to "In review"
        ComplaintStatus.RESOLVED -> SuccessColor to "Resolved"
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
private fun VerificationContent(
    drivers: List<Driver>,
    usersById: Map<String, User>,
    licenceImages: Map<String, String?>,
    onOpenLicence: (String) -> Unit,
    onCloseLicence: (String) -> Unit,
    onApprove: (String) -> Unit,
    onReject: (String) -> Unit,
    onRevoke: (String) -> Unit
) {
    val pending = drivers.filter { it.verificationStatus == VerificationStatus.PENDING }
    val others = drivers.filter { it.verificationStatus != VerificationStatus.PENDING }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text("Driver Verification", style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Review and approve registered tricycle drivers.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(20.dp))

        Text("Pending (${pending.size})", style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        if (pending.isEmpty()) {
            SectionCard {
                Text("No drivers waiting for verification.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            pending.forEach { driver ->
                DriverCard(
                    driver = driver,
                    user = usersById[driver.userId],
                    licence = licenceImages[driver.userId],
                    licenceOpen = licenceImages.containsKey(driver.userId),
                    onOpenLicence = { onOpenLicence(driver.userId) },
                    onCloseLicence = { onCloseLicence(driver.userId) },
                    actions = {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedButton(
                                onClick = { onReject(driver.userId) },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorColor),
                                modifier = Modifier.weight(1f)
                            ) { Text("Reject") }
                            Button(
                                onClick = { onApprove(driver.userId) },
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.weight(1f)
                            ) { Text("Approve") }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("All Drivers (${drivers.size})", style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        if (others.isEmpty() && pending.isEmpty()) {
            SectionCard {
                Text("No registered drivers yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            others.forEach { driver ->
                DriverCard(
                    driver = driver,
                    user = usersById[driver.userId],
                    licence = licenceImages[driver.userId],
                    licenceOpen = licenceImages.containsKey(driver.userId),
                    onOpenLicence = { onOpenLicence(driver.userId) },
                    onCloseLicence = { onCloseLicence(driver.userId) },
                    actions = {
                        if (driver.verificationStatus == VerificationStatus.APPROVED) {
                            OutlinedButton(
                                onClick = { onRevoke(driver.userId) },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorColor),
                                modifier = Modifier.fillMaxWidth()
                            ) { Text("Revoke Approval") }
                        } else {
                            Button(
                                onClick = { onApprove(driver.userId) },
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) { Text("Approve") }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun DriverCard(
    driver: Driver,
    user: User?,
    licence: String?,
    licenceOpen: Boolean,
    onOpenLicence: () -> Unit,
    onCloseLicence: () -> Unit,
    actions: @Composable () -> Unit
) {
    SectionCard {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Person, contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        user?.firstName?.ifBlank { "Driver" } ?: "Driver",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (user?.phoneNumber?.isNotBlank() == true) {
                        Text(user.phoneNumber, style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                StatusChip(driver.verificationStatus)
            }
            Spacer(modifier = Modifier.height(12.dp))
            InfoLine("License", driver.licenseNumber.ifBlank { "—" })
            InfoLine("License Expiry", driver.licenseExpiry.ifBlank { "—" })
            InfoLine("Tricycle No.", driver.tricycleNumber.ifBlank { "—" })
            Spacer(modifier = Modifier.height(12.dp))
            LicenceReview(
                hasImage = driver.hasLicenceImage,
                image = licence,
                isOpen = licenceOpen,
                onOpen = onOpenLicence,
                onClose = onCloseLicence
            )
            Spacer(modifier = Modifier.height(12.dp))
            actions()
        }
    }
}

/**
 * The licence photograph, shown only once the administrator asks for it.
 *
 * Collapsed by default. These are identity documents, and putting a dozen of
 * them on screen at once — over the shoulder of whoever is sitting nearby —
 * is not something a verification queue needs to do. Opening one is a
 * deliberate act, and it is also what triggers the fetch.
 */
@Composable
private fun LicenceReview(
    hasImage: Boolean,
    image: String?,
    isOpen: Boolean,
    onOpen: () -> Unit,
    onClose: () -> Unit
) {
    if (!hasImage) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Filled.WarningAmber,
                contentDescription = null,
                tint = WarningColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "No licence photo submitted yet.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    if (!isOpen) {
        OutlinedButton(
            onClick = onOpen,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.Badge, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("View licence photo")
        }
        return
    }

    val bitmap = remember(image) { LicenceImage.decode(image) }
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            when {
                bitmap != null -> Image(
                    bitmap = bitmap,
                    contentDescription = "Licence photo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
                image == null -> CircularProgressIndicator(modifier = Modifier.size(24.dp))
                else -> Text(
                    "That photo could not be opened.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            "Check the number, name and expiry against the details above.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        TextButton(onClick = onClose) { Text("Hide photo") }
    }
}

@Composable
private fun InfoLine(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun StatusChip(status: VerificationStatus) {
    val (color, label) = when (status) {
        VerificationStatus.APPROVED -> SuccessColor to "Approved"
        VerificationStatus.PENDING -> WarningColor to "Pending"
        VerificationStatus.REJECTED -> ErrorColor to "Rejected"
        VerificationStatus.EXPIRED -> ErrorColor to "Expired"
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
private fun MonitorContent(
    drivers: List<Driver>,
    rides: List<Ride>,
    complaints: List<Complaint>,
    usersById: Map<String, User>
) {
    var showReports by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = if (showReports) 1 else 0) {
            Tab(
                selected = !showReports,
                onClick = { showReports = false },
                text = { Text("Live") }
            )
            Tab(
                selected = showReports,
                onClick = { showReports = true },
                text = { Text("Reports") }
            )
        }
        if (showReports) {
            AdminReportsContent(
                rides = rides,
                drivers = drivers,
                complaints = complaints,
                usersById = usersById
            )
        } else {
            LiveMonitorContent(drivers = drivers, rides = rides)
        }
    }
}

@Composable
private fun LiveMonitorContent(drivers: List<Driver>, rides: List<Ride>) {
    val approved = drivers.count { it.verificationStatus == VerificationStatus.APPROVED }
    val pending = drivers.count { it.verificationStatus == VerificationStatus.PENDING }
    val activeRides = rides.count {
        it.status != RideStatus.COMPLETED && it.status != RideStatus.CANCELLED &&
            it.status != RideStatus.NO_SHOW
    }
    val completed = rides.count { it.status == RideStatus.COMPLETED }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text("System Monitor", style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatTile("Drivers", "${drivers.size}", Icons.Filled.Groups, Modifier.weight(1f))
            StatTile("Approved", "$approved", Icons.Filled.CheckCircle, Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatTile("Pending", "$pending", Icons.Filled.VerifiedUser, Modifier.weight(1f))
            StatTile("Active Rides", "$activeRides", Icons.Filled.Insights, Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(20.dp))

        Text("Recent Rides ($completed completed)", style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        if (rides.isEmpty()) {
            SectionCard {
                Text("No rides recorded yet.", style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            rides.sortedByDescending { it.requestedAt }.take(15).forEach { ride ->
                SectionCard {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${ride.pickupLocation.address} → ${ride.dropoffLocation.address}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(ride.status.name, style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary)
                            Text("₱%.2f".format(ride.estimatedFare),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun StatTile(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
    SectionCard(modifier = modifier) {
        Column {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/**
 * The fare table, as posted by FeTODAT.
 *
 * There are 240 stops, so the list is lazy and the admin filters down to what
 * they want rather than scrolling: type part of a stop name, narrow to a zone,
 * or switch to the flagged rows, which are the ones that came out of the
 * transcription with a problem worth checking against the physical sheet.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FareConfigContent(
    config: FareConfig,
    stops: List<FareStop>,
    saved: Boolean,
    importing: Boolean,
    onSaveConfig: (FareConfig) -> Unit,
    onSaveStop: (FareStop) -> Unit,
    onDeleteStop: (String) -> Unit,
    onImport: () -> Unit,
    onAcknowledgeSaved: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    var zoneFilter by remember { mutableStateOf<String?>(null) }
    var flaggedOnly by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf<FareStop?>(null) }
    var addingNew by remember { mutableStateOf(false) }
    var confirmImport by remember { mutableStateOf(false) }
    var showGlobals by remember { mutableStateOf(false) }

    LaunchedEffect(saved) {
        if (saved) {
            delay(1500)
            onAcknowledgeSaved()
        }
    }

    val zones = remember(stops) { stops.map { it.zone }.distinct().sorted() }
    val flaggedCount = remember(stops) { stops.count { it.needsReview } }

    val visible = remember(stops, query, zoneFilter, flaggedOnly) {
        val q = query.trim().lowercase()
        stops.asSequence()
            .filter { zoneFilter == null || it.zone == zoneFilter }
            .filter { !flaggedOnly || it.needsReview }
            .filter {
                q.isEmpty() || it.name.lowercase().contains(q) || it.zone.lowercase().contains(q)
            }
            .sortedWith(compareBy<FareStop>({ it.zone }, { it.name }))
            .toList()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Fares", style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold)
                    Text(
                        if (stops.isEmpty()) "No rate table loaded yet."
                        else "${stops.size} stops across ${zones.size} zones",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = { addingNew = true }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add a stop")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Search stops") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = zoneFilter == null && !flaggedOnly,
                    onClick = { zoneFilter = null; flaggedOnly = false },
                    label = { Text("All") }
                )
                if (flaggedCount > 0) {
                    FilterChip(
                        selected = flaggedOnly,
                        onClick = { flaggedOnly = !flaggedOnly },
                        label = { Text("Needs review ($flaggedCount)") },
                        leadingIcon = {
                            Icon(Icons.Filled.WarningAmber, contentDescription = null,
                                modifier = Modifier.size(16.dp))
                        }
                    )
                }
                zones.forEach { zone ->
                    FilterChip(
                        selected = zoneFilter == zone,
                        onClick = { zoneFilter = if (zoneFilter == zone) null else zone },
                        label = { Text(zone) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))

            TextButton(onClick = { showGlobals = true }) {
                Text("Minimums and flat rates")
            }

            if (saved) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Saved.", color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        if (stops.isEmpty()) {
            EmptyFareTable(importing = importing, onImport = { confirmImport = true })
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (visible.isEmpty()) {
                    item {
                        Text(
                            "Nothing matches that.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                items(visible, key = { it.id }) { stop ->
                    FareStopRow(stop = stop, onClick = { editing = stop })
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { confirmImport = true },
                        enabled = !importing,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (importing) "Loading the official table..."
                            else "Reload the official FeTODAT table"
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        config.source,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    editing?.let { stop ->
        FareStopDialog(
            stop = stop,
            isNew = false,
            onDismiss = { editing = null },
            onSave = { updated -> onSaveStop(updated); editing = null },
            onDelete = { onDeleteStop(stop.id); editing = null }
        )
    }

    if (addingNew) {
        FareStopDialog(
            stop = FareStop(zone = zoneFilter ?: zones.firstOrNull().orEmpty()),
            isNew = true,
            onDismiss = { addingNew = false },
            onSave = { created -> onSaveStop(created); addingNew = false },
            onDelete = null
        )
    }

    if (showGlobals) {
        GlobalRatesDialog(
            config = config,
            onDismiss = { showGlobals = false },
            onSave = { updated -> onSaveConfig(updated); showGlobals = false }
        )
    }

    if (confirmImport) {
        AlertDialog(
            onDismissRequest = { confirmImport = false },
            title = { Text("Load the official table?") },
            text = {
                Text(
                    "This writes all ${FareSeed.STOPS.size} stops from the posted FeTODAT " +
                        "sheet into the database. Any stop with the same name is overwritten, " +
                        "so hand-made corrections to those rows are lost. Stops you added " +
                        "yourself are left alone."
                )
            },
            confirmButton = {
                TextButton(onClick = { confirmImport = false; onImport() }) { Text("Load") }
            },
            dismissButton = {
                TextButton(onClick = { confirmImport = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun EmptyFareTable(importing: Boolean, onImport: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Filled.Payments,
            contentDescription = null,
            modifier = Modifier.size(56.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("No fares loaded", style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Load the ${FareSeed.STOPS.size} stops transcribed from the posted FeTODAT " +
                "sheet, then correct anything that reads wrong. Rides cannot be priced " +
                "until this is done.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        PrimaryButton(
            text = if (importing) "Loading..." else "Load official rates",
            onClick = onImport,
            enabled = !importing
        )
    }
}

@Composable
private fun FareStopRow(stop: FareStop, onClick: () -> Unit) {
    SectionCard(modifier = Modifier.clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        stop.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (stop.needsReview) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            Icons.Filled.WarningAmber,
                            contentDescription = "Needs review",
                            tint = WarningColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Text(
                    if (stop.active) stop.zone else "${stop.zone} — inactive",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (stop.active) MaterialTheme.colorScheme.onSurfaceVariant
                    else ErrorColor
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "₱%.2f".format(stop.regularFare),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "₱%.2f discounted".format(stop.discountedFare),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun FareStopDialog(
    stop: FareStop,
    isNew: Boolean,
    onDismiss: () -> Unit,
    onSave: (FareStop) -> Unit,
    onDelete: (() -> Unit)?
) {
    var name by remember { mutableStateOf(stop.name) }
    var zone by remember { mutableStateOf(stop.zone) }
    var regular by remember { mutableStateOf(if (stop.regularFare == 0.0) "" else "%.2f".format(stop.regularFare)) }
    var discounted by remember { mutableStateOf(if (stop.discountedFare == 0.0) "" else "%.2f".format(stop.discountedFare)) }
    var active by remember { mutableStateOf(stop.active) }
    var reviewed by remember { mutableStateOf(!stop.needsReview) }
    var lat by remember { mutableStateOf(if (stop.latitude == 0.0) "" else stop.latitude.toString()) }
    var lng by remember { mutableStateOf(if (stop.longitude == 0.0) "" else stop.longitude.toString()) }

    val regularValue = regular.toDoubleOrNull()
    val discountedValue = discounted.toDoubleOrNull()
    val canSave = name.isNotBlank() && zone.isNotBlank() &&
        regularValue != null && discountedValue != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isNew) "Add a stop" else stop.name) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                if (stop.note.isNotBlank()) {
                    SectionCard {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                Icons.Filled.WarningAmber,
                                contentDescription = null,
                                tint = WarningColor,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stop.note, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Stop name") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = zone,
                    onValueChange = { zone = it },
                    label = { Text("Zone") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                MoneyField("Regular fare (₱)", regular) { regular = it }
                Spacer(modifier = Modifier.height(10.dp))
                MoneyField("Senior / PWD / Student (₱)", discounted) { discounted = it }

                if (regularValue != null && discountedValue != null && discountedValue > regularValue) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "The discounted rate is higher than the regular one.",
                        style = MaterialTheme.typography.bodySmall,
                        color = WarningColor
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Map position (optional)",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Leave blank if unknown. A stop without a position still " +
                        "prices and books normally; it just does not show on the map.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        MoneyField("Latitude", lat) { lat = it }
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        MoneyField("Longitude", lng) { lng = it }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Bookable", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            "Passengers can pick this stop",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(checked = active, onCheckedChange = { active = it })
                }

                if (stop.needsReview) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Checked against the sheet",
                                style = MaterialTheme.typography.bodyMedium)
                            Text(
                                "Clears the review flag",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(checked = reviewed, onCheckedChange = { reviewed = it })
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = canSave,
                onClick = {
                    val id = stop.id.ifBlank {
                        (zone + "__" + name).lowercase()
                            .replace(Regex("[^a-z0-9]+"), "_")
                            .trim('_')
                    }
                    onSave(
                        stop.copy(
                            id = id,
                            name = name.trim(),
                            zone = zone.trim(),
                            regularFare = regularValue ?: 0.0,
                            discountedFare = discountedValue ?: 0.0,
                            active = active,
                            needsReview = stop.needsReview && !reviewed,
                            latitude = lat.toDoubleOrNull() ?: 0.0,
                            longitude = lng.toDoubleOrNull() ?: 0.0
                        )
                    )
                }
            ) { Text("Save") }
        },
        dismissButton = {
            Row {
                if (onDelete != null) {
                    TextButton(onClick = onDelete) { Text("Delete", color = ErrorColor) }
                }
                TextButton(onClick = onDismiss) { Text("Cancel") }
            }
        }
    )
}

/** Minimum fares and the two flat rates that are not tied to a stop. */
@Composable
private fun GlobalRatesDialog(
    config: FareConfig,
    onDismiss: () -> Unit,
    onSave: (FareConfig) -> Unit
) {
    var minRegular by remember(config) { mutableStateOf("%.2f".format(config.minimumRegular)) }
    var minDiscounted by remember(config) { mutableStateOf("%.2f".format(config.minimumDiscounted)) }
    var poblacion by remember(config) { mutableStateOf("%.2f".format(config.poblacionFlat)) }
    var terminal by remember(config) { mutableStateOf("%.2f".format(config.terminalRoundTrip)) }
    var perHead by remember(config) { mutableStateOf(config.chargePerPassenger) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Minimums and flat rates") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(
                    "A stop can never price below the minimum for its rate column.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                MoneyField("Minimum regular fare (₱)", minRegular) { minRegular = it }
                Spacer(modifier = Modifier.height(10.dp))
                MoneyField("Minimum senior / PWD / student (₱)", minDiscounted) { minDiscounted = it }
                Spacer(modifier = Modifier.height(10.dp))
                MoneyField("${FareConfig.POBLACION_LABEL} (₱)", poblacion) { poblacion = it }
                Spacer(modifier = Modifier.height(10.dp))
                MoneyField("${FareConfig.TERMINAL_ROUND_TRIP_LABEL} (₱)", terminal) { terminal = it }
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Charge per passenger", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            "Off means one fare covers the whole tricycle",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(checked = perHead, onCheckedChange = { perHead = it })
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSave(
                    config.copy(
                        minimumRegular = minRegular.toDoubleOrNull() ?: config.minimumRegular,
                        minimumDiscounted = minDiscounted.toDoubleOrNull()
                            ?: config.minimumDiscounted,
                        poblacionFlat = poblacion.toDoubleOrNull() ?: config.poblacionFlat,
                        terminalRoundTrip = terminal.toDoubleOrNull() ?: config.terminalRoundTrip,
                        chargePerPassenger = perHead
                    )
                )
            }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun MoneyField(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    )
}
