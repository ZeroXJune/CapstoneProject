package com.tpc.trikride.repositories

import com.tpc.trikride.models.FareType
import com.tpc.trikride.models.Location
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.RideRequest
import com.tpc.trikride.models.RideStatus
import com.tpc.trikride.services.FirebaseService
import com.tpc.trikride.utils.Constants
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
        regularCount: Int = 0,
        discountedCount: Int = 0,
        luggage: String = "None",
        estimatedFare: Double = 0.0,
        fareStopId: String = "",
        fareType: FareType = FareType.REGULAR,
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
            regularCount = regularCount,
            discountedCount = discountedCount,
            luggage = luggage,
            estimatedFare = estimatedFare,
            fareStopId = fareStopId,
            fareType = fareType,
            notes = notes
        )
        firebase.createRideRequest(request)
        return request
    }

    suspend fun cancelRequest(requestId: String) {
        firebase.removeRideRequest(requestId)
    }

    /**
     * This passenger's own request, if one is still open and unexpired.
     *
     * Read on bind so that reopening the app finds a search already running
     * rather than offering to start a second one.
     */
    suspend fun myOpenRequest(passengerId: String): RideRequest? =
        firebase.findOpenRequestFor(passengerId)

    fun passengerActiveRides(passengerId: String): Flow<List<Ride>> =
        firebase.getActiveRidesFlow(passengerId)

    fun passengerRideHistory(passengerId: String): Flow<List<Ride>> =
        firebase.getPassengerRideHistoryFlow(passengerId)

    fun driverRideHistory(driverId: String): Flow<List<Ride>> =
        firebase.getDriverRideHistoryFlow(driverId)

    // ---- Driver side ----

    fun openRideRequests(): Flow<List<RideRequest>> = firebase.getOpenRideRequestsFlow()

    fun driverActiveRides(driverId: String): Flow<List<Ride>> =
        firebase.getDriverActiveRidesFlow(driverId)

    /**
     * Driver accepts a request: converts it into a Ride and removes the open request
     * so other drivers no longer see it.
     */
    /**
     * Driver accepts a request: claims it, converts it into a Ride, and removes
     * the open request so other drivers no longer see it.
     *
     * Returns null when another driver got there first. The claim is a
     * transaction on the request node, so exactly one caller proceeds however
     * many tap Accept in the same second.
     */
    suspend fun acceptRequest(
        driverId: String,
        request: RideRequest,
        driverName: String = "",
        driverPhone: String = ""
    ): Ride? {
        if (!firebase.claimRideRequest(request.id, driverId)) return null
        // Destinations come from the posted fare table, which carries no
        // coordinates, so the ride keeps the price the passenger already agreed
        // to rather than recomputing anything from a distance.
        val ride = Ride(
            id = UUID.randomUUID().toString(),
            passengerId = request.passengerId,
            driverId = driverId,
            pickupLocation = request.pickupLocation,
            dropoffLocation = request.dropoffLocation,
            status = RideStatus.ACCEPTED,
            requestedAt = request.requestedAt,
            acceptedAt = System.currentTimeMillis().toString(),
            estimatedFare = request.estimatedFare,
            passengerCount = request.passengerCount,
            regularCount = request.regularCount,
            discountedCount = request.discountedCount,
            luggage = request.luggage,
            fareStopId = request.fareStopId,
            fareType = request.fareType,
            notes = request.notes,
            driverName = driverName,
            driverPhone = driverPhone
        )
        firebase.createRide(ride)
        firebase.removeRideRequest(request.id)
        return ride
    }

    /** Clears requests whose five minutes are up. */
    suspend fun purgeExpiredRequests() = firebase.purgeExpiredRideRequests()

    /**
     * Puts the passenger's own name and number on the ride so the driver can
     * reach them. Written by the passenger rather than copied from the request,
     * so that an open request broadcast to every approved driver never carries
     * a telephone number.
     */
    suspend fun attachPassengerContact(rideId: String, name: String, phone: String) =
        firebase.attachPassengerContact(rideId, name, phone)

    suspend fun updateRideStatus(rideId: String, status: RideStatus) {
        firebase.updateRideStatus(rideId, status)
    }

    /**
     * Records a passenger's rating of the driver who carried them.
     *
     * Written under the rater's own key, which is what lets the security rules
     * restrict it to them. The driver's visible average is not touched here —
     * the driver record is theirs to write, not a passenger's.
     */
    suspend fun rateRide(ride: Ride, stars: Int) {
        if (ride.driverId.isBlank() || ride.passengerId.isBlank()) return
        firebase.submitRating(ride.driverId, ride.id, ride.passengerId, stars.coerceIn(1, 5))
    }

    /** The natural next status in the ride lifecycle, or null if the ride is finished. */
    fun nextStatus(current: RideStatus): RideStatus? = when (current) {
        RideStatus.ACCEPTED -> RideStatus.DRIVER_ARRIVING
        RideStatus.DRIVER_ARRIVING -> RideStatus.DRIVER_ARRIVED
        RideStatus.DRIVER_ARRIVED -> RideStatus.IN_PROGRESS
        RideStatus.IN_PROGRESS -> RideStatus.COMPLETED
        else -> null
    }

    /**
     * Ends a ride that is not going to finish normally.
     *
     * The lifecycle only ever moved forwards, so a passenger who did not turn
     * up or a tricycle that broke down left a ride nobody could close: it stayed
     * in both parties' active lists for good, the driver could not reach their
     * online switch, and the passenger could not book again. CANCELLED and
     * NO_SHOW were in the enum, filtered on in six places and written by
     * nothing.
     */
    suspend fun cancelRide(rideId: String) =
        firebase.updateRideStatus(rideId, RideStatus.CANCELLED)

    /** The passenger never arrived. Only meaningful once the driver is there. */
    suspend fun markNoShow(rideId: String) =
        firebase.updateRideStatus(rideId, RideStatus.NO_SHOW)

    /** What the driver says was actually collected, which is cash and can differ. */
    suspend fun recordActualFare(rideId: String, amount: Double) =
        firebase.recordActualFare(rideId, amount)

    /**
     * Whether this ride can still be called off, and by whom.
     *
     * A passenger may withdraw until the ride is under way; after that they are
     * in the tricycle and it is between them and the driver. A driver may stop
     * at any point up to completion, because a breakdown does not wait for a
     * convenient status.
     */
    fun passengerMayCancel(status: RideStatus): Boolean = status in setOf(
        RideStatus.REQUESTED, RideStatus.SEARCHING, RideStatus.ACCEPTED,
        RideStatus.DRIVER_ARRIVING, RideStatus.DRIVER_ARRIVED
    )

    fun driverMayCancel(status: RideStatus): Boolean =
        status !in setOf(RideStatus.COMPLETED, RideStatus.CANCELLED, RideStatus.NO_SHOW)

    fun driverMayMarkNoShow(status: RideStatus): Boolean =
        status == RideStatus.DRIVER_ARRIVED
}
