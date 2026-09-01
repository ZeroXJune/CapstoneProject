package com.tpc.trikride.models

data class Ride(
    val id: String = "",
    val passengerId: String = "",
    val driverId: String = "",
    val pickupLocation: Location = Location(),
    val dropoffLocation: Location = Location(),
    val status: RideStatus = RideStatus.REQUESTED,
    val requestedAt: String = "",
    val acceptedAt: String = "",
    val startedAt: String = "",
    val completedAt: String = "",
    val estimatedDuration: Int = 0, // in minutes
    val actualDuration: Int = 0,
    val estimatedFare: Double = 0.0,
    val actualFare: Double = 0.0,
    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val paymentStatus: PaymentStatus = PaymentStatus.PENDING,
    val route: List<Location> = emptyList(),
    val passengerCount: Int = 1,
    val luggage: String = "None",
    // Which row of the posted fare table priced this ride, and which column.
    val fareStopId: String = "",
    val fareType: FareType = FareType.REGULAR,
    val notes: String = "",
    /**
     * Enough of each party for the other to find them, and no more.
     *
     * Names and telephone numbers live on `users/{uid}`, which the rules keep
     * private to the account and to administrators, so neither side could see
     * the other at all. Copying the two fields onto the ride shares them with
     * exactly the person on the other end of it and nobody else, which is what
     * the Privacy Policy says happens. The driver writes their own half when
     * they accept; the passenger writes theirs once the ride exists.
     */
    val driverName: String = "",
    val driverPhone: String = "",
    val passengerName: String = "",
    val passengerPhone: String = ""
)

enum class RideStatus {
    REQUESTED,
    SEARCHING,
    ACCEPTED,
    DRIVER_ARRIVING,
    DRIVER_ARRIVED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
    NO_SHOW
}

enum class PaymentMethod {
    CASH,
    CARD,
    GCash,
    PAYMAYA
}

enum class PaymentStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED,
    REFUNDED
}

data class RideRequest(
    val id: String = "",
    val passengerId: String = "",
    val pickupLocation: Location = Location(),
    val dropoffLocation: Location = Location(),
    val requestedAt: String = "",
    val expiresAt: String = "",
    val passengerCount: Int = 1,
    val luggage: String = "None",
    val estimatedFare: Double = 0.0,
    val fareStopId: String = "",
    val fareType: FareType = FareType.REGULAR,
    val notes: String = "",
    val preferredDriverId: String? = null
)

data class RideOffer(
    val id: String = "",
    val rideRequestId: String = "",
    val driverId: String = "",
    val offeredAt: String = "",
    val estimatedPickupTime: Int = 0, // in seconds
    val estimatedFare: Double = 0.0,
    val status: OfferStatus = OfferStatus.PENDING
)

enum class OfferStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    EXPIRED
}

