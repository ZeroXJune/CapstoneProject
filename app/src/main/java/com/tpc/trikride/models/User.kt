package com.tpc.trikride.models

data class User(
    val id: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val idNumber: String = "",
    val userType: UserType = UserType.PASSENGER,
    val profileImageUrl: String = "",
    val createdAt: String = "",
    val updatedAt: String = ""
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
    val documents: List<Document> = emptyList()
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

data class Document(
    val id: String = "",
    val type: DocumentType = DocumentType.LICENSE,
    val url: String = "",
    val uploadedAt: String = "",
    val expiryDate: String = ""
)

enum class DocumentType {
    LICENSE,
    IDENTIFICATION,
    INSURANCE,
    INSPECTION_CERTIFICATE
}

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
)
