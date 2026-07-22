package com.tpc.trikride.repositories

import com.tpc.trikride.models.Location
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.RideRequest
import com.tpc.trikride.models.RideStatus
import com.tpc.trikride.services.FirebaseService
import com.tpc.trikride.utils.Constants
import com.tpc.trikride.utils.FareCalculator
import com.tpc.trikride.utils.LocationUtils
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Coordinates the ride lifecycle between passengers and drivers:
 *
 *   Passenger requests a ride  →  request appears to available drivers
 *   Driver accepts             →  request becomes a Ride (ACCEPTED)
 *   Driver progresses status   →  ARRIVING → ARRIVED → IN_PROGRESS → COMPLETED
 */
class RideRepository(
    private val firebase: FirebaseService = FirebaseService()
) {

    // ---- Passenger side ----

    suspend fun requestRide(
        passengerId: String,
        pickup: Location,
        dropoff: Location,
        passengerCount: Int = 1,
        luggage: String = "None",
        estimatedFare: Double = 0.0,
        notes: String = ""
    ): RideRequest {
        val now = System.currentTimeMillis()
        val request = RideRequest(
            id = UUID.randomUUID().toString(),
            passengerId = passengerId,
            pickupLocation = pickup,
            dropoffLocation = dropoff,
            requestedAt = now.toString(),
            expiresAt = (now + Constants.RIDE_REQUEST_TTL_MS).toString(),
            passengerCount = passengerCount,
            luggage = luggage,
            estimatedFare = estimatedFare,
            notes = notes
        )
        firebase.createRideRequest(request)
        return request
    }

    suspend fun cancelRequest(requestId: String) {
        firebase.removeRideRequest(requestId)
    }

    fun passengerActiveRides(passengerId: String): Flow<List<Ride>> =
        firebase.getActiveRidesFlow(passengerId)

    // ---- Driver side ----

    fun openRideRequests(): Flow<List<RideRequest>> = firebase.getOpenRideRequestsFlow()

    fun driverActiveRides(driverId: String): Flow<List<Ride>> =
        firebase.getDriverActiveRidesFlow(driverId)

    /**
     * Driver accepts a request: converts it into a Ride and removes the open request
     * so other drivers no longer see it.
     */
    suspend fun acceptRequest(driverId: String, request: RideRequest): Ride {
        val distanceKm = LocationUtils.distanceKm(request.pickupLocation, request.dropoffLocation)
        val ride = Ride(
            id = UUID.randomUUID().toString(),
            passengerId = request.passengerId,
            driverId = driverId,
            pickupLocation = request.pickupLocation,
            dropoffLocation = request.dropoffLocation,
            status = RideStatus.ACCEPTED,
            requestedAt = request.requestedAt,
            acceptedAt = System.currentTimeMillis().toString(),
            estimatedDuration = FareCalculator.estimateDurationMinutes(distanceKm),
            // Use the config-priced fare the passenger already saw; fall back
            // to the distance formula only if none was supplied.
            estimatedFare = if (request.estimatedFare > 0.0) request.estimatedFare
            else FareCalculator.estimateFare(distanceKm, request.passengerCount),
            passengerCount = request.passengerCount,
            luggage = request.luggage,
            notes = request.notes
        )
        firebase.createRide(ride)
        firebase.removeRideRequest(request.id)
        return ride
    }

    suspend fun updateRideStatus(rideId: String, status: RideStatus) {
        firebase.updateRideStatus(rideId, status)
    }

    /** The natural next status in the ride lifecycle, or null if the ride is finished. */
    fun nextStatus(current: RideStatus): RideStatus? = when (current) {
        RideStatus.ACCEPTED -> RideStatus.DRIVER_ARRIVING
        RideStatus.DRIVER_ARRIVING -> RideStatus.DRIVER_ARRIVED
        RideStatus.DRIVER_ARRIVED -> RideStatus.IN_PROGRESS
        RideStatus.IN_PROGRESS -> RideStatus.COMPLETED
        else -> null
    }
}
