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

enum class LegalDoc(val title: String) {
    TERMS("Terms & Conditions"),
    PRIVACY("Privacy Policy"),
    COMMUNITY("Safety and Community Guidelines"),
    DRIVER_AGREEMENT("Driver Agreement");

    val body: String
        get() = when (this) {
            TERMS -> TERMS_TEXT
            PRIVACY -> PRIVACY_TEXT
            COMMUNITY -> COMMUNITY_TEXT
            DRIVER_AGREEMENT -> DRIVER_AGREEMENT_TEXT
        }
}

@Composable
fun LegalScreen(doc: LegalDoc, onBack: () -> Unit) {
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
            Text(
                doc.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Text(
                text = doc.body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

private val TERMS_TEXT = """
Terms and Conditions for TrikRide
Effective Date: July 28, 2026

By creating an account or using TrikRide, you agree to comply with these Terms and Conditions.

1. Eligibility
Users must be:
•  Registered students, faculty, or authorized personnel of the participating institution.
•  Registered and approved drivers for driver accounts.

2. Account Registration
Users agree to:
•  Provide accurate and complete information.
•  Maintain only one active account unless otherwise authorized.
•  Keep login credentials confidential.
•  Notify the administrator immediately if they suspect unauthorized access to their account.

3. Ride Booking
Passengers agree to:
•  Enter accurate pickup and destination locations.
•  Be present at the designated pickup point on time.
•  Treat drivers and fellow passengers with courtesy and respect.

Drivers agree to:
•  Maintain valid registration and any required permits.
•  Arrive at pickup locations promptly whenever possible.
•  Provide safe, respectful, and professional service.
•  Follow all applicable traffic laws and institutional policies.

4. Prohibited Activities
Users shall not:
•  Create fake or fraudulent accounts.
•  Impersonate another person.
•  Submit false booking requests.
•  Harass, threaten, or discriminate against other users.
•  Attempt unauthorized access to the system.
•  Use the application for illegal or unlawful activities.

Violations may result in temporary suspension or permanent removal from the TrikRide platform.

5. Limitation of Liability
TrikRide is a ride scheduling and driver matching platform. While we strive to provide reliable service, we cannot guarantee uninterrupted availability and are not responsible for delays caused by traffic, weather, vehicle issues, or other circumstances beyond our reasonable control.

6. Account Suspension
The system administrator reserves the right to suspend or terminate accounts found to be in violation of these Terms and Conditions.

7. Intellectual Property
All application content, including the TrikRide name, logo, interface design, graphics, source code, and documentation, is owned by the TrikRide development team unless otherwise stated. Unauthorized reproduction or distribution is prohibited.

8. Amendments
These Terms and Conditions may be updated from time to time. Continued use of TrikRide after changes are published constitutes acceptance of the updated Terms.

9. Governing Rules
These Terms shall be governed by applicable Philippine laws and the policies of the participating educational institution.

10. Acceptance
By registering and using TrikRide, you confirm that you have read, understood, and agreed to these Terms and Conditions and the Privacy Policy.
""".trimIndent()

private val COMMUNITY_TEXT = """
TrikRide Safety and Community Guidelines
Effective Date: July 28, 2026

Our Commitment
TrikRide is committed to providing a safe, respectful, and reliable transportation environment for students, drivers, faculty, and staff.

Respect Everyone
•  Treat all users with courtesy and professionalism.
•  Avoid abusive, offensive, discriminatory, or threatening language.
•  Respect personal space and privacy.

Safe Riding
•  Wait at the designated pickup location.
•  Verify the driver's identity before boarding.
•  Follow the driver's safety instructions during the trip.
•  Remain seated while the vehicle is moving.
•  Do not distract the driver while driving.

Driver Responsibilities
Drivers are expected to:
•  Drive safely and obey all traffic laws.
•  Maintain a roadworthy and clean vehicle.
•  Arrive at pickup locations as promptly as possible.
•  Treat every passenger fairly and respectfully.
•  Never operate a vehicle while under the influence of alcohol or illegal drugs.

Passenger Responsibilities
Passengers are expected to:
•  Arrive on time for scheduled pickups.
•  Respect the driver's vehicle and property.
•  Avoid behavior that may endanger others.
•  Report emergencies or unsafe situations immediately.

Prohibited Conduct
The following are strictly prohibited:
•  Violence or physical assault.
•  Sexual harassment or misconduct.
•  Bullying, intimidation, or discrimination.
•  Possession or use of illegal drugs.
•  Carrying dangerous weapons or prohibited items.
•  Vandalism or intentional damage to vehicles.
•  Providing false information or fake bookings.

Reporting Safety Concerns
Users are encouraged to report:
•  Unsafe driving.
•  Harassment or inappropriate behavior.
•  Fake accounts or fraudulent activities.
•  Vehicle safety issues.
•  Lost belongings.

Reports will be reviewed by authorized administrators, and appropriate action may be taken. Use the Support tab to file a report.

Account Enforcement
Violations of these Community Guidelines may result in:
•  Warning notices.
•  Temporary account suspension.
•  Permanent account removal.
•  Referral to school authorities or law enforcement when necessary.

By using TrikRide, all users agree to help maintain a safe, respectful, and welcoming community.
""".trimIndent()

private val DRIVER_AGREEMENT_TEXT = """
TrikRide Driver Agreement
Effective Date: July 28, 2026

This Driver Agreement establishes the responsibilities and expectations for all drivers using the TrikRide platform.

Driver Eligibility
To become a TrikRide driver, you must:
•  Be at least 18 years old.
•  Possess a valid driver's license appropriate for the vehicle operated.
•  Operate a legally registered tricycle or authorized vehicle.
•  Complete the registration and verification process required by TrikRide.

Driver Responsibilities
Drivers agree to:
•  Provide accurate personal and vehicle information.
•  Keep account information updated.
•  Drive safely and comply with all traffic laws.
•  Treat all passengers respectfully and without discrimination.
•  Arrive at pickup locations as promptly as possible.
•  Notify passengers through the app if delays occur.
•  Maintain a clean and safe vehicle.

Professional Conduct
Drivers shall:
•  Wear appropriate attire while providing transportation services.
•  Avoid abusive or inappropriate language.
•  Respect passenger privacy.
•  Never ask for personal information unrelated to the ride.

Safety Requirements
Drivers shall never:
•  Drive while under the influence of alcohol or illegal drugs.
•  Allow unauthorized persons to operate their registered vehicle.
•  Accept bookings using another driver's account.
•  Endanger passengers through reckless driving.

Account Suspension or Termination
TrikRide may suspend or terminate a driver's account for:
•  Repeated complaints.
•  Unsafe driving practices.
•  Fraudulent activity.
•  Submission of false documents.
•  Violation of this Agreement or applicable laws.

Limitation of Responsibility
Drivers acknowledge that TrikRide functions as a ride scheduling and matching platform. Drivers remain responsible for complying with all traffic regulations and for the safe operation of their vehicles.

Agreement
By registering as a TrikRide driver, you confirm that you have read, understood, and agreed to abide by this Driver Agreement.
""".trimIndent()

private val PRIVACY_TEXT = """
TrikRide Privacy Policy
Effective Date: July 28, 2026

1. Information We Collect
We collect the information you provide during registration (name, email, phone number, date of birth, and — for drivers — license and tricycle details), an optional profile photo, and information generated while using the app (ride requests, pickup/destination, ride history, and any concerns you report).

2. How We Use Information
Your information is used to create your account, match passengers with drivers, price rides, support driver verification, and improve the service.

3. Location
Location is used to show pickup/destination and, for drivers, availability. Location is only used while you are using the relevant features of the app.

4. Data Storage
Account, ride, and profile photo data are stored in Google Firebase. A profile photo is reduced to a small thumbnail before it is stored. Communications with the server are encrypted in transit. TrikRide does not collect card, bank, or any other payment details; fares are paid in cash directly to the driver.

5. Sharing
A passenger's ride details are shared with the assigned driver (and vice versa) to complete the ride. Administrators can view driver records and ride logs to operate and monitor the service. We do not sell your personal information.

6. Your Choices
You can edit your profile details and sign out at any time. You may request account concerns or corrections through the Support feature.

7. Children
The service is intended for members of the college community and is not directed at children under 13.

8. Contact
For privacy questions, contact the TrikRide support hotline listed in the app.
""".trimIndent()
