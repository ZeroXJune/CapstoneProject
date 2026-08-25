package com.tpc.trikride.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tpc.trikride.BuildConfig
import com.tpc.trikride.models.COMPLAINT_CATEGORIES
import com.tpc.trikride.models.UserType
import com.tpc.trikride.viewmodels.SupportViewModel

/**
 * The concern form, the reporter's own past reports, and the contact details.
 *
 * Shared between passengers and drivers. The Community Guidelines tell both to
 * use the Support tab, and the admin screen has always shown who filed what, so
 * the form only ever being on the passenger's side was an oversight.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportPanel(
    userId: String,
    reporterType: UserType,
    viewModel: SupportViewModel
) {
    var category by remember { mutableStateOf(COMPLAINT_CATEGORIES.first()) }
    var description by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val submitting by viewModel.submitting.collectAsState()
    val submitted by viewModel.submitted.collectAsState()
    val error by viewModel.error.collectAsState()
    val myComplaints by viewModel.myComplaints.collectAsState()

    LaunchedEffect(userId) { viewModel.bind(userId) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text("Support", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Report a concern and an administrator will review it.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(20.dp))

        SectionCard {
            Column {
                Text("Report a Concern", style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                if (submitted) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Report sent. An administrator will review it.",
                            color = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    SecondaryButton(text = "Report another", onClick = {
                        description = ""
                        viewModel.resetSubmitted()
                    })
                } else {
                    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                        OutlinedTextField(
                            value = category,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Category") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            COMPLAINT_CATEGORIES.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = { category = option; expanded = false }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Describe your concern") },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                    error?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(it, color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    PrimaryButton(
                        text = if (submitting) "Sending..." else "Submit Report",
                        onClick = {
                            viewModel.submitComplaint(
                                reporterName = "",
                                reporterType = reporterType,
                                category = category,
                                description = description.trim()
                            )
                        },
                        enabled = !submitting && description.isNotBlank()
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        if (myComplaints.isNotEmpty()) {
            Text("My Reports", style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            myComplaints.sortedByDescending { it.createdAt.toLongOrNull() ?: 0L }.forEach { c ->
                SectionCard {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(c.category, fontWeight = FontWeight.SemiBold)
                            Text(
                                c.status.name.replace('_', ' '),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(c.description, style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (c.adminNote.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Admin: ${c.adminNote}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        SectionCard {
            Column {
                Text("Contact", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                ContactRow(Icons.Filled.Phone, "Hotline", BuildConfig.SUPPORT_HOTLINE)
                Spacer(modifier = Modifier.height(8.dp))
                ContactRow(Icons.Filled.SupportAgent, "Email", BuildConfig.SUPPORT_EMAIL)
                Spacer(modifier = Modifier.height(8.dp))
                ContactRow(Icons.Filled.History, "Hours", "6:00 AM - 9:00 PM")
            }
        }
    }
}

@Composable
private fun ContactRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(12.dp))
        Text(label, style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}

