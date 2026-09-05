package com.tpc.trikride.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tpc.trikride.models.Driver
import com.tpc.trikride.models.DriverDocument
import com.tpc.trikride.models.FareType
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.RideRequest
import com.tpc.trikride.models.RideStatus
import com.tpc.trikride.models.VerificationStatus
import com.tpc.trikride.ui.components.LicenceUploadCard
import com.tpc.trikride.ui.components.PrimaryButton
import com.tpc.trikride.ui.components.RefreshableBox
import com.tpc.trikride.ui.components.SectionCard
import com.tpc.trikride.ui.components.SkeletonCard
import com.tpc.trikride.ui.components.SupportPanel
import com.tpc.trikride.ui.components.TrikTextField
import com.tpc.trikride.utils.Navigation
import com.tpc.trikride.ui.theme.ErrorColor
import com.tpc.trikride.ui.theme.RatingColor
import kotlinx.coroutines.delay
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DateRange
import com.tpc.trikride.utils.LocationProvider
import com.tpc.trikride.viewmodels.DriverViewModel
import com.tpc.trikride.viewmodels.SupportViewModel

private enum class DriverTab { DASHBOARD, REQUESTS, HISTORY, SUPPORT, PROFILE }

@Composable
fun DriverHomeScreen(
    userId: String,
    onSignOut: () -> Unit,
    viewModel: DriverViewModel = viewModel(),
    supportViewModel: SupportViewModel = viewModel()
) {
    LaunchedEffect(userId) {
        viewModel.bind(userId)
        supportViewModel.bind(userId)
    }

    val driver by viewModel.driverProfile.collectAsState()
    val openRequests by viewModel.openRequests.collectAsState()
    val activeRides by viewModel.activeRides.collectAsState()
    val isRegistering by viewModel.isRegistering.collectAsState()
    val error by viewModel.errorMessage.collectAsState()
    val earnings by viewModel.earnings.collectAsState()
    val licenceDoc by viewModel.licenceDoc.collectAsState()
    val uploadingLicence by viewModel.uploadingLicence.collectAsState()
    val licenceMessage by viewModel.licenceMessage.collectAsState()

    // Publish position only while online, and only while this screen exists.
    // Going offline or leaving the app stops it; there is no background service.
    val context = LocalContext.current
    val locationPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> if (granted) viewModel.startPublishingLocation(context) }

    LaunchedEffect(driver?.isAvailable) {
        if (driver?.isAvailable == true) {
            if (LocationProvider.hasPermission(context)) {
                viewModel.startPublishingLocation(context)
            } else {
                locationPermission.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        } else {
            viewModel.stopPublishingLocation()
        }
    }

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

    // The number and expiry are on this node too, so it is fetched whether or
    // not a photograph has been sent.
    LaunchedEffect(Unit) { viewModel.loadLicenceDocument() }

    val notifications by supportViewModel.notifications.collectAsState()
    val unreadCount = notifications.count { !it.read }

    val licenceCard: @Composable () -> Unit = {
        LicenceUploadCard(
            status = profile.verificationStatus,
            hasImage = profile.hasLicenceImage,
            imageData = licenceDoc?.image,
            isUploading = uploadingLicence,
            message = licenceMessage,
            onSubmit = { uri, consentedAt ->
                viewModel.submitLicenceImage(context, uri, consentedAt)
            },
            onRemove = viewModel::removeLicenceImage,
            onDismissMessage = viewModel::clearLicenceMessage
        )
    }

    var tab by remember { mutableStateOf(DriverTab.DASHBOARD) }
    var showNotifications by remember { mutableStateOf(false) }

    if (showNotifications) {
        NotificationsScreen(
            userId = userId,
            viewModel = supportViewModel,
            onBack = { showNotifications = false }
        )
        return
    }

    Scaffold(
        bottomBar = { DriverBottomBar(selected = tab, onSelect = { tab = it }, requestCount = openRequests.size) }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            // Anything the database refuses — going online, accepting a ride,
            // advancing one — used to land here and go nowhere: this error was
            // only ever shown on the registration screen, so a driver whose
            // account already existed saw a control that simply did not work.
            error?.let { message ->
                SectionCard {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = viewModel::dismissError) { Text("Dismiss") }
                    }
                }
            }
            Box {
            when (tab) {
                DriverTab.DASHBOARD -> {
                    val active = activeRides.firstOrNull()
                    if (active != null) {
                        ActiveRideContent(ride = active, onAdvance = { viewModel.advanceRide(active) })
                    } else {
                        DriverDashboard(
                            driver = profile,
                            activeCount = activeRides.size,
                            earnings = earnings,
                            unreadCount = unreadCount,
                            onToggleOnline = viewModel::setAvailability,
                            onOpenNotifications = { showNotifications = true },
                            onRefresh = viewModel::refresh,
                            // Only while there is nothing on file. Once it is
                            // sent, the card lives in Profile and the dashboard
                            // goes back to being about driving.
                            licenceCard = if (profile.hasLicenceImage) null else licenceCard
                        )
                    }
                }
                DriverTab.REQUESTS -> RequestsContent(
                    isOnline = profile.isAvailable,
                    requests = openRequests,
                    onAccept = viewModel::acceptRequest
                )
                DriverTab.HISTORY -> DriverHistoryContent(viewModel)
                DriverTab.SUPPORT -> SupportPanel(
                    userId = userId,
                    reporterType = com.tpc.trikride.models.UserType.DRIVER,
                    viewModel = supportViewModel
                )
                DriverTab.PROFILE -> SettingsScreen(
                    userId = userId,
                    userType = com.tpc.trikride.models.UserType.DRIVER,
                    subtitle = "Tricycle #${profile.tricycleNumber}",
                    onSignOut = onSignOut,
                    extraContent = {
                        DriverCredentialsCard(driver = profile, licence = licenceDoc)
                        Spacer(modifier = Modifier.height(12.dp))
                        licenceCard()
                    }
                )
            }
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
            selected = selected == DriverTab.SUPPORT,
            onClick = { onSelect(DriverTab.SUPPORT) },
            icon = { Icon(Icons.Filled.SupportAgent, contentDescription = "Support") },
            label = { Text("Support") }
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
    earnings: Double,
    unreadCount: Int,
    onToggleOnline: (Boolean) -> Unit,
    onOpenNotifications: () -> Unit,
    onRefresh: () -> Unit,
    licenceCard: (@Composable () -> Unit)? = null
) {
  RefreshableBox(isRefreshing = false, onRefresh = onRefresh) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Driver Dashboard", style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            IconButton(onClick = onOpenNotifications) {
                BadgedBox(badge = { if (unreadCount > 0) Badge { Text("$unreadCount") } }) {
                    Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Above everything else while it is outstanding: without it the driver
        // cannot be approved, and without approval nothing else on this screen
        // does anything for them.
        licenceCard?.let {
            it()
            Spacer(modifier = Modifier.height(16.dp))
        }

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
                    // Not today's: `earnings` sums every completed ride this
                    // driver has ever finished, because the history flow it
                    // folds is not filtered by date. Labelled for what it is
                    // rather than left saying something untrue.
                    Text("Total Earned", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("₱%.2f".format(earnings), style = MaterialTheme.typography.headlineSmall,
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
}

@Composable
private fun DriverHistoryContent(viewModel: DriverViewModel) {
    val history by viewModel.rideHistory.collectAsState()
    val loading by viewModel.loadingHistory.collectAsState()
    val earnings by viewModel.earnings.collectAsState()

    RefreshableBox(isRefreshing = false, onRefresh = viewModel::refresh) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Ride History", style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold)
            Text("Completed and cancelled trips",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(16.dp))

            SectionCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Payments, contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Total Earned", style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("₱%.2f".format(earnings),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            when {
                loading -> repeat(3) {
                    SkeletonCard(lines = 2)
                    Spacer(modifier = Modifier.height(10.dp))
                }
                history.isEmpty() -> SectionCard {
                    Text("No trips yet.", style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                else -> history.forEach { ride ->
                    SectionCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "${ride.pickupLocation.address} to ${ride.dropoffLocation.address}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(ride.status.name.replace('_', ' '),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Text("₱%.2f".format(ride.estimatedFare),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
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
                    Text("Rate", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        if (request.regularCount > 0 || request.discountedCount > 0) {
                            listOfNotNull(
                                request.regularCount.takeIf { it > 0 }?.let { "$it regular" },
                                request.discountedCount.takeIf { it > 0 }
                                    ?.let { "$it senior/PWD/student" }
                            ).joinToString(", ")
                        } else if (request.fareType == FareType.REGULAR) "Regular"
                        else "Senior / PWD / Student",
                        style = MaterialTheme.typography.titleMedium
                    )
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
    val context = LocalContext.current
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        ride.passengerName.ifBlank { "Passenger" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Fare ₱%.2f  •  %s".format(ride.estimatedFare, ride.partyLabel),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (ride.passengerPhone.isNotBlank()) {
                    IconButton(onClick = { Navigation.dial(context, ride.passengerPhone) }) {
                        Icon(Icons.Filled.Phone, contentDescription = "Call the passenger",
                            tint = MaterialTheme.colorScheme.primary)
                    }
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
        Spacer(modifier = Modifier.height(16.dp))

        NavigationHandoff(ride)

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
private fun DriverCredentialsCard(driver: Driver, licence: DriverDocument?) {
    Column {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "VEHICLE & CREDENTIALS",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        SectionCard {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Star, contentDescription = null, tint = RatingColor,
                        modifier = Modifier.size(18.dp))
                    Text(" %.1f  •  ${driver.totalRides} trips".format(driver.rating),
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                InfoRow(
                    Icons.Filled.Badge, "Licence Number",
                    licence?.licenceNumber?.ifBlank { null } ?: "—"
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                InfoRow(
                    Icons.Filled.DateRange, "Licence Expiry",
                    licence?.licenceExpiry?.ifBlank { null } ?: "—"
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                InfoRow(Icons.Filled.CreditCard, "Tricycle Number", driver.tricycleNumber)
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                InfoRow(Icons.Filled.CheckCircle, "Verification", driver.verificationStatus.name)
            }
        }
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

/**
 * Opens the driver's own navigation app at whichever end of the ride is next.
 *
 * Before the passenger is aboard that is the pickup; afterwards it is the
 * destination. Nothing is drawn when the relevant point has no coordinates,
 * which is the case for any fare stop the administrator has not positioned, and
 * nothing is drawn when neither app is installed.
 */
@Composable
private fun NavigationHandoff(ride: Ride) {
    val context = LocalContext.current
    val heading = if (ride.status == RideStatus.IN_PROGRESS) {
        ride.dropoffLocation
    } else {
        ride.pickupLocation
    }
    if (!heading.hasCoordinates) return

    val hasWaze = remember { Navigation.isInstalled(context, Navigation.WAZE) }
    val hasMaps = remember { Navigation.isInstalled(context, Navigation.GOOGLE_MAPS) }
    if (!hasWaze && !hasMaps) return

    val label = if (ride.status == RideStatus.IN_PROGRESS) "destination" else "pickup"

    SectionCard {
        Column {
            Text(
                "Navigate to the $label",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                heading.address.ifBlank { "Pinned point" },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                if (hasWaze) {
                    OutlinedButton(
                        onClick = { Navigation.openWaze(context, heading) },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                    ) { Text("Waze") }
                }
                if (hasMaps) {
                    OutlinedButton(
                        onClick = { Navigation.openGoogleMaps(context, heading) },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                    ) { Text("Google Maps") }
                }
            }
        }
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
