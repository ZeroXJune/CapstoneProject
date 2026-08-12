package com.tpc.trikride.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tpc.trikride.models.User
import com.tpc.trikride.models.UserType
import com.tpc.trikride.ui.components.AvatarPicker
import com.tpc.trikride.ui.components.PrimaryButton
import com.tpc.trikride.ui.components.SectionCard
import com.tpc.trikride.ui.components.SkeletonBox
import com.tpc.trikride.ui.components.TrikTextField
import com.tpc.trikride.ui.theme.ThemeState
import com.tpc.trikride.viewmodels.ProfileViewModel

/**
 * Shared Settings screen (profile lives inside settings, per the design).
 * [roleLabel] and [subtitle] let each account type show its own identity,
 * and [extraContent] lets a role add its own rows (e.g. driver vehicle info).
 */
@Composable
fun SettingsScreen(
    userId: String,
    userType: UserType,
    subtitle: String,
    onSignOut: () -> Unit,
    viewModel: ProfileViewModel = viewModel(),
    extraContent: @Composable () -> Unit = {}
) {
    LaunchedEffect(userId) { viewModel.bind(userId) }
    val state by viewModel.state.collectAsState()

    var editing by remember { mutableStateOf(false) }
    var showDoc by remember { mutableStateOf<LegalDoc?>(null) }

    showDoc?.let { doc ->
        LegalScreen(doc = doc, onBack = { showDoc = null })
        return
    }

    if (editing) {
        EditProfileContent(
            user = state.user,
            isSaving = state.isSaving,
            isUploadingPhoto = state.isUploadingPhoto,
            onPickPhoto = viewModel::uploadPhoto,
            onSave = { name, phone ->
                viewModel.saveProfile(name, phone)
                editing = false
            },
            onBack = { editing = false }
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text("Settings", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // Profile summary
        if (state.isLoading) {
            ProfileSkeleton()
        } else {
            SectionCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AvatarPicker(
                        imageUrl = state.user?.profileImageUrl,
                        initials = initials(state.user?.firstName),
                        isUploading = state.isUploadingPhoto,
                        onImagePicked = viewModel::uploadPhoto,
                        size = 56.dp
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            state.user?.firstName?.ifBlank { "TrikRide User" } ?: "TrikRide User",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            userType.name.lowercase().replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            state.user?.email.orEmpty().ifBlank { subtitle },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = { editing = true }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit profile")
                    }
                }
            }
        }

        state.message?.let {
            Spacer(modifier = Modifier.height(10.dp))
            Text(it, color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall)
        }
        state.error?.let {
            Spacer(modifier = Modifier.height(10.dp))
            Text(it, color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(8.dp))
        extraContent()

        GroupTitle("Account")
        SettingsRow(Icons.Filled.Person, "Edit Profile") { editing = true }
        SettingsRow(Icons.Filled.Lock, "Change Password", subtitle = "Sends a reset link to your email") {
            viewModel.sendPasswordReset()
        }

        GroupTitle("Preferences")
        val isDark = ThemeState.darkModeOverride ?: isSystemInDarkTheme()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                if (isDark) Icons.Filled.DarkMode else Icons.Filled.LightMode,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text("Dark Mode", style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f))
            Switch(checked = isDark, onCheckedChange = { ThemeState.darkModeOverride = it })
        }

        GroupTitle("Legal & About")
        SettingsRow(Icons.Filled.Description, "Terms & Conditions") { showDoc = LegalDoc.TERMS }
        SettingsRow(Icons.Filled.PrivacyTip, "Privacy Policy") { showDoc = LegalDoc.PRIVACY }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Info, contentDescription = null,
                tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(14.dp))
            Text("App Version", style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f))
            Text("1.0.0", style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = onSignOut,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Log Out")
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun EditProfileContent(
    user: User?,
    isSaving: Boolean,
    isUploadingPhoto: Boolean,
    onPickPhoto: (android.net.Uri) -> Unit,
    onSave: (String, String) -> Unit,
    onBack: () -> Unit
) {
    var name by remember(user) { mutableStateOf(user?.firstName.orEmpty()) }
    var phone by remember(user) { mutableStateOf(user?.phoneNumber.orEmpty()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text("Edit Profile", style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(20.dp))

        AvatarPicker(
            imageUrl = user?.profileImageUrl,
            initials = initials(name),
            isUploading = isUploadingPhoto,
            onImagePicked = onPickPhoto,
            size = 96.dp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Tap the photo to change it",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(20.dp))

        TrikTextField(name, { name = it }, "Full Name", Icons.Filled.Person)
        Spacer(modifier = Modifier.height(14.dp))
        TrikTextField(phone, { phone = it }, "Phone Number", Icons.Filled.Phone)
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            "Email and birthdate can't be changed here.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = if (isSaving) "Saving..." else "Save Changes",
            onClick = { onSave(name, phone) },
            enabled = !isSaving && name.isNotBlank()
        )
    }
}

@Composable
private fun GroupTitle(text: String) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    label: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.bodyLarge)
            if (subtitle != null) {
                Text(subtitle, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun ProfileSkeleton() {
    SectionCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            SkeletonBox(modifier = Modifier.size(56.dp), circle = true)
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                SkeletonBox(modifier = Modifier.fillMaxWidth(0.6f).height(16.dp))
                Spacer(modifier = Modifier.height(8.dp))
                SkeletonBox(modifier = Modifier.fillMaxWidth(0.4f).height(12.dp))
                Spacer(modifier = Modifier.height(6.dp))
                SkeletonBox(modifier = Modifier.fillMaxWidth(0.8f).height(12.dp))
            }
        }
    }
}

private fun initials(name: String?): String {
    val parts = name?.trim()?.split(" ")?.filter { it.isNotBlank() }.orEmpty()
    return when {
        parts.isEmpty() -> "TR"
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> "${parts[0].first()}${parts[1].first()}".uppercase()
    }
}
