package com.tpc.trikride.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tpc.trikride.models.FareConfig
import com.tpc.trikride.models.FareStop
import com.tpc.trikride.models.FareType
import com.tpc.trikride.models.Location
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.RideRequest
import com.tpc.trikride.models.Driver
import com.tpc.trikride.repositories.AuthRepository
import com.tpc.trikride.repositories.DriverRepository
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class PassengerViewModel(
    private val rideRepository: RideRepository = RideRepository(),
    private val fareRepository: FareRepository = FareRepository(),
    private val driverRepository: DriverRepository = DriverRepository(),
    private val authRepository: AuthRepository = AuthRepository()
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

    /**
     * Where the assigned driver is, while a ride is in progress.
     *
     * Null until a driver has accepted and started reporting a position. The
     * driver only publishes while their app is open and they are online, so a
     * gap here means exactly that rather than a fault.
     */
    /**
     * The assigned driver's record, for the tricycle number and the real
     * rating. Their name and telephone number are not here — those come off the
     * ride, because `users` is private and `drivers` is not.
     */
    val assignedDriver: StateFlow<Driver?> = activeRides
        .map { rides -> rides.firstOrNull()?.driverId.orEmpty() }
        .flatMapLatest { id ->
            if (id.isBlank()) flowOf(null) else driverRepository.driverProfile(id)
        }
        .catch { _errorMessage.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val driverLocation: StateFlow<Location?> = activeRides
        .map { rides -> rides.firstOrNull()?.driverId.orEmpty() }
        .flatMapLatest { id ->
            if (id.isBlank()) flowOf(null) else driverRepository.driverLocation(id)
        }
        .catch { _errorMessage.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

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
        regularCount: Int = 1,
        discountedCount: Int = 0,
        luggage: String = "None",
        notes: String = ""
    ) {
        val id = passengerId.value ?: return
        val quote = FareEngine.quote(fareConfig.value, destination, regularCount, discountedCount)
        val dropoff = Location(address = destination.label)
        // Kept for records covering a party that is all one kind; a mixed party
        // is described by the two counts, not by this.
        val fareType = if (regularCount > 0) FareType.REGULAR else FareType.DISCOUNTED
        viewModelScope.launch {
            try {
                _pendingRequest.value = rideRepository.requestRide(
                    passengerId = id,
                    pickup = pickup,
                    dropoff = dropoff,
                    passengerCount = quote.passengers,
                    regularCount = regularCount,
                    discountedCount = discountedCount,
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

    /**
     * Puts this passenger's name and number on the ride the first time it
     * appears, so the driver can call them.
     *
     * Silent on failure. A driver who cannot telephone is a smaller problem
     * than an error banner over a ride that is otherwise proceeding normally.
     */
    private fun publishContact(ride: Ride) {
        if (ride.passengerPhone.isNotBlank()) return
        val uid = passengerId.value ?: return
        viewModelScope.launch {
            runCatching {
                val user = authRepository.loadUser(uid) ?: return@runCatching
                if (user.phoneNumber.isBlank()) return@runCatching
                rideRepository.attachPassengerContact(
                    ride.id, user.firstName, user.phoneNumber
                )
            }
        }
    }

    /** Once a ride is active, the request has been consumed by a driver. */
    fun clearPendingRequestIfMatched() {
        val active = activeRides.value.firstOrNull() ?: return
        _pendingRequest.value = null
        publishContact(active)
    }

    /**
     * Rides this passenger has already rated, so the completion screen can stop
     * offering. Kept in memory only — the screen is shown once, immediately
     * after the ride, and a rating that has been written is not editable.
     */
    private val _ratedRides = MutableStateFlow<Set<String>>(emptySet())
    val ratedRides: StateFlow<Set<String>> = _ratedRides

    fun rateRide(ride: Ride, stars: Int) {
        if (ride.id in _ratedRides.value) return
        viewModelScope.launch {
            try {
                rideRepository.rateRide(ride, stars)
                _ratedRides.value = _ratedRides.value + ride.id
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Could not send your rating"
            }
        }
    }

    fun dismissError() {
        _errorMessage.value = null
    }
}
