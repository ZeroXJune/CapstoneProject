package com.talibon.trikride.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.talibon.trikride.models.Driver
import com.talibon.trikride.models.Ride
import com.talibon.trikride.models.RideRequest
import com.talibon.trikride.models.RideStatus
import com.talibon.trikride.models.VerificationStatus
import com.talibon.trikride.utils.FareCalculator
import com.talibon.trikride.utils.LocationUtils
import com.talibon.trikride.viewmodels.DriverViewModel

@Composable
fun DriverHomeScreen(
    userId: String,
    viewModel: DriverViewModel = viewModel()
) {
    LaunchedEffect(userId) { viewModel.bind(userId) }

    val driver by viewModel.driverProfile.collectAsState()
    val openRequests by viewModel.openRequests.collectAsState()
    val activeRides by viewModel.activeRides.collectAsState()
    val isRegistering by viewModel.isRegistering.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        errorMessage?.let { message ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(message, modifier = Modifier.weight(1f))
                    OutlinedButton(onClick = viewModel::dismissError) { Text("Dismiss") }
                }
            }
        }

        val profile = driver
        if (profile == null) {
            DriverOnboardingForm(
                isSubmitting = isRegistering,
                onSubmit = viewModel::registerDriver
            )
        } else {
            DriverDashboard(
                driver = profile,
                openRequests = openRequests,
                activeRides = activeRides,
                onToggleAvailability = viewModel::setAvailability,
                onAcceptRequest = viewModel::acceptRequest,
                onAdvanceRide = viewModel::advanceRide
            )
        }
    }
}

@Composable
private fun DriverOnboardingForm(
    isSubmitting: Boolean,
    onSubmit: (licenseNumber: String, licenseExpiry: String, tricycleNumber: String) -> Unit
) {
    var licenseNumber by remember { mutableStateOf("") }
    var licenseExpiry by remember { mutableStateOf("") }
    var tricycleNumber by remember { mutableStateOf("") }

    Text(
        text = "Driver Registration",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold
    )
    Text(
        text = "Register your details below. An administrator will verify your " +
            "credentials before you can accept passengers.",
        style = MaterialTheme.typography.bodyMedium
    )

    OutlinedTextField(
        value = licenseNumber,
        onValueChange = { licenseNumber = it },
        label = { Text("Driver's License Number") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    OutlinedTextField(
        value = licenseExpiry,
        onValueChange = { licenseExpiry = it },
        label = { Text("License Expiry (MM/YYYY)") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    OutlinedTextField(
        value = tricycleNumber,
        onValueChange = { tricycleNumber = it },
        label = { Text("Tricycle Body / Plate Number") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    Button(
        onClick = { onSubmit(licenseNumber.trim(), licenseExpiry.trim(), tricycleNumber.trim()) },
        enabled = !isSubmitting &&
            licenseNumber.isNotBlank() &&
            licenseExpiry.isNotBlank() &&
            tricycleNumber.isNotBlank(),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(if (isSubmitting) "Submitting..." else "Submit for Verification")
    }
}

@Composable
private fun DriverDashboard(
    driver: Driver,
    openRequests: List<RideRequest>,
    activeRides: List<Ride>,
    onToggleAvailability: (Boolean) -> Unit,
    onAcceptRequest: (RideRequest) -> Unit,
    onAdvanceRide: (Ride) -> Unit
) {
    Text(
        text = "Driver Dashboard",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold
    )

    if (driver.verificationStatus != VerificationStatus.APPROVED) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Verification: ${driver.verificationStatus}",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Your registration is being reviewed by the administrator. " +
                        "You will be notified once approved.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    // Availability toggle
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (driver.isAvailable) "You are ONLINE" else "You are OFFLINE",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (driver.isAvailable) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Tricycle #${driver.tricycleNumber}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Switch(
                checked = driver.isAvailable,
                onCheckedChange = onToggleAvailability
            )
        }
    }

    // Active rides take priority over new requests
    if (activeRides.isNotEmpty()) {
        Text("Current Ride", style = MaterialTheme.typography.titleLarge)
        activeRides.forEach { ride ->
            ActiveDriverRideCard(ride = ride, onAdvance = onAdvanceRide)
        }
    } else if (driver.isAvailable) {
        Text("Incoming Requests", style = MaterialTheme.typography.titleLarge)
        if (openRequests.isEmpty()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "No ride requests right now. New requests will appear here.",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            openRequests.forEach { request ->
                RideRequestCard(request = request, onAccept = { onAcceptRequest(request) })
            }
        }
    } else {
        Card(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Go online to start receiving ride requests.",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun RideRequestCard(request: RideRequest, onAccept: () -> Unit) {
    val distanceKm = LocationUtils.distanceKm(request.pickupLocation, request.dropoffLocation)
    val fare = FareCalculator.estimateFare(distanceKm)

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("Pickup: ${request.pickupLocation.address}", fontWeight = FontWeight.Bold)
            Text("Dropoff: ${request.dropoffLocation.address}")
            Text("Trip: %.1f km  •  Est. fare ₱%.2f".format(distanceKm, fare))
            if (request.notes.isNotBlank()) {
                Text("Notes: ${request.notes}", style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onAccept,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Accept Ride")
            }
        }
    }
}

@Composable
private fun ActiveDriverRideCard(ride: Ride, onAdvance: (Ride) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Status: ${ride.status}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text("Pickup: ${ride.pickupLocation.address}")
            Text("Dropoff: ${ride.dropoffLocation.address}")
            Text("Fare: ₱%.2f".format(ride.estimatedFare))
            if (ride.notes.isNotBlank()) {
                Text("Passenger notes: ${ride.notes}")
            }

            val actionLabel = when (ride.status) {
                RideStatus.ACCEPTED -> "Start Driving to Pickup"
                RideStatus.DRIVER_ARRIVING -> "I've Arrived at Pickup"
                RideStatus.DRIVER_ARRIVED -> "Start Ride"
                RideStatus.IN_PROGRESS -> "Complete Ride"
                else -> null
            }
            if (actionLabel != null) {
                Button(
                    onClick = { onAdvance(ride) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(actionLabel)
                }
            }
        }
    }
}
