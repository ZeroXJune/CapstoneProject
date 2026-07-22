package com.tpc.trikride.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tpc.trikride.models.FareConfig
import com.tpc.trikride.models.Location
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.RideRequest
import com.tpc.trikride.repositories.FareRepository
import com.tpc.trikride.repositories.RideRepository
import com.tpc.trikride.utils.FareEngine
import com.tpc.trikride.utils.LocationUtils
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
class PassengerViewModel(
    private val rideRepository: RideRepository = RideRepository(),
    private val fareRepository: FareRepository = FareRepository()
) : ViewModel() {

    private val passengerId = MutableStateFlow<String?>(null)

    /** The passenger's open (not yet accepted) ride request, if any. */
    private val _pendingRequest = MutableStateFlow<RideRequest?>(null)
    val pendingRequest: StateFlow<RideRequest?> = _pendingRequest

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    /** Live admin-configured pricing used for fare estimates. */
    val fareConfig: StateFlow<FareConfig> = fareRepository.fareConfig()
        .catch { _errorMessage.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), FareConfig())

    /** Rides that have been accepted and are in progress for this passenger. */
    val activeRides: StateFlow<List<Ride>> = passengerId
        .filterNotNull()
        .flatMapLatest { rideRepository.passengerActiveRides(it) }
        .catch { _errorMessage.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun bind(userId: String) {
        passengerId.value = userId
    }

    fun requestRide(
        pickup: Location,
        dropoff: Location,
        passengerCount: Int = 1,
        luggage: String = "None",
        notes: String = ""
    ) {
        val id = passengerId.value ?: return
        val distanceKm = LocationUtils.distanceKm(pickup, dropoff)
        val fare = FareEngine.total(
            fareConfig.value, pickup.address, dropoff.address, distanceKm, passengerCount
        )
        viewModelScope.launch {
            try {
                _pendingRequest.value = rideRepository.requestRide(
                    id, pickup, dropoff, passengerCount, luggage, fare, notes
                )
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to request ride"
            }
        }
    }

    fun cancelPendingRequest() {
        val request = _pendingRequest.value ?: return
        viewModelScope.launch {
            try {
                rideRepository.cancelRequest(request.id)
            } finally {
                _pendingRequest.value = null
            }
        }
    }

    /** Once a ride is active, the request has been consumed by a driver. */
    fun clearPendingRequestIfMatched() {
        if (activeRides.value.isNotEmpty()) {
            _pendingRequest.value = null
        }
    }

    fun dismissError() {
        _errorMessage.value = null
    }
}
