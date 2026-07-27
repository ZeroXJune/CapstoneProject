package com.tpc.trikride.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

enum class LegalDoc { TERMS, PRIVACY }

@Composable
fun LegalScreen(doc: LegalDoc, onBack: () -> Unit) {
    val title = if (doc == LegalDoc.TERMS) "Terms & Conditions" else "Privacy Policy"
    val body = if (doc == LegalDoc.TERMS) TERMS_TEXT else PRIVACY_TEXT

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

private val TERMS_TEXT = """
TrikRide — Terms & Conditions
Last updated: 2026

1. Acceptance of Terms
By creating an account or using TrikRide, you agree to these Terms & Conditions. TrikRide is a ride-scheduling platform for the Talibon Polytechnic College community that connects passengers with verified tricycle drivers.

2. Eligibility
You must provide accurate registration details. Drivers must complete onboarding and be verified by an administrator before accepting rides.

3. Use of the Service
Passengers agree to book rides in good faith and to be present at the pickup location. Drivers agree to serve accepted rides responsibly and to follow local traffic rules and the tricycle operators' association guidelines.

4. Fares
Fares are set by the administrator based on the official pricing for each route. The fare shown before booking is the amount payable. Payment is settled directly with the driver (cash); TrikRide does not process online payments.

5. Conduct
Abusive, unsafe, or fraudulent behavior by any user may result in suspension of the account. Concerns may be reported through the in-app Support/Concerns feature.

6. Limitation of Liability
TrikRide facilitates connections between passengers and drivers. It is not liable for the conduct of users, the condition of vehicles, or events occurring during a ride.

7. Changes
These terms may be updated. Continued use of the app after changes constitutes acceptance of the updated terms.

8. Contact
For questions, contact the TrikRide support hotline listed in the app.
""".trimIndent()

private val PRIVACY_TEXT = """
TrikRide — Privacy Policy
Last updated: 2026

1. Information We Collect
We collect the information you provide during registration (name, email, phone number, and — for drivers — license and tricycle details) and information generated while using the app (ride requests, pickup/destination, and ride history).

2. How We Use Information
Your information is used to create your account, match passengers with drivers, price rides, support driver verification, and improve the service.

3. Location
Location is used to show pickup/destination and, for drivers, availability. Location is only used while you are using the relevant features of the app.

4. Data Storage
Account and ride data are stored securely using Google Firebase. Communications with the server are encrypted in transit.

5. Sharing
A passenger's ride details are shared with the assigned driver (and vice versa) to complete the ride. Administrators can view driver records and ride logs to operate and monitor the service. We do not sell your personal information.

6. Your Choices
You can edit your profile details and sign out at any time. You may request account concerns or corrections through the Support feature.

7. Children
The service is intended for members of the college community and is not directed at children under 13.

8. Contact
For privacy questions, contact the TrikRide support hotline listed in the app.
""".trimIndent()
