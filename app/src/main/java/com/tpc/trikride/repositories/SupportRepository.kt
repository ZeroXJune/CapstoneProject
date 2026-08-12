package com.tpc.trikride.repositories

import com.tpc.trikride.models.AppNotification
import com.tpc.trikride.models.Complaint
import com.tpc.trikride.models.ComplaintStatus
import com.tpc.trikride.models.NotificationType
import com.tpc.trikride.models.UserType
import com.tpc.trikride.services.FirebaseService
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/** Complaints and in-app notifications. */
class SupportRepository(
    private val firebase: FirebaseService = FirebaseService()
) {

    suspend fun submitComplaint(
        reporterId: String,
        reporterName: String,
        reporterType: UserType,
        category: String,
        description: String
    ): Complaint {
        val complaint = Complaint(
            id = UUID.randomUUID().toString(),
            reporterId = reporterId,
            reporterName = reporterName,
            reporterType = reporterType,
            category = category,
            description = description,
            status = ComplaintStatus.OPEN,
            createdAt = System.currentTimeMillis().toString()
        )
        firebase.submitComplaint(complaint)
        notify(
            userId = reporterId,
            title = "Concern submitted",
            message = "We received your report about \"$category\" and an administrator will review it.",
            type = NotificationType.COMPLAINT
        )
        return complaint
    }

    fun allComplaints(): Flow<List<Complaint>> = firebase.getAllComplaintsFlow()

    fun myComplaints(userId: String): Flow<List<Complaint>> =
        firebase.getUserComplaintsFlow(userId)

    suspend fun updateComplaint(complaint: Complaint, status: ComplaintStatus, note: String) {
        firebase.updateComplaintStatus(complaint.id, status, note)
        val label = when (status) {
            ComplaintStatus.OPEN -> "reopened"
            ComplaintStatus.IN_REVIEW -> "under review"
            ComplaintStatus.RESOLVED -> "resolved"
        }
        notify(
            userId = complaint.reporterId,
            title = "Your concern is $label",
            message = note.ifBlank { "An administrator updated your report about \"${complaint.category}\"." },
            type = NotificationType.COMPLAINT
        )
    }

    // Notifications

    fun notifications(userId: String): Flow<List<AppNotification>> =
        firebase.getNotificationsFlow(userId)

    suspend fun notify(
        userId: String,
        title: String,
        message: String,
        type: NotificationType = NotificationType.GENERAL
    ) {
        if (userId.isBlank()) return
        firebase.pushNotification(
            AppNotification(
                id = UUID.randomUUID().toString(),
                userId = userId,
                title = title,
                message = message,
                type = type,
                createdAt = System.currentTimeMillis().toString()
            )
        )
    }

    suspend fun markRead(userId: String, id: String) = firebase.markNotificationRead(userId, id)

    suspend fun markAllRead(userId: String, ids: List<String>) =
        firebase.markAllNotificationsRead(userId, ids)
}
