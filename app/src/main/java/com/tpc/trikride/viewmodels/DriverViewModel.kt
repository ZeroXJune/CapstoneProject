package com.tpc.trikride.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tpc.trikride.models.Driver
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.RideRequest
import com.tpc.trikride.models.NotificationType
import com.tpc.trikride.repositories.DriverRepository
import com.tpc.trikride.repositories.RideRepository
import com.tpc.trikride.repositories.SupportRepository
import com.tpc.trikride.utils.LocationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class DriverViewModel(
    private val driverRepository: DriverRepository = DriverRepository(),
    private val rideRepository: RideRepository = RideRepository(),
    private val supportRepository: SupportRepository = SupportRepository()
) : ViewModel() {

    private val driverId = MutableStateFlow<String?>(null)

    /** Publishes position while the driver is online; cancelled when offline. */
    private var locationJob: Job? = null

    /** The driver's own last known position, for centring their map. */
    private val _myLocation = MutableStateFlow<com.tpc.trikride.models.Location?>(null)
    val myLocation: StateFlow<com.tpc.trikride.models.Location?> = _myLocation

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    /** True while the initial profile load / registration is in flight. */
    private val _isRegistering = MutableStateFlow(false)
    val isRegistering: StateFlow<Boolean> = _isRegistering

    /** The driver's profile; null until they complete onboarding. */
    val driverProfile: StateFlow<Driver?> = driverId
        .filterNotNull()
        .flatMapLatest { driverRepository.driverProfile(it) }
        .catch { _errorMessage.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    /** Ride requests waiting for a driver, visible when online. */
    val openRequests: StateFlow<List<RideRequest>> = driverId
        .filterNotNull()
        .flatMapLatest { rideRepository.openRideRequests() }
        .catch { _errorMessage.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Rides this driver has accepted and not yet completed. */
    val activeRides: StateFlow<List<Ride>> = driverId
        .filterNotNull()
        .flatMapLatest { rideRepository.driverActiveRides(it) }
        .catch { _errorMessage.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Finished rides for this driver, newest first. */
    val rideHistory: StateFlow<List<Ride>> = driverId
        .filterNotNull()
        .flatMapLatest { rideRepository.driverRideHistory(it) }
        .catch { _errorMessage.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _loadingHistory = MutableStateFlow(true)
    val loadingHistory: StateFlow<Boolean> = _loadingHistory

    /** Total fare of completed rides, used for the earnings figure. */
    val earnings: StateFlow<Double> = rideHistory
        .map { rides ->
            rides.filter { it.status == com.tpc.trikride.models.RideStatus.COMPLETED }
                .sumOf { it.estimatedFare }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    fun bind(userId: String) {
        driverId.value = userId
        viewModelScope.launch {
            kotlinx.coroutines.delay(600)
            _loadingHistory.value = false
        }
    }

    /** Streams are live already; this clears stale errors and the skeleton. */
    fun refresh() {
        _errorMessage.value = null
        _loadingHistory.value = false
    }

    fun registerDriver(licenseNumber: String, licenseExpiry: String, tricycleNumber: String) {
        val id = driverId.value ?: return
        viewModelScope.launch {
            _isRegistering.value = true
            try {
                driverRepository.registerDriver(id, licenseNumber, licenseExpiry, tricycleNumber)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Registration failed"
            } finally {
                _isRegistering.value = false
            }
        }
    }

    /**
     * Starts publishing the driver's position to the database.
     *
     * Only while online and only while a screen is collecting: the flow removes
     * its callback when this job is cancelled, so a driver who goes offline or
     * closes the app stops being tracked. There is no background service, which
     * keeps the app clear of the background-location permission and of draining
     * a battery the driver needs for the rest of their shift.
     */
    fun startPublishingLocation(context: Context) {
        val id = driverId.value ?: return
        if (locationJob?.isActive == true) return
        locationJob = viewModelScope.launch {
            LocationProvider.updates(context).collect { fix ->
                _myLocation.value = fix
                runCatching { driverRepository.updateLocation(id, fix) }
            }
        }
    }

    fun stopPublishingLocation() {
        locationJob?.cancel()
        locationJob = null
    }

    override fun onCleared() {
        super.onCleared()
        stopPublishingLocation()
    }

    fun setAvailability(isAvailable: Boolean) {
        val id = driverId.value ?: return
        viewModelScope.launch {
            try {
                driverRepository.setAvailability(id, isAvailable)
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to update availability"
            }
        }
    }

    fun acceptRequest(request: RideRequest) {
        val id = driverId.value ?: return
        viewModelScope.launch {
            try {
                rideRepository.acceptRequest(id, request)
                // Busy with a passenger, so hide from other matching until done.
                driverRepository.setAvailability(id, false)
                supportRepository.notify(
                    userId = request.passengerId,
                    title = "Driver found",
                    message = "A driver accepted your ride to ${request.dropoffLocation.address}.",
                    type = NotificationType.RIDE
                )
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to accept request"
            }
        }
    }

    /** Moves a ride to its next lifecycle stage (arriving → arrived → in progress → completed). */
    fun advanceRide(ride: Ride) {
        val next = rideRepository.nextStatus(ride.status) ?: return
        val id = driverId.value ?: return
        viewModelScope.launch {
            try {
                rideRepository.updateRideStatus(ride.id, next)
                supportRepository.notify(
                    userId = ride.passengerId,
                    title = rideNotificationTitle(next),
                    message = "Your ride to ${ride.dropoffLocation.address} was updated.",
                    type = NotificationType.RIDE
                )
                if (next == com.tpc.trikride.models.RideStatus.COMPLETED) {
                    driverRepository.setAvailability(id, true)
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to update ride"
            }
        }
    }

    fun dismissError() {
        _errorMessage.value = null
    }

    private fun rideNotificationTitle(status: com.tpc.trikride.models.RideStatus): String =
        when (status) {
            com.tpc.trikride.models.RideStatus.DRIVER_ARRIVING -> "Your driver is on the way"
            com.tpc.trikride.models.RideStatus.DRIVER_ARRIVED -> "Your driver has arrived"
            com.tpc.trikride.models.RideStatus.IN_PROGRESS -> "Your ride has started"
            com.tpc.trikride.models.RideStatus.COMPLETED -> "Ride completed"
            else -> "Ride update"
        }
}
