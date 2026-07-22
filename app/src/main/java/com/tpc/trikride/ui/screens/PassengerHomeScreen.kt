package com.tpc.trikride.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import com.tpc.trikride.models.Location
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.RideStatus
import com.tpc.trikride.utils.Constants
import com.tpc.trikride.utils.FareCalculator
import com.tpc.trikride.utils.LocationUtils
import com.tpc.trikride.viewmodels.PassengerViewModel

@Composable
fun PassengerHomeScreen(
    userId: String,
    viewModel: PassengerViewModel = viewModel()
) {
    LaunchedEffect(userId) { viewModel.bind(userId) }

    val activeRides by viewModel.activeRides.collectAsState()
    val pendingRequest by viewModel.pendingRequest.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // A driver picked up the request — stop showing "searching".
    LaunchedEffect(activeRides) { viewModel.clearPendingRequestIfMatched() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Book a Ride",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

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

        when {
            activeRides.isNotEmpty() -> ActiveRideCard(ride = activeRides.first())
            pendingRequest != null -> SearchingCard(onCancel = viewModel::cancelPendingRequest)
            else -> BookingForm(onRequestRide = viewModel::requestRide)
        }
    }
}

@Composable
private fun BookingForm(onRequestRide: (Location, Location, String) -> Unit) {
    var pickup by remember { mutableStateOf<Location?>(null) }
    var dropoff by remember { mutableStateOf<Location?>(null) }
    var notes by remember { mutableStateOf("") }

    LocationDropdown(
        label = "Pickup Location",
        selected = pickup,
        onSelected = { pickup = it }
    )

    LocationDropdown(
        label = "Dropoff Location",
        selected = dropoff,
        onSelected = { dropoff = it }
    )

    OutlinedTextField(
        value = notes,
        onValueChange = { notes = it },
        label = { Text("Notes for driver (optional)") },
        modifier = Modifier.fillMaxWidth()
    )

    val from = pickup
    val to = dropoff
    if (from != null && to != null && from.address != to.address) {
        val distanceKm = LocationUtils.distanceKm(from, to)
        FareEstimateCard(
            distanceKm = distanceKm,
            fare = FareCalculator.estimateFare(distanceKm),
            minutes = FareCalculator.estimateDurationMinutes(distanceKm)
        )
    }

    Button(
        onClick = {
            val p = pickup ?: return@Button
            val d = dropoff ?: return@Button
            onRequestRide(p, d, notes)
        },
        enabled = pickup != null && dropoff != null && pickup?.address != dropoff?.address,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text("Request Ride")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationDropdown(
    label: String,
    selected: Location?,
    onSelected: (Location) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selected?.address ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Constants.CAMPUS_LOCATIONS.forEach { location ->
                DropdownMenuItem(
                    text = { Text(location.address) },
                    onClick = {
                        onSelected(location)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun FareEstimateCard(distanceKm: Double, fare: Double, minutes: Int) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Trip Estimate", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Distance: %.1f km".format(distanceKm))
            Text("Duration: ~$minutes min")
            Text(
                text = "Fare: ₱%.2f".format(fare),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun SearchingCard(onCancel: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(modifier = Modifier.size(48.dp))
            Text(
                "Searching for drivers...",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                "Nearby tricycle drivers have been notified of your request.",
                style = MaterialTheme.typography.bodyMedium
            )
            OutlinedButton(onClick = onCancel) { Text("Cancel Request") }
        }
    }
}

@Composable
private fun ActiveRideCard(ride: Ride) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = rideStatusLabel(ride.status),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text("From: ${ride.pickupLocation.address}")
            Text("To: ${ride.dropoffLocation.address}")
            Text("Estimated fare: ₱%.2f".format(ride.estimatedFare))
            Text("Estimated duration: ${ride.estimatedDuration} min")
            if (ride.notes.isNotBlank()) {
                Text("Notes: ${ride.notes}")
            }
        }
    }
}

private fun rideStatusLabel(status: RideStatus): String = when (status) {
    RideStatus.REQUESTED, RideStatus.SEARCHING -> "Finding your driver..."
    RideStatus.ACCEPTED -> "Driver accepted your ride!"
    RideStatus.DRIVER_ARRIVING -> "Your driver is on the way"
    RideStatus.DRIVER_ARRIVED -> "Your driver has arrived"
    RideStatus.IN_PROGRESS -> "Ride in progress"
    RideStatus.COMPLETED -> "Ride completed"
    RideStatus.CANCELLED -> "Ride cancelled"
    RideStatus.NO_SHOW -> "Marked as no-show"
}
