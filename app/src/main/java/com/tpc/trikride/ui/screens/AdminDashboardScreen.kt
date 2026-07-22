package com.tpc.trikride.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tpc.trikride.models.Driver
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.RideStatus
import com.tpc.trikride.models.User
import com.tpc.trikride.models.VerificationStatus
import com.tpc.trikride.ui.components.SectionCard
import com.tpc.trikride.ui.components.SettingsCard
import com.tpc.trikride.ui.theme.ErrorColor
import com.tpc.trikride.ui.theme.SuccessColor
import com.tpc.trikride.ui.theme.WarningColor
import com.tpc.trikride.viewmodels.AdminViewModel

private enum class AdminTab { VERIFY, MONITOR, PROFILE }

@Composable
fun AdminDashboardScreen(
    onSignOut: () -> Unit,
    viewModel: AdminViewModel = viewModel()
) {
    val drivers by viewModel.drivers.collectAsState()
    val users by viewModel.users.collectAsState()
    val rides by viewModel.rides.collectAsState()

    var tab by remember { mutableStateOf(AdminTab.VERIFY) }
    val usersById = remember(users) { users.associateBy { it.id } }
    val pendingCount = drivers.count { it.verificationStatus == VerificationStatus.PENDING }

    Scaffold(
        bottomBar = { AdminBottomBar(selected = tab, onSelect = { tab = it }, pending = pendingCount) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (tab) {
                AdminTab.VERIFY -> VerificationContent(
                    drivers = drivers,
                    usersById = usersById,
                    onApprove = viewModel::approveDriver,
                    onReject = viewModel::rejectDriver
                )
                AdminTab.MONITOR -> MonitorContent(drivers = drivers, rides = rides)
                AdminTab.PROFILE -> AdminProfileContent(onSignOut = onSignOut)
            }
        }
    }
}

@Composable
private fun AdminBottomBar(selected: AdminTab, onSelect: (AdminTab) -> Unit, pending: Int) {
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
            selected = selected == AdminTab.MONITOR,
            onClick = { onSelect(AdminTab.MONITOR) },
            icon = { Icon(Icons.Filled.Insights, contentDescription = "Monitor") },
            label = { Text("Monitor") }
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

@Composable
private fun AdminProfileContent(onSignOut: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.VerifiedUser, contentDescription = null,
                tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(44.dp))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text("Administrator", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("Talibon Polytechnic College", style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(20.dp))
        SettingsCard(onSignOut = onSignOut)
    }
}
