package com.tpc.trikride.models

/** A concern or complaint raised by a passenger or driver, reviewed by admin. */
data class Complaint(
    val id: String = "",
    val reporterId: String = "",
    val reporterName: String = "",
    val reporterType: UserType = UserType.PASSENGER,
    val category: String = "Other",
    val description: String = "",
    val status: ComplaintStatus = ComplaintStatus.OPEN,
    val adminNote: String = "",
    val createdAt: String = "",
    val resolvedAt: String = ""
)

enum class ComplaintStatus {
    OPEN,
    IN_REVIEW,
    RESOLVED
}

/** Selectable complaint categories shown on the Support form. */
val COMPLAINT_CATEGORIES = listOf(
    "Driver behavior",
    "Passenger behavior",
    "Wrong fare",
    "Safety concern",
    "App problem",
    "Other"
)
