package com.tpc.trikride.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.tpc.trikride.models.VerificationStatus
import com.tpc.trikride.utils.LicenceImage

/**
 * Where a driver sends the photograph of their licence.
 *
 * This sits in the driver's own screens rather than in the registration form on
 * purpose. Registration stays short, and a driver on a poor connection or one
 * who declines the camera permission is not stuck part-way through creating an
 * account. The verification gate holds either way, since nobody can accept a
 * passenger until an administrator approves them.
 *
 * Nothing is sent until the consent in [ConsentToUpload] is ticked. A licence is
 * sensitive personal information under the Data Privacy Act, and burying that
 * agreement in the Terms accepted days earlier is not the same as asking.
 */
@Composable
fun LicenceUploadCard(
    status: VerificationStatus,
    hasImage: Boolean,
    imageData: String?,
    isUploading: Boolean,
    message: String?,
    onSubmit: (Uri, String) -> Unit,
    onRemove: () -> Unit,
    onDismissMessage: () -> Unit
) {
    val context = LocalContext.current
    var showConsent by remember { mutableStateOf(false) }
    var showChooser by remember { mutableStateOf(false) }
    var showRemoveConfirm by remember { mutableStateOf(false) }
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }
    // Stamped when the driver agrees, not when the write lands.
    var consentedAt by remember { mutableStateOf("") }

    val preview = remember(imageData) { LicenceImage.decode(imageData) }

    fun send(uri: Uri) = onSubmit(uri, consentedAt)

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri -> uri?.let(::send) }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        val uri = pendingCameraUri
        if (success && uri != null) send(uri)
        pendingCameraUri = null
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = newCameraUri(context)
            pendingCameraUri = uri
            cameraLauncher.launch(uri)
        }
    }

    fun openCamera() {
        val granted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        if (granted) {
            val uri = newCameraUri(context)
            pendingCameraUri = uri
            cameraLauncher.launch(uri)
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    SectionCard {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Badge,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Driver's licence",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        licenceStatusLine(status, hasImage),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (preview != null) {
                Image(
                    bitmap = preview,
                    contentDescription = "Your licence photo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
                Spacer(modifier = Modifier.height(12.dp))
            } else if (hasImage) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(modifier = Modifier.size(28.dp)) }
                Spacer(modifier = Modifier.height(12.dp))
            } else {
                Text(
                    "Photograph your licence with the number, your name and the expiry " +
                        "date all readable. An administrator checks it against what you " +
                        "typed, then approves your account.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (isUploading) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Sending…", style = MaterialTheme.typography.bodySmall)
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    PrimaryButton(
                        text = if (hasImage) "Replace photo" else "Add licence photo",
                        onClick = { showConsent = true },
                        modifier = Modifier.weight(1f)
                    )
                    if (hasImage) {
                        OutlinedButton(
                            onClick = { showRemoveConfirm = true },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.weight(1f)
                        ) { Text("Remove") }
                    }
                }
            }

            message?.let {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

    if (showConsent) {
        ConsentToUpload(
            onDismiss = { showConsent = false },
            onAgree = {
                consentedAt = System.currentTimeMillis().toString()
                showConsent = false
                showChooser = true
                onDismissMessage()
            }
        )
    }

    if (showChooser) {
        AlertDialog(
            onDismissRequest = { showChooser = false },
            title = { Text("Licence photo") },
            text = {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    ) {
                        Icon(Icons.Filled.PhotoCamera, contentDescription = null)
                        Spacer(modifier = Modifier.width(12.dp))
                        TextButton(onClick = { showChooser = false; openCamera() }) {
                            Text("Take a photo")
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    ) {
                        Icon(Icons.Filled.PhotoLibrary, contentDescription = null)
                        Spacer(modifier = Modifier.width(12.dp))
                        TextButton(onClick = {
                            showChooser = false
                            galleryLauncher.launch(
                                androidx.activity.result.PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }) { Text("Choose from gallery") }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showChooser = false }) { Text("Cancel") }
            }
        )
    }

    if (showRemoveConfirm) {
        AlertDialog(
            onDismissRequest = { showRemoveConfirm = false },
            title = { Text("Remove licence photo?") },
            text = {
                Text(
                    "The photo is deleted from our records. You cannot be approved to " +
                        "carry passengers without one, and an approved account will need " +
                        "a new photo before its next review."
                )
            },
            confirmButton = {
                TextButton(onClick = { showRemoveConfirm = false; onRemove() }) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveConfirm = false }) { Text("Keep it") }
            }
        )
    }
}

/**
 * The agreement shown immediately before the camera opens.
 *
 * Deliberately specific and deliberately here. It names what is collected, who
 * can see it, and when it is destroyed, because "you agreed to the Terms" is not
 * a meaningful answer to a driver asking why the app holds a picture of their
 * licence.
 */
@Composable
private fun ConsentToUpload(onDismiss: () -> Unit, onAgree: () -> Unit) {
    var agreed by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Before you send your licence") },
        text = {
            Column {
                Text(
                    "A driver's licence is sensitive personal information under the " +
                        "Data Privacy Act of 2012. Here is exactly what happens to it.",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(10.dp))
                ConsentPoint("It is used only to verify that you may drive, and to check it again when the licence expires.")
                ConsentPoint("Only you and a TrikRide administrator can open it. It is never shown to passengers.")
                ConsentPoint("If your application is refused, the photo is deleted at that moment.")
                ConsentPoint("You can remove it yourself at any time, and it goes with your account if you delete that.")
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(checked = agreed, onCheckedChange = { agreed = it })
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "I agree to TrikRide holding a photo of my licence on these terms.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onAgree, enabled = agreed) { Text("Continue") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun ConsentPoint(text: String) {
    Row(modifier = Modifier.padding(top = 6.dp)) {
        Text("•", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodySmall)
    }
}

private fun licenceStatusLine(status: VerificationStatus, hasImage: Boolean): String = when {
    !hasImage && status == VerificationStatus.REJECTED ->
        "Your application was refused and the previous photo was deleted."
    !hasImage -> "No photo on file yet. One is needed before you can be approved."
    status == VerificationStatus.APPROVED -> "On file and approved."
    status == VerificationStatus.EXPIRED -> "On file, but your licence needs renewing."
    else -> "On file, waiting for an administrator to review it."
}
