package com.tpc.trikride.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.	Icons.AutoMirrored.Filled.DirectionsBike
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tpc.trikride.models.AppNotification
import com.tpc.trikride.models.NotificationType
import com.tpc.trikride.ui.components.RefreshableBox
import com.tpc.trikride.ui.components.SectionCard
import com.tpc.trikride.ui.components.SkeletonCard
import com.tpc.trikride.viewmodels.SupportViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotificationsScreen(
    userId: String,
    viewModel: SupportViewModel = viewModel(),
    onBack: (() -> Unit)? = null
) {
    LaunchedEffect(userId) { viewModel.bind(userId) }

    val notifications by viewModel.notifications.collectAsState()
    val loading by viewModel.loadingNotifications.collectAsState()

    val sorted = notifications.sortedByDescending { it.createdAt.toLongOrNull() ?: 0L }
    val unread = sorted.count { !it.read }

    RefreshableBox(isRefreshing = false, onRefresh = viewModel::refreshNotifications) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (onBack != null) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Notifications",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        if (unread > 0) "$unread unread" else "You are all caught up",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (unread > 0) {
                    TextButton(onClick = viewModel::markAllRead) { Text("Mark all read") }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            when {
                loading -> {
                    repeat(3) {
                        SkeletonCard(lines = 2)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
                sorted.isEmpty() -> {
                    SectionCard {
                        Column {
                            Text(
                                "Nothing here yet",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Ride updates and replies to your concerns will show up here.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                else -> sorted.forEach { item ->
                    NotificationRow(item) { viewModel.markRead(item.id) }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
private fun NotificationRow(item: AppNotification, onClick: () -> Unit) {
    SectionCard(modifier = Modifier.clickable(onClick = onClick)) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (item.type) {
                        NotificationType.RIDE -> 	Icons.AutoMirrored.Filled.DirectionsBike
                        NotificationType.COMPLAINT -> Icons.Filled.ReportProblem
                        NotificationType.ACCOUNT -> Icons.Filled.AccountCircle
                        NotificationType.GENERAL -> Icons.Filled.Notifications
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    item.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = if (item.read) FontWeight.Normal else FontWeight.Bold
                )
                Text(
                    item.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    formatWhen(item.createdAt),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (!item.read) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}

private fun formatWhen(millis: String): String {
    val value = millis.toLongOrNull() ?: return ""
    val sdf = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
    return sdf.format(Date(value))
}
