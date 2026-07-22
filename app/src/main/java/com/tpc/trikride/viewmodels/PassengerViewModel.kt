package com.tpc.trikride.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tpc.trikride.models.Location
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.RideRequest
import com.tpc.trikride.repositories.RideRepository
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
    private val rideRepository: RideRepository = RideRepository()
) : ViewModel() {

    private val passengerId = MutableStateFlow<String?>(null)

    /** The passenger's open (not yet accepted) ride request, if any. */
    private val _pendingRequest = MutableStateFlow<RideRequest?>(null)
    val pendingRequest: StateFlow<RideRequest?> = _pendingRequest

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    /** Rides that have been accepted and are in progress for this passenger. */
    val activeRides: StateFlow<List<Ride>> = passengerId
        .filterNotNull()
        .flatMapLatest { rideRepository.passengerActiveRides(it) }
        .catch { _errorMessage.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun bind(userId: String) {
        passengerId.value = userId
    }

    fun requestRide(pickup: Location, dropoff: Location, notes: String = "") {
        val id = passengerId.value ?: return
        viewModelScope.launch {
            try {
                _pendingRequest.value = rideRepository.requestRide(id, pickup, dropoff, notes)
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
