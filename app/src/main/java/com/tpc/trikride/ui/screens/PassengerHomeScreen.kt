package com.tpc.trikride.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tpc.trikride.models.FareConfig
import com.tpc.trikride.models.Location
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.RideStatus
import com.tpc.trikride.ui.components.PrimaryButton
import com.tpc.trikride.ui.components.SecondaryButton
import com.tpc.trikride.ui.components.SectionCard
import com.tpc.trikride.ui.components.SettingsCard
import com.tpc.trikride.ui.components.SimplePlaceholder
import com.tpc.trikride.ui.theme.EmeraldGreen
import com.tpc.trikride.ui.theme.ForestGreen
import com.tpc.trikride.ui.theme.RatingColor
import com.tpc.trikride.utils.Constants
import com.tpc.trikride.utils.FareEngine
import com.tpc.trikride.utils.LocationUtils
import com.tpc.trikride.viewmodels.PassengerViewModel

private enum class PassengerTab { HOME, HISTORY, SUPPORT, PROFILE }

@Composable
fun PassengerHomeScreen(
    userId: String,
    onSignOut: () -> Unit,
    viewModel: PassengerViewModel = viewModel()
) {
    LaunchedEffect(userId) { viewModel.bind(userId) }

    val activeRides by viewModel.activeRides.collectAsState()
    val pendingRequest by viewModel.pendingRequest.collectAsState()
    val error by viewModel.errorMessage.collectAsState()
    val fareConfig by viewModel.fareConfig.collectAsState()

    var tab by remember { mutableStateOf(PassengerTab.HOME) }
    var showBooking by remember { mutableStateOf(false) }
    var completedRide by remember { mutableStateOf<Ride?>(null) }
    var lastActive by remember { mutableStateOf<Ride?>(null) }

    LaunchedEffect(activeRides) {
        val current = activeRides.firstOrNull()
        if (current != null) {
            lastActive = current
        } else if (lastActive != null && completedRide == null) {
            completedRide = lastActive
        }
        viewModel.clearPendingRequestIfMatched()
    }

    Scaffold(
        bottomBar = { PassengerBottomBar(selected = tab, onSelect = { tab = it }) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (tab) {
                PassengerTab.HOME -> {
                    val active = activeRides.firstOrNull()
                    when {
                        completedRide != null -> RideCompleteContent(
                            ride = completedRide!!,
                            onBackHome = { completedRide = null; lastActive = null }
                        )
                        active != null -> RideTrackingContent(active)
                        pendingRequest != null -> SearchingContent(onCancel = viewModel::cancelPendingRequest)
                        showBooking -> BookingContent(
                            error = error,
                            fareConfig = fareConfig,
                            onDismissError = viewModel::dismissError,
                            onConfirm = { p, d, count, luggage, notes ->
                                viewModel.requestRide(p, d, count, luggage, notes)
                                showBooking = false
                            },
                            onBack = { showBooking = false }
                        )
                        else -> PassengerDashboard(onBookRide = { showBooking = true })
                    }
                }
                PassengerTab.HISTORY -> SimplePlaceholder(
                    icon = Icons.Filled.History,
                    title = "Ride History",
                    message = "Your completed and cancelled rides will appear here."
                )
                PassengerTab.SUPPORT -> SupportContent()
                PassengerTab.PROFILE -> PassengerProfileContent(onSignOut = onSignOut)
            }
        }
    }
}

@Composable
private fun PassengerBottomBar(selected: PassengerTab, onSelect: (PassengerTab) -> Unit) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        NavigationBarItem(
            selected = selected == PassengerTab.HOME,
            onClick = { onSelect(PassengerTab.HOME) },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = selected == PassengerTab.HISTORY,
            onClick = { onSelect(PassengerTab.HISTORY) },
            icon = { Icon(Icons.Filled.History, contentDescription = "History") },
            label = { Text("History") }
        )
        NavigationBarItem(
            selected = selected == PassengerTab.SUPPORT,
            onClick = { onSelect(PassengerTab.SUPPORT) },
            icon = { Icon(Icons.Filled.SupportAgent, contentDescription = "Support") },
            label = { Text("Support") }
        )
        NavigationBarItem(
            selected = selected == PassengerTab.PROFILE,
            onClick = { onSelect(PassengerTab.PROFILE) },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}

@Composable
private fun PassengerDashboard(onBookRide: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Hello! 👋",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Where are you headed today?",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(20.dp))

        // "Where to?" hero card
        Card(
            shape = RoundedCornerShape(22.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onBookRide)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.horizontalGradient(listOf(ForestGreen, EmeraldGreen)))
                    .padding(24.dp)
            ) {
                Column {
                    Text(
                        text = "Where to?",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Book a ride now",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickAction(Icons.Filled.DirectionsBike, "Book Ride", Modifier.weight(1f), onBookRide)
            QuickAction(Icons.Filled.History, "History", Modifier.weight(1f)) {}
            QuickAction(Icons.Filled.Notifications, "Alerts", Modifier.weight(1f)) {}
            QuickAction(Icons.Filled.Person, "Profile", Modifier.weight(1f)) {}
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Recent Rides",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        SectionCard {
            Column {
                Text(
                    "No rides yet",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Book your first tricycle ride around campus.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun QuickAction(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.primary)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(label, style = MaterialTheme.typography.labelMedium, maxLines = 1)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private val LUGGAGE_OPTIONS = listOf(
    "Backpack", "Large Bag", "Shopping Bags", "Box / Package", "Market Goods"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BookingContent(
    error: String?,
    fareConfig: FareConfig,
    onDismissError: () -> Unit,
    onConfirm: (Location, Location, Int, String, String) -> Unit,
    onBack: () -> Unit
) {
    var pickup by remember { mutableStateOf<Location?>(null) }
    var dropoff by remember { mutableStateOf<Location?>(null) }
    var passengerCount by remember { mutableStateOf(1) }
    val selectedLuggage = remember { mutableStateListOf<String>() }
    var notes by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text("Book Ride", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(12.dp))

        MapPlaceholder(height = 160.dp)
        Spacer(modifier = Modifier.height(16.dp))

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

        LocationField("Pickup Location", Icons.Filled.MyLocation, pickup) { pickup = it }
        Spacer(modifier = Modifier.height(12.dp))
        LocationField("Destination", Icons.Filled.LocationOn, dropoff) { dropoff = it }
        Spacer(modifier = Modifier.height(16.dp))

        // Passenger count
        SectionCard {
            Column {
                Text("Number of Passengers", style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold)
                Text("Max 3 per tricycle", style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FilledTonalIconButton(
                        onClick = { if (passengerCount > 1) passengerCount-- },
                        enabled = passengerCount > 1
                    ) { Icon(Icons.Filled.Remove, contentDescription = "Fewer") }
                    Text(
                        "$passengerCount",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    FilledTonalIconButton(
                        onClick = { if (passengerCount < 3) passengerCount++ },
                        enabled = passengerCount < 3
                    ) { Icon(Icons.Filled.Add, contentDescription = "More") }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Luggage
        SectionCard {
            Column {
                Text("Luggage / Items to Carry", style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold)
                Text("Select all that apply — the driver will be notified",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    LUGGAGE_OPTIONS.forEach { option ->
                        FilterChip(
                            selected = option in selectedLuggage,
                            onClick = {
                                if (option in selectedLuggage) selectedLuggage.remove(option)
                                else selectedLuggage.add(option)
                            },
                            label = { Text(option) }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Notes for driver (optional)") },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        val from = pickup
        val to = dropoff
        val valid = from != null && to != null && from.address != to.address
        if (valid && from != null && to != null) {
            val distanceKm = LocationUtils.distanceKm(from, to)
            val routeFare = FareEngine.routeFare(fareConfig, from.address, to.address)
            val baseTotal = FareEngine.baseTotal(fareConfig, from.address, to.address, distanceKm)
            val extraFare = FareEngine.extraPassengerFare(fareConfig, passengerCount)
            val total = FareEngine.total(fareConfig, from.address, to.address, distanceKm, passengerCount)
            SectionCard {
                Column {
                    Text("Fare Estimate", style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    if (routeFare != null) {
                        FareLine("Fixed route fare", "₱%.2f".format(routeFare))
                    } else {
                        FareLine("Base fare", "₱%.2f".format(fareConfig.baseFare))
                        FareLine(
                            "Distance (%.1f km)".format(distanceKm),
                            "₱%.2f".format(baseTotal - fareConfig.baseFare)
                        )
                    }
                    if (extraFare > 0) {
                        FareLine("Extra passengers (+${passengerCount - 1})", "₱%.2f".format(extraFare))
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Estimate", fontWeight = FontWeight.Bold)
                        Text("₱%.2f".format(total), style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        PrimaryButton(
            text = "Find a Driver",
            onClick = {
                if (from != null && to != null) {
                    val luggage = if (selectedLuggage.isEmpty()) "None" else selectedLuggage.joinToString(", ")
                    onConfirm(from, to, passengerCount, luggage, notes)
                }
            },
            enabled = valid
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun FareLine(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationField(
    label: String,
    icon: ImageVector,
    selected: Location?,
    onSelected: (Location) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = selected?.address ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            leadingIcon = { Icon(icon, contentDescription = null) },
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
                    onClick = { onSelected(location); expanded = false }
                )
            }
        }
    }
}

@Composable
private fun SearchingContent(onCancel: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(56.dp))
        Spacer(modifier = Modifier.height(24.dp))
        Text("Searching for drivers...", style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Nearby verified tricycle drivers have been notified of your request.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        SecondaryButton(text = "Cancel Request", onClick = onCancel)
    }
}

@Composable
private fun RideTrackingContent(ride: Ride) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = trackingHeadline(ride.status),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            "Please wait for your driver to arrive",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Driver card
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
                    Text("Your Driver", style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Star, contentDescription = null,
                            tint = RatingColor, modifier = Modifier.size(16.dp))
                        Text(" 4.8  •  Verified", style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Filled.Chat, contentDescription = "Message",
                        tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Filled.Phone, contentDescription = "Call",
                        tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        MapPlaceholder(height = 160.dp)
        Spacer(modifier = Modifier.height(16.dp))

        // Trip route
        SectionCard {
            Column {
                RouteRow(Icons.Filled.MyLocation, "Pickup", ride.pickupLocation.address)
                Spacer(modifier = Modifier.height(10.dp))
                RouteRow(Icons.Filled.LocationOn, "Destination", ride.dropoffLocation.address)
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${ride.passengerCount} passenger(s)", style = MaterialTheme.typography.bodyMedium)
                    Text(ride.luggage, style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Progress timeline
        Text("Ride Progress", style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        val step = statusStep(ride.status)
        TimelineRow("Ride Requested", step >= 0)
        TimelineRow("Driver Accepted", step >= 1)
        TimelineRow("Driver on the way", step >= 2)
        TimelineRow("Driver Arrived", step >= 3)
        TimelineRow("Ride Started", step >= 4)
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun RouteRow(icon: ImageVector, label: String, value: String) {
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
private fun TimelineRow(label: String, done: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = if (done) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (done) FontWeight.Bold else FontWeight.Normal,
            color = if (done) MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun RideCompleteContent(ride: Ride, onBackHome: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(56.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text("Ride Completed!", style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold)
        Text("Thank you for riding with us.", style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(24.dp))

        SectionCard {
            Column {
                SummaryRow("Fare", "₱%.2f".format(ride.estimatedFare))
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                SummaryRow("Passengers", "${ride.passengerCount}")
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                SummaryRow("Luggage", ride.luggage)
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                SummaryRow("Estimated Duration", "${ride.estimatedDuration} min")
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                SummaryRow("Payment Method", "Cash")
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                SummaryRow("From", ride.pickupLocation.address)
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                SummaryRow("To", ride.dropoffLocation.address)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        Text("Rate your driver", style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        var rating by remember { mutableStateOf(0) }
        Row {
            (1..5).forEach { i ->
                IconButton(onClick = { rating = i }) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = "Star $i",
                        tint = if (i <= rating) RatingColor else MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        PrimaryButton(text = "Back to Home", onClick = onBackHome)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.End)
    }
}

@Composable
private fun MapPlaceholder(height: Dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Filled.Map,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Text(
                "Live map",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SupportContent() {
    var description by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text("Support", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("We're here to help", style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(20.dp))

        SectionCard {
            Column {
                Text("Report an Issue", style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                if (submitted) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Report submitted. Thank you!",
                            color = MaterialTheme.colorScheme.primary)
                    }
                } else {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Describe your concern") },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    PrimaryButton(
                        text = "Submit Report",
                        onClick = { submitted = true },
                        enabled = description.isNotBlank()
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        SectionCard {
            Column {
                Text("Contact", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                ContactRow(Icons.Filled.Phone, "Hotline", "0966-749-7561")
                Spacer(modifier = Modifier.height(8.dp))
                ContactRow(Icons.Filled.SupportAgent, "Email", "trikride@tpc.edu.ph")
                Spacer(modifier = Modifier.height(8.dp))
                ContactRow(Icons.Filled.History, "Hours", "6:00 AM – 9:00 PM")
            }
        }
    }
}

@Composable
private fun ContactRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(12.dp))
        Text(label, style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun PassengerProfileContent(onSignOut: () -> Unit) {
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
        Text("Passenger", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("Talibon Polytechnic College", style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(20.dp))
        SettingsCard(onSignOut = onSignOut)
    }
}

private fun trackingHeadline(status: RideStatus): String = when (status) {
    RideStatus.ACCEPTED -> "Driver accepted your ride!"
    RideStatus.DRIVER_ARRIVING -> "Driver is on the way"
    RideStatus.DRIVER_ARRIVED -> "Your driver has arrived"
    RideStatus.IN_PROGRESS -> "Ride in progress"
    else -> "Finding your driver..."
}

private fun statusStep(status: RideStatus): Int = when (status) {
    RideStatus.REQUESTED, RideStatus.SEARCHING -> 0
    RideStatus.ACCEPTED -> 1
    RideStatus.DRIVER_ARRIVING -> 2
    RideStatus.DRIVER_ARRIVED -> 3
    RideStatus.IN_PROGRESS -> 4
    RideStatus.COMPLETED -> 5
    else -> 0
}
