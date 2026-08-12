package com.tpc.trikride.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tpc.trikride.models.FareConfig
import com.tpc.trikride.models.FareStop
import com.tpc.trikride.models.FareType
import com.tpc.trikride.models.Location
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.RideRequest
import com.tpc.trikride.repositories.FareRepository
import com.tpc.trikride.repositories.RideRepository
import com.tpc.trikride.utils.FareEngine
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

    /** Bookable destinations from the posted fare table. */
    val fareStops: StateFlow<List<FareStop>> = fareRepository.fareStops()
        .catch { _errorMessage.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Rides that have been accepted and are in progress for this passenger. */
    val activeRides: StateFlow<List<Ride>> = passengerId
        .filterNotNull()
        .flatMapLatest { rideRepository.passengerActiveRides(it) }
        .catch { _errorMessage.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Finished rides, newest first. */
    val rideHistory: StateFlow<List<Ride>> = passengerId
        .filterNotNull()
        .flatMapLatest { rideRepository.passengerRideHistory(it) }
        .catch { _errorMessage.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _loadingHistory = MutableStateFlow(true)
    val loadingHistory: StateFlow<Boolean> = _loadingHistory

    fun bind(userId: String) {
        passengerId.value = userId
        viewModelScope.launch {
            kotlinx.coroutines.delay(600)
            _loadingHistory.value = false
        }
    }

    /** Firebase streams are already live; this just clears any stale error. */
    fun refresh() {
        _errorMessage.value = null
        _loadingHistory.value = false
    }

    fun requestRide(
        pickup: Location,
        destination: FareStop,
        fareType: FareType,
        passengerCount: Int = 1,
        luggage: String = "None",
        notes: String = ""
    ) {
        val id = passengerId.value ?: return
        val quote = FareEngine.quote(fareConfig.value, destination, fareType, passengerCount)
        val dropoff = Location(address = destination.label)
        viewModelScope.launch {
            try {
                _pendingRequest.value = rideRepository.requestRide(
                    passengerId = id,
                    pickup = pickup,
                    dropoff = dropoff,
                    passengerCount = passengerCount,
                    luggage = luggage,
                    estimatedFare = quote.total,
                    fareStopId = destination.id,
                    fareType = fareType,
                    notes = notes
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
