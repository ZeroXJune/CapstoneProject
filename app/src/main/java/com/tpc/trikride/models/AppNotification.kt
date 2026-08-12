package com.tpc.trikride.models

/**
 * An in-app notification written when something happens on a ride or an
 * account. Stored per user under notifications/{userId}/{id}.
 */
data class AppNotification(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val message: String = "",
    val type: NotificationType = NotificationType.GENERAL,
    val read: Boolean = false,
    val createdAt: String = ""
)

enum class NotificationType {
    RIDE,
    COMPLAINT,
    ACCOUNT,
    GENERAL
}
