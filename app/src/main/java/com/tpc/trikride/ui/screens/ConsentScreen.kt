package com.tpc.trikride.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tpc.trikride.ui.components.PrimaryButton
import com.tpc.trikride.ui.components.SectionCard

/**
 * The gate between signing in and using the app.
 *
 * Every document has to be opened-able and separately ticked, and the button
 * stays disabled until all of them are. Someone who does not want to agree can
 * sign out; they cannot get past this screen without agreeing, which is the
 * whole point of it.
 */
@Composable
fun ConsentScreen(
    includeLegal: Boolean,
    includeDriverAgreement: Boolean,
    isSaving: Boolean,
    error: String?,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    val documents = remember(includeLegal, includeDriverAgreement) {
        buildList {
            if (includeLegal) {
                add(LegalDoc.TERMS)
                add(LegalDoc.PRIVACY)
                add(LegalDoc.COMMUNITY)
            }
            if (includeDriverAgreement) add(LegalDoc.DRIVER_AGREEMENT)
        }
    }
    val checked = remember(documents) { mutableStateMapOf<LegalDoc, Boolean>() }
    var reading by remember { mutableStateOf<LegalDoc?>(null) }

    reading?.let { doc ->
        LegalScreen(doc = doc, onBack = { reading = null })
        return
    }

    val allChecked = documents.all { checked[it] == true }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Icon(
            Icons.Filled.Gavel,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(44.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Before you continue",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            when {
                includeLegal && includeDriverAgreement ->
                    "Please read and agree to the following before using TrikRide. " +
                        "Drivers also accept the Driver Agreement."
                includeDriverAgreement ->
                    "One more thing before you start driving. Please read and accept " +
                        "the Driver Agreement."
                else ->
                    "Please read and agree to the following before using TrikRide."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(20.dp))

        SectionCard {
            Column {
                documents.forEachIndexed { index, doc ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = checked[doc] == true,
                            onCheckedChange = { checked[doc] = it },
                            enabled = !isSaving
                        )
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable(enabled = !isSaving) { reading = doc }
                        ) {
                            Text(
                                "I have read and agree to the",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    doc.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    Icons.AutoMirrored.Filled.OpenInNew,
                                    contentDescription = "Open ${doc.title}",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                    if (index != documents.lastIndex) HorizontalDivider()
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "Tap a document title to read it in full.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        error?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(it, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(24.dp))
        PrimaryButton(
            text = if (isSaving) "Saving..." else "I Agree and Continue",
            onClick = onAccept,
            enabled = allChecked && !isSaving
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(onClick = onDecline, enabled = !isSaving) {
                Text("Not now, sign me out")
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}
