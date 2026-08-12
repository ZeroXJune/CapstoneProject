package com.tpc.trikride.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tpc.trikride.models.Complaint
import com.tpc.trikride.models.ComplaintStatus
import com.tpc.trikride.models.Driver
import com.tpc.trikride.models.FareConfig
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.RideStatus
import com.tpc.trikride.models.RouteFare
import com.tpc.trikride.models.User
import com.tpc.trikride.models.VerificationStatus
import com.tpc.trikride.ui.components.PrimaryButton
import com.tpc.trikride.ui.components.SectionCard
import com.tpc.trikride.utils.Constants
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
    val fareSaved by viewModel.fareSaved.collectAsState()
    val complaints by viewModel.complaints.collectAsState()

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
                    onApprove = viewModel::approveDriver,
                    onReject = viewModel::rejectDriver
                )
                AdminTab.CONCERNS -> ConcernsContent(
                    complaints = complaints,
                    usersById = usersById,
                    onUpdate = viewModel::updateComplaint
                )
                AdminTab.MONITOR -> MonitorContent(drivers = drivers, rides = rides)
                AdminTab.FARES -> FareConfigContent(
                    config = fareConfig,
                    saved = fareSaved,
                    onSave = viewModel::saveFareConfig,
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
    onApprove: (String) -> Unit,
    onReject: (String) -> Unit
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
                    actions = {
                        if (driver.verificationStatus == VerificationStatus.APPROVED) {
                            OutlinedButton(
                                onClick = { onReject(driver.userId) },
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
private fun DriverCard(driver: Driver, user: User?, actions: @Composable () -> Unit) {
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
            actions()
        }
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
private fun MonitorContent(drivers: List<Driver>, rides: List<Ride>) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FareConfigContent(
    config: FareConfig,
    saved: Boolean,
    onSave: (FareConfig) -> Unit,
    onAcknowledgeSaved: () -> Unit
) {
    var base by remember(config) { mutableStateOf(config.baseFare.toString()) }
    var perKm by remember(config) { mutableStateOf(config.perKmRate.toString()) }
    var perExtra by remember(config) { mutableStateOf(config.perExtraPassenger.toString()) }
    val routes = remember(config) { mutableStateListOf<RouteFare>().apply { addAll(config.routes) } }

    var newPickup by remember { mutableStateOf<String?>(null) }
    var newDest by remember { mutableStateOf<String?>(null) }
    var newFare by remember { mutableStateOf("") }

    LaunchedEffect(saved) {
        if (saved) {
            delay(1500)
            onAcknowledgeSaved()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text("Fare Configuration", style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold)
        Text("Set the official pricing used for every ride.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(16.dp))

        // Global rates
        SectionCard {
            Column {
                Text("Default Rates", style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold)
                Text("Used for routes without a fixed fare below.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(12.dp))
                MoneyField("Base fare (₱)", base) { base = it }
                Spacer(modifier = Modifier.height(10.dp))
                MoneyField("Per kilometre (₱)", perKm) { perKm = it }
                Spacer(modifier = Modifier.height(10.dp))
                MoneyField("Per extra passenger (₱)", perExtra) { perExtra = it }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Fixed route fares
        SectionCard {
            Column {
                Text("Fixed Route Fares", style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold)
                Text("An exact price for a specific pickup → destination.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(12.dp))

                if (routes.isEmpty()) {
                    Text("No fixed route fares yet.", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else {
                    routes.forEachIndexed { index, route ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("${route.pickup} → ${route.destination}",
                                    style = MaterialTheme.typography.bodyMedium)
                                Text("₱%.2f".format(route.fare),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary)
                            }
                            IconButton(onClick = { routes.removeAt(index) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Remove",
                                    tint = ErrorColor)
                            }
                        }
                        HorizontalDivider()
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text("Add a route fare", style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                LocationDropdown("Pickup", newPickup) { newPickup = it }
                Spacer(modifier = Modifier.height(8.dp))
                LocationDropdown("Destination", newDest) { newDest = it }
                Spacer(modifier = Modifier.height(8.dp))
                MoneyField("Fare (₱)", newFare) { newFare = it }
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = {
                        val p = newPickup
                        val d = newDest
                        val f = newFare.toDoubleOrNull()
                        if (p != null && d != null && p != d && f != null) {
                            routes.add(RouteFare(p, d, f))
                            newPickup = null; newDest = null; newFare = ""
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Add Route Fare") }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (saved) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.CheckCircle, contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Fares saved.", color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        PrimaryButton(
            text = "Save Fares",
            onClick = {
                onSave(
                    FareConfig(
                        baseFare = base.toDoubleOrNull() ?: config.baseFare,
                        perKmRate = perKm.toDoubleOrNull() ?: config.perKmRate,
                        perExtraPassenger = perExtra.toDoubleOrNull() ?: config.perExtraPassenger,
                        routes = routes.toList()
                    )
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun MoneyField(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationDropdown(label: String, selected: String?, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = selected ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            Constants.CAMPUS_LOCATIONS.forEach { location ->
                DropdownMenuItem(
                    text = { Text(location.address) },
                    onClick = { onSelected(location.address); expanded = false }
                )
            }
        }
    }
}

