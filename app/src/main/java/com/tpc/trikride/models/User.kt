package com.tpc.trikride.models

data class User(
    val id: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val idNumber: String = "",
    val birthDate: String = "",
    val userType: UserType = UserType.PASSENGER,
    // Kept for compatibility with records written before photos moved into the
    // database; nothing writes it now.
    val profileImageUrl: String = "",
    val createdAt: String = "",
    val updatedAt: String = "",
    // Which version of the legal documents this account has accepted. Empty
    // means the account predates consent tracking and must accept before use.
    val acceptedLegalVersion: String = "",
    val acceptedLegalAt: String = "",
    /** Drivers accept an additional agreement; blank for passengers. */
    val acceptedDriverAgreementVersion: String = ""
)

enum class UserType {
    PASSENGER,
    DRIVER,
    ADMIN
}

data class Driver(
    val userId: String = "",
    val licenseNumber: String = "",
    val licenseExpiry: String = "",
    val tricycleNumber: String = "",
    val verificationStatus: VerificationStatus = VerificationStatus.PENDING,
    val isAvailable: Boolean = false,
    val currentLocation: Location? = null,
    val rating: Double = 0.0,
    val totalRides: Int = 0,
    val verifiedAt: String = "",
    /**
     * Whether a licence photograph is on file. The image itself lives under
     * `driverDocuments/{uid}`, not here — the admin screens read every driver
     * record constantly and must not pull a few hundred kilobytes of licence
     * with each one. This flag is what those screens need: enough to show
     * whether there is anything to review.
     */
    val hasLicenceImage: Boolean = false
)

data class Passenger(
    val userId: String = "",
    val savedLocations: List<SavedLocation> = emptyList(),
    val emergencyContacts: List<String> = emptyList(),
    val preferredPaymentMethod: String = ""
)

enum class VerificationStatus {
    PENDING,
    APPROVED,
    REJECTED,
    EXPIRED
}

/**
 * A photograph of a driver's licence, held for verification.
 *
 * Sensitive personal information under the Data Privacy Act of 2012, which is
 * why it is stored apart from everything else, read by nobody but the owner and
 * an administrator, and deleted when the application is refused. [consentedAt]
 * records that the driver was told what the image is for before sending it.
 */
data class DriverDocument(
    val image: String = "",
    val uploadedAt: String = "",
    val consentedAt: String = "",
    val reviewedAt: String = ""
)

data class SavedLocation(
    val id: String = "",
    val label: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String = ""
)

data class Location(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String = "",
    val timestamp: String = ""
) {
    /**
     * Whether this carries a real position. A default Location is 0,0, which is
     * a point in the Atlantic, so plotting it would send the map to the wrong
     * hemisphere rather than show nothing.
     */
    @get:com.google.firebase.database.Exclude
    val hasCoordinates: Boolean
        get() = latitude != 0.0 || longitude != 0.0
}
