package com.tpc.trikride.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tpc.trikride.models.FareConfig
import com.tpc.trikride.models.FareStop
import com.tpc.trikride.models.FareType
import com.tpc.trikride.models.Location
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.RideStatus
import com.tpc.trikride.models.UserType
import com.tpc.trikride.ui.components.MapPin
import com.tpc.trikride.ui.components.PickerMap
import com.tpc.trikride.ui.components.PrimaryButton
import com.tpc.trikride.ui.components.RefreshableBox
import com.tpc.trikride.ui.components.SecondaryButton
import com.tpc.trikride.ui.components.SectionCard
import com.tpc.trikride.ui.components.SkeletonCard
import com.tpc.trikride.ui.components.SupportPanel
import com.tpc.trikride.ui.components.TALIBON_CENTRE
import com.tpc.trikride.ui.components.TrikMap
import com.tpc.trikride.ui.theme.EmeraldGreen
import com.tpc.trikride.ui.theme.ForestGreen
import com.tpc.trikride.ui.theme.RatingColor
import com.tpc.trikride.utils.Constants
import com.tpc.trikride.utils.FareEngine
import com.tpc.trikride.utils.LocationProvider
import com.tpc.trikride.utils.LocationUtils
import com.tpc.trikride.utils.Navigation
import com.tpc.trikride.utils.ReverseGeocoder
import com.tpc.trikride.viewmodels.PassengerViewModel
import com.tpc.trikride.viewmodels.SupportViewModel
import kotlinx.coroutines.launch

private enum class PassengerTab { HOME, HISTORY, SUPPORT, PROFILE }

@Composable
fun PassengerHomeScreen(
    userId: String,
    onSignOut: () -> Unit,
    viewModel: PassengerViewModel = viewModel(),
    supportViewModel: SupportViewModel = viewModel()
) {
    LaunchedEffect(userId) {
        viewModel.bind(userId)
        supportViewModel.bind(userId)
    }

    val activeRides by viewModel.activeRides.collectAsState()
    val pendingRequest by viewModel.pendingRequest.collectAsState()
    val error by viewModel.errorMessage.collectAsState()
    val fareConfig by viewModel.fareConfig.collectAsState()
    val fareStops by viewModel.fareStops.collectAsState()
    val driverLocation by viewModel.driverLocation.collectAsState()
    val assignedDriver by viewModel.assignedDriver.collectAsState()
    val ratedRides by viewModel.ratedRides.collectAsState()
    // The dashboard below is only reached when there is no active ride, so the
    // active list it used to be given was empty by construction and "Recent
    // Rides" read "No rides yet" for everybody. Finished rides are what the
    // heading means.
    val rideHistory by viewModel.rideHistory.collectAsState()

    val notifications by supportViewModel.notifications.collectAsState()
    val unreadCount = notifications.count { !it.read }

    var tab by remember { mutableStateOf(PassengerTab.HOME) }
    var showBooking by remember { mutableStateOf(false) }
    var showNotifications by remember { mutableStateOf(false) }
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

    if (showNotifications) {
        NotificationsScreen(
            userId = userId,
            viewModel = supportViewModel,
            onBack = { showNotifications = false }
        )
        return
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
                            alreadyRated = completedRide!!.id in ratedRides,
                            onRate = { stars -> viewModel.rateRide(completedRide!!, stars) },
                            onBackHome = { completedRide = null; lastActive = null }
                        )
                        active != null -> RideTrackingContent(active, driverLocation, assignedDriver)
                        pendingRequest != null -> SearchingContent(onCancel = viewModel::cancelPendingRequest)
                        showBooking -> BookingContent(
                            error = error,
                            fareConfig = fareConfig,
                            fareStops = fareStops,
                            onDismissError = viewModel::dismissError,
                            onConfirm = { p, stop, regular, discounted, luggage, notes ->
                                viewModel.requestRide(p, stop, regular, discounted, luggage, notes)
                                showBooking = false
                            },
                            onBack = { showBooking = false }
                        )
                        else -> PassengerDashboard(
                            rides = rideHistory,
                            unreadCount = unreadCount,
                            onBookRide = { showBooking = true },
                            onOpenNotifications = { showNotifications = true },
                            onRefresh = viewModel::refresh
                        )
                    }
                }
                PassengerTab.HISTORY -> RideHistoryContent(viewModel)
                PassengerTab.SUPPORT -> SupportPanel(
                    userId = userId,
                    reporterType = UserType.PASSENGER,
                    viewModel = supportViewModel
                )
                PassengerTab.PROFILE -> SettingsScreen(
                    userId = userId,
                    userType = com.tpc.trikride.models.UserType.PASSENGER,
                    subtitle = "Talibon Polytechnic College",
                    onSignOut = onSignOut
                )
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
private fun PassengerDashboard(
    rides: List<Ride>,
    unreadCount: Int,
    onBookRide: () -> Unit,
    onOpenNotifications: () -> Unit,
    onRefresh: () -> Unit
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Hello!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Where are you headed today?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onOpenNotifications) {
                    BadgedBox(badge = { if (unreadCount > 0) Badge { Text("$unreadCount") } }) {
                        Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

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
                text = "Recent Rides",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            if (rides.isEmpty()) {
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
            } else {
                rides.take(3).forEach { ride ->
                    RideRow(ride)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun RideHistoryContent(viewModel: PassengerViewModel) {
    val history by viewModel.rideHistory.collectAsState()
    val loading by viewModel.loadingHistory.collectAsState()

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
            Text("Your past trips", style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(16.dp))

            when {
                loading -> repeat(3) {
                    SkeletonCard(lines = 2)
                    Spacer(modifier = Modifier.height(10.dp))
                }
                history.isEmpty() -> SectionCard {
                    Text(
                        "No completed rides yet.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                else -> history.forEach { ride ->
                    RideRow(ride)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun RideRow(ride: Ride) {
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
                Text(
                    ride.status.name.replace('_', ' '),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                "P%.2f".format(ride.estimatedFare),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private val LUGGAGE_OPTIONS = listOf(
    "Backpack", "Large Bag", "Shopping Bags", "Box / Package", "Market Goods"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BookingContent(
    error: String?,
    fareConfig: FareConfig,
    fareStops: List<FareStop>,
    onDismissError: () -> Unit,
    onConfirm: (Location, FareStop, Int, Int, String, String) -> Unit,
    onBack: () -> Unit
) {
    var pickup by remember { mutableStateOf<Location?>(null) }
    var destination by remember { mutableStateOf<FareStop?>(null) }
    // Two counters rather than one rate switch: a party can hold both kinds at
    // once, and the posted sheet prices them from different columns.
    var regularCount by remember { mutableStateOf(1) }
    var discountedCount by remember { mutableStateOf(0) }
    val passengerCount = regularCount + discountedCount
    val selectedLuggage = remember { mutableStateListOf<String>() }
    var notes by remember { mutableStateOf("") }
    var pickingDestination by remember { mutableStateOf(false) }
    var pickingPickup by remember { mutableStateOf(false) }
    var pinningPickup by remember { mutableStateOf(false) }
    var pinningDestination by remember { mutableStateOf(false) }

    // The sheet carries two rates that are not tied to a numbered stop, so they
    // are offered alongside the rest rather than being admin-only trivia.
    val bookable = remember(fareStops, fareConfig) {
        FareEngine.flatStops(fareConfig) + fareStops.filter { it.active }
    }

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

        TrikMap(
            height = 180.dp,
            connectPins = pickup?.hasCoordinates == true && destination?.hasCoordinates == true,
            pins = buildList {
                pickup?.takeIf { it.hasCoordinates }
                    ?.let { add(MapPin(it, "Pickup", EmeraldGreen)) }
                destination?.takeIf { it.hasCoordinates }
                    ?.let { add(MapPin(it.location, it.name, ForestGreen, emphasis = true)) }
            }
        )
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

        if (bookable.isEmpty()) {
            SectionCard {
                Column {
                    Text(
                        "Destinations are not available yet",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "The fare table has not been loaded. Ask the administrator to " +
                            "publish the official FeTODAT rates.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Same searchable list as the destination. Pickup used to come from a
        // short fixed list of campus points, which was wrong for anyone not
        // starting at the campus.
        StopField(
            label = "Pickup Location",
            value = pickup?.address.orEmpty(),
            placeholder = "Choose where to be collected",
            icon = Icons.Filled.MyLocation,
            enabled = bookable.isNotEmpty(),
            onClick = { pickingPickup = true }
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = { pinningPickup = true }) {
                Icon(Icons.Filled.Place, contentDescription = null,
                    modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Not on the list? Pin it on the map")
            }
        }
        Spacer(modifier = Modifier.height(6.dp))

        StopField(
            label = "Destination",
            value = destination?.label.orEmpty(),
            placeholder = "Choose a stop",
            icon = Icons.Filled.LocationOn,
            enabled = bookable.isNotEmpty(),
            onClick = { pickingDestination = true }
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = { pinningDestination = true },
                enabled = bookable.isNotEmpty()
            ) {
                Icon(Icons.Filled.Map, contentDescription = null,
                    modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Don't know the name? Find it on the map")
            }
        }
        // No fare is shown at all until a destination is chosen, which is the
        // moment someone assumes the minimum is the price.
        if (destination == null) {
            val minimum = "₱%.0f".format(FareEngine.minimumFor(fareConfig, FareType.REGULAR))
            Text(
                "The fare depends on where you are going. $minimum is the minimum, " +
                    "not a flat rate, and longer trips cost more. Choose a destination " +
                    "and the exact amount appears here.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Who is travelling. A type is chosen first and its counter appears
        // with it, so a party of one kind never sees a second counter sitting
        // at zero. A count of zero and an unchosen type mean the same thing to
        // the fare engine; the chip is what the passenger reasons about.
        SectionCard {
            Column {
                Text("Who is travelling", style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold)
                Text(
                    "Choose every kind of passenger in your party. Seniors, persons " +
                        "with disabilities and students pay the discounted column of the " +
                        "posted sheet — bring the ID, the driver will ask for it.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = regularCount > 0,
                        onClick = {
                            // Never leave the tricycle empty: the last kind
                            // standing cannot be unchosen.
                            if (regularCount > 0) {
                                if (discountedCount > 0) regularCount = 0
                            } else if (passengerCount < Constants.MAX_PASSENGERS) {
                                regularCount = 1
                            }
                        },
                        enabled = regularCount > 0 ||
                            passengerCount < Constants.MAX_PASSENGERS,
                        label = { Text("Regular") }
                    )
                    FilterChip(
                        selected = discountedCount > 0,
                        onClick = {
                            if (discountedCount > 0) {
                                if (regularCount > 0) discountedCount = 0
                            } else if (passengerCount < Constants.MAX_PASSENGERS) {
                                discountedCount = 1
                            }
                        },
                        enabled = discountedCount > 0 ||
                            passengerCount < Constants.MAX_PASSENGERS,
                        label = { Text("Senior / PWD / Student") }
                    )
                }

                if (regularCount > 0) {
                    Spacer(modifier = Modifier.height(12.dp))
                    PassengerCounter(
                        label = "Regular",
                        count = regularCount,
                        canAdd = passengerCount < Constants.MAX_PASSENGERS,
                        canRemove = regularCount > 1,
                        onAdd = { regularCount++ },
                        onRemove = { regularCount-- }
                    )
                }
                if (discountedCount > 0) {
                    Spacer(modifier = Modifier.height(12.dp))
                    PassengerCounter(
                        label = "Senior / PWD / Student",
                        count = discountedCount,
                        canAdd = passengerCount < Constants.MAX_PASSENGERS,
                        canRemove = discountedCount > 1,
                        onAdd = { discountedCount++ },
                        onRemove = { discountedCount-- }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    if (passengerCount >= Constants.MAX_PASSENGERS) {
                        "Full — ${Constants.MAX_PASSENGERS} seats is the most a tricycle takes"
                    } else {
                        "$passengerCount of ${Constants.MAX_PASSENGERS} seats" +
                            if (fareConfig.chargePerPassenger) ", charged per head" else ""
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
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
        val to = destination
        val valid = from != null && to != null

        if (from != null && to != null) {
            val quote = FareEngine.quote(fareConfig, to, regularCount, discountedCount)
            SectionCard {
                Column {
                    Text("Fare", style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold)
                    Text(
                        to.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (quote.regularCount > 0) {
                        FareLine(
                            "Regular × ${quote.regularCount}",
                            "₱%.2f".format(quote.regularRate * quote.regularCount)
                        )
                    }
                    if (quote.discountedCount > 0) {
                        FareLine(
                            "Senior / PWD / student × ${quote.discountedCount}",
                            "₱%.2f".format(quote.discountedRate * quote.discountedCount)
                        )
                    }
                    if (quote.minimumApplied) {
                        FareLine("Minimum fare applied", "yes")
                    }
                    if (!fareConfig.chargePerPassenger) {
                        FareLine("Charged per tricycle", "not per head")
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total", fontWeight = FontWeight.Bold)
                        Text("₱%.2f".format(quote.total), style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Cash on arrival. Rates are the ones posted by FeTODAT.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        PrimaryButton(
            text = "Find a Driver",
            onClick = {
                if (from != null && to != null) {
                    val luggage = if (selectedLuggage.isEmpty()) "None" else selectedLuggage.joinToString(", ")
                    onConfirm(from, to, regularCount, discountedCount, luggage, notes)
                }
            },
            enabled = valid
        )
        Spacer(modifier = Modifier.height(16.dp))
    }

    if (pinningPickup) {
        PickupPinner(
            initial = pickup ?: TALIBON_CENTRE,
            onDismiss = { pinningPickup = false },
            onPicked = { pickup = it; pinningPickup = false }
        )
    }

    if (pinningDestination) {
        DestinationFinder(
            stops = bookable,
            initial = pickup ?: TALIBON_CENTRE,
            onDismiss = { pinningDestination = false },
            onPicked = { destination = it; pinningDestination = false },
            onUseList = { pinningDestination = false; pickingDestination = true }
        )
    }

    if (pickingDestination) {
        StopPicker(
            title = "Where to?",
            stops = bookable,
            showFares = true,
            minimumFare = FareEngine.minimumFor(fareConfig, FareType.REGULAR),
            onDismiss = { pickingDestination = false },
            onPick = { destination = it; pickingDestination = false }
        )
    }

    if (pickingPickup) {
        StopPicker(
            title = "Where from?",
            stops = bookable,
            showFares = false,
            minimumFare = FareEngine.minimumFor(fareConfig, FareType.REGULAR),
            onDismiss = { pickingPickup = false },
            onPick = { pickup = it.location; pickingPickup = false }
        )
    }
}

/** One row of the two-column party counter: a label, a number, and two buttons. */
@Composable
private fun PassengerCounter(
    label: String,
    count: Int,
    canAdd: Boolean,
    canRemove: Boolean,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        FilledTonalIconButton(onClick = onRemove, enabled = canRemove) {
            Icon(Icons.Filled.Remove, contentDescription = "One fewer $label")
        }
        Text(
            "$count",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        FilledTonalIconButton(onClick = onAdd, enabled = canAdd) {
            Icon(Icons.Filled.Add, contentDescription = "One more $label")
        }
    }
}

/**
 * Full-screen map for pinning a pickup point that is not on the list.
 *
 * The map moves under a fixed centre pin rather than asking the user to drag a
 * marker, which is far easier one-handed. "Use my location" is offered but not
 * required: a passenger can pin the corner they will actually be standing on,
 * which is often not where they are standing now.
 */
@Composable
private fun PickupPinner(
    initial: Location,
    onDismiss: () -> Unit,
    onPicked: (Location) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var centre by remember { mutableStateOf(initial) }
    var label by remember { mutableStateOf("") }
    var locating by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            locating = true
            scope.launch {
                LocationProvider.current(context)?.let { centre = it }
                locating = false
            }
        }
    }

    // Describe wherever the pin has settled, a beat after it stops moving.
    LaunchedEffect(centre.latitude, centre.longitude) {
        label = ""
        kotlinx.coroutines.delay(400)
        label = ReverseGeocoder.describe(context, centre)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 16.dp, top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close")
                    }
                    Text(
                        "Set your pickup point",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    "Move the map so the pin sits where you want to be picked up.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))

                PickerMap(
                    height = 380.dp,
                    centre = centre,
                    pinColor = EmeraldGreen,
                    onMoved = { centre = it },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    SecondaryButton(
                        text = if (locating) "Finding you..." else "Use my current location",
                        onClick = {
                            if (LocationProvider.hasPermission(context)) {
                                locating = true
                                scope.launch {
                                    LocationProvider.current(context)?.let { centre = it }
                                    locating = false
                                }
                            } else {
                                permissionLauncher.launch(
                                    android.Manifest.permission.ACCESS_FINE_LOCATION
                                )
                            }
                        },
                        enabled = !locating
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    SectionCard {
                        Column {
                            Text(
                                "Pickup point",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                label.ifBlank { "Locating..." },
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    PrimaryButton(
                        text = "Use this point",
                        onClick = { onPicked(centre.copy(address = label.ifBlank { "Pinned location" })) },
                        enabled = centre.hasCoordinates
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

/**
 * Full-screen map for choosing a destination by pointing at it.
 *
 * The ordinance prices a ride per named stop, not per kilometre, so this cannot
 * drop a pin anywhere and charge for it. What it does instead is let the
 * passenger move the map to roughly where they are going and name the posted
 * stop nearest to that point, with its fare, which is the part they could not
 * do before: you can find your destination without already knowing what the
 * fare sheet calls it.
 *
 * Only stops with coordinates can be found this way, and those are filled in by
 * the administrator over time. When none has been yet, the map is pointless and
 * the list is offered instead.
 */
@Composable
private fun DestinationFinder(
    stops: List<FareStop>,
    initial: Location,
    onDismiss: () -> Unit,
    onPicked: (FareStop) -> Unit,
    onUseList: () -> Unit
) {
    val mappable = remember(stops) { stops.filter { it.hasCoordinates } }
    var centre by remember { mutableStateOf(if (initial.hasCoordinates) initial else TALIBON_CENTRE) }

    val nearest = remember(mappable, centre.latitude, centre.longitude) {
        mappable.minByOrNull { LocationUtils.distanceKm(centre, it.location) }
    }
    val awayKm = nearest?.let { LocationUtils.distanceKm(centre, it.location) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 16.dp, top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close")
                    }
                    Text(
                        "Find your destination",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (mappable.isEmpty()) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        SectionCard {
                            Column {
                                Text(
                                    "No stop has a map position yet",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    "The posted fare sheet gives names, not coordinates, so " +
                                        "the administrator adds positions to the stops over " +
                                        "time. Until then, choose your destination from the " +
                                        "list — the fare is the same either way.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        PrimaryButton(text = "Choose from the list", onClick = onUseList)
                    }
                } else {
                    Text(
                        "Move the map to where you are going. The nearest posted stop is " +
                            "named below, and that is the one you will be charged for.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    PickerMap(
                        height = 360.dp,
                        centre = centre,
                        pinColor = ForestGreen,
                        onMoved = { centre = it },
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        SectionCard {
                            Column {
                                Text(
                                    "Nearest posted stop",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    nearest?.label ?: "—",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                if (nearest != null && awayKm != null) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        "%s from the pin · ₱%.0f regular, ₱%.0f discounted".format(
                                            if (awayKm < 1.0) "%.0f m".format(awayKm * 1000)
                                            else "%.1f km".format(awayKm),
                                            FareEngine.rateFor(nearest, FareType.REGULAR),
                                            FareEngine.rateFor(nearest, FareType.DISCOUNTED)
                                        ),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = onUseList) { Text("Choose from the list instead") }
                        Spacer(modifier = Modifier.height(4.dp))
                        PrimaryButton(
                            text = "Use this stop",
                            onClick = { nearest?.let(onPicked) },
                            enabled = nearest != null
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

/**
 * Full-screen searchable list of the posted stops.
 *
 * A dropdown does not work at this size, and neither does asking someone to
 * remember which zone their destination sits in, so the search matches both
 * the stop name and the zone, and the price for the selected rate column is
 * shown on every row.
 */
@Composable
private fun StopPicker(
    title: String,
    stops: List<FareStop>,
    /**
     * Fares belong to the destination. Showing one beside a pickup stop would
     * read as the price of being collected there, which is not a thing.
     */
    showFares: Boolean,
    minimumFare: Double,
    onDismiss: () -> Unit,
    onPick: (FareStop) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var zone by remember { mutableStateOf<String?>(null) }
    val zones = remember(stops) { stops.map { it.zone }.distinct().sorted() }

    val results = remember(stops, query, zone) {
        // Every word has to appear somewhere, so "poblacion talibon" and
        // "market balintawak" both find what the passenger meant. A single
        // substring match over the whole query found neither.
        val terms = query.trim().lowercase().split(" ").filter { it.isNotBlank() }
        stops.asSequence()
            .filter { zone == null || it.zone == zone }
            .filter { stop ->
                val haystack = "${stop.name} ${stop.zone}".lowercase()
                terms.all { haystack.contains(it) }
            }
            // Flat rates first: they are the two the table has no row for, so
            // they are the two nobody finds by scrolling.
            .sortedWith(
                compareBy<FareStop>(
                    { if (it.zone == FareEngine.FLAT_ZONE) 0 else 1 },
                    { it.zone },
                    { it.name }
                )
            )
            .toList()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 16.dp, top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close")
                    }
                    Text(
                        title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                // The minimum is the number people remember, and they remember
                // it as the price. Saying what the rest of the table looks like
                // is the difference between a fare that is higher than expected
                // and one that feels like being overcharged.
                val highest = remember(stops) {
                    stops.maxOfOrNull { FareEngine.rateFor(it, FareType.REGULAR) }
                }
                val note = if (showFares) {
                    val floor = "₱%.0f".format(minimumFare)
                    val ceiling = highest?.let { " and rise to ₱%.0f".format(it) }.orEmpty()
                    "Fares start at $floor$ceiling, depending on how far you are going. " +
                        "$floor is the least a ride can cost, not the usual price."
                } else {
                    "No prices here: the fare comes from where you are going, not " +
                        "from where you are collected."
                }
                Text(
                    note,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Search stop or zone") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = zone == null,
                        onClick = { zone = null },
                        label = { Text("All zones") }
                    )
                    zones.forEach { z ->
                        FilterChip(
                            selected = zone == z,
                            onClick = { zone = if (zone == z) null else z },
                            label = { Text(z) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                if (results.isEmpty()) {
                    Text(
                        "No stop matches that.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(20.dp)
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(
                            start = 20.dp, end = 20.dp, top = 4.dp, bottom = 24.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(results, key = { it.id }) { stop ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onPick(stop) }
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(stop.name, style = MaterialTheme.typography.bodyLarge)
                                    Text(
                                        stop.zone,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (showFares) {
                                    Text(
                                        "₱%.0f / ₱%.0f".format(
                                            FareEngine.rateFor(stop, FareType.REGULAR),
                                            FareEngine.rateFor(stop, FareType.DISCOUNTED)
                                        ),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
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

/**
 * A read-only field that opens a full-screen search rather than a dropdown.
 *
 * Two hundred and forty stops will not fit in a dropdown, and the pickup and
 * destination now draw on the same list, so they use the same control.
 */
@Composable
private fun StopField(
    label: String,
    value: String,
    placeholder: String,
    icon: ImageVector,
    enabled: Boolean,
    onClick: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        readOnly = true,
        enabled = false,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        leadingIcon = { Icon(icon, contentDescription = null) },
        trailingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick)
    )
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
private fun RideTrackingContent(
    ride: Ride,
    driverLocation: Location?,
    driver: com.tpc.trikride.models.Driver?
) {
    val context = LocalContext.current
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
                    Text(
                        ride.driverName.ifBlank { "Your Driver" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Star, contentDescription = null,
                            tint = RatingColor, modifier = Modifier.size(16.dp))
                        // Real numbers or nothing. A driver nobody has rated
                        // yet says so, rather than borrowing someone's score.
                        val score = driver?.takeIf { it.ratingCount > 0 }
                            ?.let { " %.1f".format(it.rating) } ?: " Not yet rated"
                        val tricycle = driver?.tricycleNumber
                            ?.takeIf { it.isNotBlank() }
                            ?.let { "  •  Tricycle $it" }.orEmpty()
                        Text(
                            "$score$tricycle",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                if (ride.driverPhone.isNotBlank()) {
                    IconButton(onClick = { Navigation.dial(context, ride.driverPhone) }) {
                        Icon(Icons.Filled.Phone, contentDescription = "Call the driver",
                            tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        TrikMap(
            height = 200.dp,
            connectPins = true,
            pins = buildList {
                if (ride.pickupLocation.hasCoordinates) {
                    add(MapPin(ride.pickupLocation, "Pickup", EmeraldGreen))
                }
                driverLocation?.let { add(MapPin(it, "Your driver", RatingColor, emphasis = true)) }
            }
        )
        if (driverLocation == null) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Waiting for your driver's location. It appears once they are moving " +
                    "with the app open.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
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
private fun RideCompleteContent(
    ride: Ride,
    alreadyRated: Boolean,
    onRate: (Int) -> Unit,
    onBackHome: () -> Unit
) {
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

        Text(
            if (alreadyRated) "Thanks for rating" else "Rate your driver",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        var rating by remember { mutableStateOf(0) }
        Row {
            (1..5).forEach { i ->
                IconButton(
                    onClick = { rating = i },
                    enabled = !alreadyRated
                ) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = "Star $i",
                        tint = if (i <= rating) RatingColor else MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
        // Sending is a separate, deliberate act. Rating on the first tap would
        // record a three when someone was on their way to five.
        if (!alreadyRated) {
            Spacer(modifier = Modifier.height(4.dp))
            TextButton(
                onClick = { if (rating > 0) onRate(rating) },
                enabled = rating > 0
            ) { Text("Send rating") }
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
