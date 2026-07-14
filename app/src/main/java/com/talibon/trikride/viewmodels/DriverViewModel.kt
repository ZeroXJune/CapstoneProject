package com.talibon.trikride.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talibon.trikride.models.Driver
import com.talibon.trikride.models.Ride
import com.talibon.trikride.models.RideRequest
import com.talibon.trikride.repositories.DriverRepository
import com.talibon.trikride.repositories.RideRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class DriverViewModel(
    private val driverRepository: DriverRepository = DriverRepository(),
    private val rideRepository: RideRepository = RideRepository()
) : ViewModel() {

    private val driverId = MutableStateFlow<String?>(null)

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

    fun bind(userId: String) {
        driverId.value = userId
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
                // Busy with a passenger — hide from other matching until done.
                driverRepository.setAvailability(id, false)
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
                if (next == com.talibon.trikride.models.RideStatus.COMPLETED) {
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
}
