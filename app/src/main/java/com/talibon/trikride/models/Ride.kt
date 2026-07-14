package com.talibon.trikride.models

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
    val notes: String = ""
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

data class RideReview(
    val id: String = "",
    val rideId: String = "",
    val reviewerId: String = "",
    val revieweeId: String = "",
    val rating: Int = 5, // 1-5 stars
    val comment: String = "",
    val categories: Map<String, Int> = emptyMap(), // e.g., "cleanliness" -> 4
    val createdAt: String = ""
)
