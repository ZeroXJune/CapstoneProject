package com.tpc.trikride.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tpc.trikride.models.Driver
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.RideRequest
import com.tpc.trikride.models.RideStatus
import com.tpc.trikride.models.VerificationStatus
import com.tpc.trikride.ui.components.PrimaryButton
import com.tpc.trikride.ui.components.SectionCard
import com.tpc.trikride.ui.components.SettingsCard
import com.tpc.trikride.ui.components.SimplePlaceholder
import com.tpc.trikride.ui.components.TrikTextField
import com.tpc.trikride.ui.theme.ErrorColor
import com.tpc.trikride.ui.theme.RatingColor
import com.tpc.trikride.utils.LocationUtils
import kotlinx.coroutines.delay
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DateRange
import com.tpc.trikride.viewmodels.DriverViewModel

private enum class DriverTab { DASHBOARD, REQUESTS, HISTORY, PROFILE }

@Composable
fun DriverHomeScreen(
    userId: String,
    onSignOut: () -> Unit,
    viewModel: DriverViewModel = viewModel()
) {
    LaunchedEffect(userId) { viewModel.bind(userId) }

    val driver by viewModel.driverProfile.collectAsState()
    val openRequests by viewModel.openRequests.collectAsState()
    val activeRides by viewModel.activeRides.collectAsState()
    val isRegistering by viewModel.isRegistering.collectAsState()
    val error by viewModel.errorMessage.collectAsState()

    val profile = driver
    if (profile == null) {
        DriverOnboardingContent(
            isSubmitting = isRegistering,
            error = error,
            onDismissError = viewModel::dismissError,
            onSubmit = viewModel::registerDriver
        )
        return
    }

    var tab by remember { mutableStateOf(DriverTab.DASHBOARD) }

    Scaffold(
        bottomBar = { DriverBottomBar(selected = tab, onSelect = { tab = it }, requestCount = openRequests.size) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (tab) {
                DriverTab.DASHBOARD -> {
                    val active = activeRides.firstOrNull()
                    if (active != null) {
                        ActiveRideContent(ride = active, onAdvance = { viewModel.advanceRide(active) })
                    } else {
                        DriverDashboard(
                            driver = profile,
                            activeCount = activeRides.size,
                            onToggleOnline = viewModel::setAvailability
                        )
                    }
                }
                DriverTab.REQUESTS -> RequestsContent(
                    isOnline = profile.isAvailable,
                    requests = openRequests,
                    onAccept = viewModel::acceptRequest
                )
                DriverTab.HISTORY -> SimplePlaceholder(
                    icon = Icons.Filled.History,
                    title = "Ride History",
                    message = "Your completed trips and earnings will appear here."
                )
                DriverTab.PROFILE -> DriverProfileContent(driver = profile, onSignOut = onSignOut)
            }
        }
    }
}

@Composable
private fun DriverBottomBar(
    selected: DriverTab,
    onSelect: (DriverTab) -> Unit,
    requestCount: Int
) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        NavigationBarItem(
            selected = selected == DriverTab.DASHBOARD,
            onClick = { onSelect(DriverTab.DASHBOARD) },
            icon = { Icon(Icons.Filled.Dashboard, contentDescription = "Dashboard") },
            label = { Text("Dashboard") }
        )
        NavigationBarItem(
            selected = selected == DriverTab.REQUESTS,
            onClick = { onSelect(DriverTab.REQUESTS) },
            icon = {
                BadgedBox(badge = {
                    if (requestCount > 0) Badge { Text("$requestCount") }
                }) {
                    Icon(Icons.Filled.Inbox, contentDescription = "Requests")
                }
            },
            label = { Text("Requests") }
        )
        NavigationBarItem(
            selected = selected == DriverTab.HISTORY,
            onClick = { onSelect(DriverTab.HISTORY) },
            icon = { Icon(Icons.Filled.History, contentDescription = "History") },
            label = { Text("History") }
        )
        NavigationBarItem(
            selected = selected == DriverTab.PROFILE,
            onClick = { onSelect(DriverTab.PROFILE) },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}

@Composable
private fun DriverDashboard(
    driver: Driver,
    activeCount: Int,
    onToggleOnline: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text("Driver Dashboard", style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // Profile + rating + online switch
        SectionCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
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
                        if (driver.isAvailable) "Online" else "Offline",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (driver.isAvailable) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Star, contentDescription = null,
                            tint = RatingColor, modifier = Modifier.size(16.dp))
                        Text(
                            " %.1f  •  Tricycle #%s".format(driver.rating, driver.tricycleNumber),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Switch(checked = driver.isAvailable, onCheckedChange = onToggleOnline)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        if (driver.verificationStatus != VerificationStatus.APPROVED) {
            SectionCard {
                Column {
                    Text("Verification: ${driver.verificationStatus}",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary)
                    Text(
                        "Your registration is under review by the administrator. " +
                            "You'll be notified once approved.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Earnings hero
        SectionCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Payments, contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Earnings Today", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("₱0.00", style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Active Rides", "$activeCount", Modifier.weight(1f))
            StatCard("Total Trips", "${driver.totalRides}", Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(20.dp))

        PrimaryButton(
            text = if (driver.isAvailable) "Go Offline" else "Go Online",
            onClick = { onToggleOnline(!driver.isAvailable) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            if (driver.isAvailable) "You are receiving ride requests."
            else "Go online to start receiving ride requests.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    SectionCard(modifier = modifier) {
        Column {
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun RequestsContent(
    isOnline: Boolean,
    requests: List<RideRequest>,
    onAccept: (RideRequest) -> Unit
) {
    val declined = remember { mutableStateListOf<String>() }
    val visible = requests.filter { it.id !in declined }

    // Ticking clock for the countdown timers.
    val nowMs by produceState(initialValue = System.currentTimeMillis()) {
        while (true) {
            value = System.currentTimeMillis()
            delay(1000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("Incoming Requests", style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        when {
            !isOnline -> SectionCard {
                Text("You are offline. Go online from the Dashboard to receive requests.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            visible.isEmpty() -> SectionCard {
                Text("No ride requests right now. New requests will appear here.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            else -> visible.forEach { request ->
                val expiresAt = request.expiresAt.toLongOrNull() ?: nowMs
                val remaining = ((expiresAt - nowMs) / 1000).coerceAtLeast(0)
                RequestCard(
                    request = request,
                    remainingSeconds = remaining,
                    onAccept = { onAccept(request) },
                    onDecline = { declined.add(request.id) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun RequestCard(
    request: RideRequest,
    remainingSeconds: Long,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    val distanceKm = LocationUtils.distanceKm(request.pickupLocation, request.dropoffLocation)
    val fare = request.estimatedFare

    SectionCard {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("New ride request", style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold)
                Text(
                    "${remainingSeconds}s",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (remainingSeconds <= 10) ErrorColor else MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            RouteRowDriver(Icons.Filled.MyLocation, "Pickup", request.pickupLocation.address)
            Spacer(modifier = Modifier.height(8.dp))
            RouteRowDriver(Icons.Filled.LocationOn, "Destination", request.dropoffLocation.address)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Estimated Fare", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("₱%.2f".format(fare), style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Distance", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("%.1f km".format(distanceKm), style = MaterialTheme.typography.titleMedium)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("👥 ${request.passengerCount} passenger(s)",
                    style = MaterialTheme.typography.bodySmall)
                Text("🧳 ${request.luggage}", style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (request.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Notes: ${request.notes}", style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onDecline,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorColor),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) { Text("Decline") }
                Button(
                    onClick = onAccept,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) { Text("Accept") }
            }
        }
    }
}

@Composable
private fun ActiveRideContent(ride: Ride, onAdvance: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("Active Ride", style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Status: ${statusLabel(ride.status)}", style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))

        // Passenger card
        SectionCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Person, contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Passenger", style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold)
                    Text("Fare ₱%.2f  •  %d min".format(ride.estimatedFare, ride.estimatedDuration),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        SectionCard {
            Column {
                RouteRowDriver(Icons.Filled.MyLocation, "Pickup", ride.pickupLocation.address)
                Spacer(modifier = Modifier.height(10.dp))
                RouteRowDriver(Icons.Filled.LocationOn, "Destination", ride.dropoffLocation.address)
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("👥 ${ride.passengerCount} passenger(s)", style = MaterialTheme.typography.bodyMedium)
                    Text("🧳 ${ride.luggage}", style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                if (ride.notes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Passenger notes: ${ride.notes}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        val actionLabel = when (ride.status) {
            RideStatus.ACCEPTED -> "Head to Pickup"
            RideStatus.DRIVER_ARRIVING -> "Arrived at Pickup"
            RideStatus.DRIVER_ARRIVED -> "Start Ride"
            RideStatus.IN_PROGRESS -> "Complete Ride"
            else -> null
        }
        if (actionLabel != null) {
            PrimaryButton(text = actionLabel, onClick = onAdvance)
        }
    }
}

@Composable
private fun DriverProfileContent(driver: Driver, onSignOut: () -> Unit) {
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
            Icon(Icons.Filled.Person, contentDescription = null,
                tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(44.dp))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text("Registered Driver", style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Star, contentDescription = null, tint = RatingColor,
                modifier = Modifier.size(18.dp))
            Text(" %.1f  •  ${driver.totalRides} trips".format(driver.rating),
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(modifier = Modifier.height(20.dp))

        SectionCard {
            Column {
                InfoRow(Icons.Filled.Badge, "License Number", driver.licenseNumber)
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                InfoRow(Icons.Filled.DateRange, "License Expiry", driver.licenseExpiry)
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                InfoRow(Icons.Filled.CreditCard, "Tricycle Number", driver.tricycleNumber)
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                InfoRow(Icons.Filled.CheckCircle, "Verification", driver.verificationStatus.name)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        SettingsCard(onSignOut = onSignOut)
    }
}

@Composable
private fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value.ifBlank { "—" }, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun RouteRowDriver(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun DriverOnboardingContent(
    isSubmitting: Boolean,
    error: String?,
    onDismissError: () -> Unit,
    onSubmit: (String, String, String) -> Unit
) {
    var licenseNumber by remember { mutableStateOf("") }
    var licenseExpiry by remember { mutableStateOf("") }
    var tricycleNumber by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("Driver Registration", style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "Register your details below. An administrator will verify your " +
                "credentials before you can accept passengers.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))

        error?.let {
            SectionCard {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.weight(1f))
                    TextButton(onClick = onDismissError) { Text("Dismiss") }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        TrikTextField(licenseNumber, { licenseNumber = it }, "Driver's License Number", Icons.Filled.Badge)
        Spacer(modifier = Modifier.height(14.dp))
        TrikTextField(licenseExpiry, { licenseExpiry = it }, "License Expiry (MM/YYYY)", Icons.Filled.DateRange)
        Spacer(modifier = Modifier.height(14.dp))
        TrikTextField(tricycleNumber, { tricycleNumber = it }, "Tricycle Body / Plate Number", Icons.Filled.CreditCard)
        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = if (isSubmitting) "Submitting..." else "Submit for Verification",
            onClick = { onSubmit(licenseNumber.trim(), licenseExpiry.trim(), tricycleNumber.trim()) },
            enabled = !isSubmitting && licenseNumber.isNotBlank() &&
                licenseExpiry.isNotBlank() && tricycleNumber.isNotBlank()
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

private fun statusLabel(status: RideStatus): String = when (status) {
    RideStatus.ACCEPTED -> "Accepted"
    RideStatus.DRIVER_ARRIVING -> "Heading to pickup"
    RideStatus.DRIVER_ARRIVED -> "Arrived at pickup"
    RideStatus.IN_PROGRESS -> "Ride in progress"
    RideStatus.COMPLETED -> "Completed"
    else -> status.name
}
