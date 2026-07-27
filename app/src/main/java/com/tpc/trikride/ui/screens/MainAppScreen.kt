package com.tpc.trikride.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tpc.trikride.R
import com.tpc.trikride.models.UserType
import com.tpc.trikride.ui.components.PrimaryButton
import com.tpc.trikride.ui.components.SectionCard
import com.tpc.trikride.ui.components.TrikTextField
import com.tpc.trikride.ui.theme.EmeraldGreen
import com.tpc.trikride.ui.theme.ForestGreen
import com.tpc.trikride.utils.AuthPrefs
import com.tpc.trikride.utils.PasswordRules
import com.tpc.trikride.viewmodels.AuthViewModel

private enum class AppScreen { LOGIN, REGISTER, ACCOUNT_SELECTION }

private data class RegistrationData(
    val fullName: String,
    val idNumber: String,
    val birthDate: String,
    val email: String,
    val phone: String,
    val password: String
)

@Composable
fun MainAppScreen(authViewModel: AuthViewModel = viewModel()) {
    val state by authViewModel.state.collectAsState()
    var screen by remember { mutableStateOf(AppScreen.LOGIN) }
    var pendingReg by remember { mutableStateOf<RegistrationData?>(null) }

    // Checking for an existing signed-in session → show the splash.
    if (state.isBootstrapping) {
        SplashScreen()
        return
    }

    // Signed in but email not verified yet → non-blocking prompt.
    var verifyDismissed by remember { mutableStateOf(false) }
    if (state.userId != null && state.userType != null && !state.emailVerified && !verifyDismissed) {
        VerifyEmailScreen(
            onResend = authViewModel::resendVerification,
            onRefresh = authViewModel::refreshVerification,
            onContinue = { verifyDismissed = true },
            onSignOut = authViewModel::signOut
        )
        return
    }

    // Signed in with a known account type → straight to the dashboard.
    if (state.userId != null && state.userType != null) {
        when (state.userType) {
            UserType.PASSENGER -> PassengerHomeScreen(
                userId = state.userId!!,
                onSignOut = authViewModel::signOut
            )
            UserType.DRIVER -> DriverHomeScreen(
                userId = state.userId!!,
                onSignOut = authViewModel::signOut
            )
            UserType.ADMIN -> AdminDashboardScreen(onSignOut = authViewModel::signOut)
            null -> Unit
        }
        return
    }

    // Signed in but no account type stored yet → pick one (persists to DB).
    if (state.userId != null && state.needsAccountType) {
        AccountSelectionScreen(
            isLoading = state.isLoading,
            error = state.error,
            onSelect = { authViewModel.chooseAccountType(it) },
            onBack = { authViewModel.signOut(); screen = AppScreen.LOGIN }
        )
        return
    }

    when (screen) {
        AppScreen.LOGIN -> LoginScreen(
            isLoading = state.isLoading,
            error = state.error,
            onLogin = { email, password -> authViewModel.login(email, password) },
            onRegisterClick = { authViewModel.clearError(); screen = AppScreen.REGISTER }
        )
        AppScreen.REGISTER -> RegisterScreen(
            isLoading = state.isLoading,
            error = state.error,
            onNext = { data -> pendingReg = data; authViewModel.clearError(); screen = AppScreen.ACCOUNT_SELECTION },
            onLoginClick = { authViewModel.clearError(); screen = AppScreen.LOGIN }
        )
        AppScreen.ACCOUNT_SELECTION -> AccountSelectionScreen(
            isLoading = state.isLoading,
            error = state.error,
            onSelect = { type ->
                val reg = pendingReg
                if (reg != null) {
                    authViewModel.register(
                        reg.fullName, reg.idNumber, reg.birthDate,
                        reg.email, reg.phone, reg.password, type
                    )
                }
            },
            onBack = { authViewModel.clearError(); screen = AppScreen.REGISTER }
        )
    }
}

@Composable
private fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(ForestGreen, EmeraldGreen))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.trikride_logo),
                    contentDescription = "TrikRide logo",
                    modifier = Modifier.size(104.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "TrikRide",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Smart Tricycle Ride Scheduling and\nDriver Onboarding System",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(28.dp))
            CircularProgressIndicator(color = Color.White, strokeWidth = 3.dp)
        }

        Text(
            text = "Talibon Polytechnic College",
            style = MaterialTheme.typography.labelLarge,
            color = Color.White.copy(alpha = 0.85f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
        )
    }
}

@Composable
private fun LoginScreen(
    isLoading: Boolean,
    error: String?,
    onLogin: (String, String) -> Unit,
    onRegisterClick: () -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf(AuthPrefs.rememberedEmail(context)) }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(email.isNotBlank()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("Login", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "Welcome back! Please login to your account",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(32.dp))

        TrikTextField(email, { email = it }, "Email", Icons.Filled.Email, keyboardType = KeyboardType.Email)
        Spacer(modifier = Modifier.height(16.dp))
        TrikTextField(password, { password = it }, "Password", Icons.Filled.Lock, isPassword = true)
        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it })
            Text("Remember me", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = { }) {
                Text("Forgot Password?", color = MaterialTheme.colorScheme.primary)
            }
        }

        ErrorText(error)
        Spacer(modifier = Modifier.height(16.dp))

        PrimaryButton(
            text = if (isLoading) "Logging in..." else "Login",
            onClick = {
                if (rememberMe) AuthPrefs.setRememberedEmail(context, email.trim())
                else AuthPrefs.clearRememberedEmail(context)
                onLogin(email, password)
            },
            enabled = !isLoading && email.isNotBlank() && password.isNotBlank()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Don't have an account? ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Register",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(onClick = onRegisterClick)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegisterScreen(
    isLoading: Boolean,
    error: String?,
    onNext: (RegistrationData) -> Unit,
    onLoginClick: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var idNumber by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var accepted by remember { mutableStateOf(false) }
    var showDoc by remember { mutableStateOf<LegalDoc?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Show a legal doc as an overlay without losing the typed form.
    showDoc?.let { doc ->
        LegalScreen(doc = doc, onBack = { showDoc = null })
        return
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { birthDate = formatBirthdate(it) }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    val passwordsMatch = password.isNotBlank() && password == confirm
    val strong = PasswordRules.isStrong(password)
    val canSubmit = !isLoading && fullName.isNotBlank() && email.isNotBlank() &&
        phone.isNotBlank() && birthDate.isNotBlank() && passwordsMatch && strong && accepted

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onLoginClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Register", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Create your account", style = MaterialTheme.typography.titleMedium)
        Text(
            "Fill in the details to get started",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))

        TrikTextField(fullName, { fullName = it }, "Full Name", Icons.Filled.Person)
        Spacer(modifier = Modifier.height(14.dp))
        TrikTextField(idNumber, { idNumber = it }, "Student ID / Driver ID", Icons.Filled.Badge)
        Spacer(modifier = Modifier.height(14.dp))
        DateField(
            label = "Birthdate",
            value = birthDate,
            onClick = { showDatePicker = true }
        )
        Spacer(modifier = Modifier.height(14.dp))
        TrikTextField(email, { email = it }, "Email", Icons.Filled.Email, keyboardType = KeyboardType.Email)
        Spacer(modifier = Modifier.height(14.dp))
        TrikTextField(phone, { phone = it }, "Phone Number", Icons.Filled.Phone, keyboardType = KeyboardType.Phone)
        Spacer(modifier = Modifier.height(14.dp))
        TrikTextField(password, { password = it }, "Password", Icons.Filled.Lock, isPassword = true)

        if (password.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            PasswordRules.evaluate(password).forEach { check ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        if (check.passed) "✓ " else "• ",
                        color = if (check.passed) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        check.label,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (check.passed) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        TrikTextField(confirm, { confirm = it }, "Confirm Password", Icons.Filled.Lock, isPassword = true)

        if (confirm.isNotBlank() && !passwordsMatch) {
            Spacer(modifier = Modifier.height(6.dp))
            Text("Passwords do not match", style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = accepted, onCheckedChange = { accepted = it })
            Column {
                Text("I agree to TrikRide's", style = MaterialTheme.typography.bodySmall)
                Row {
                    Text("Terms", style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { showDoc = LegalDoc.TERMS })
                    Text(" and ", style = MaterialTheme.typography.bodySmall)
                    Text("Privacy Policy", style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { showDoc = LegalDoc.PRIVACY })
                }
            }
        }

        ErrorText(error)
        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Continue",
            onClick = {
                onNext(
                    RegistrationData(
                        fullName.trim(), idNumber.trim(), birthDate,
                        email.trim(), phone.trim(), password
                    )
                )
            },
            enabled = canSubmit
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                "Already have an account? ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Login",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(onClick = onLoginClick)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun AccountSelectionScreen(
    isLoading: Boolean,
    error: String?,
    onSelect: (UserType) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        IconButton(onClick = onBack, enabled = !isLoading) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Choose Account Type", style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "Select the type of account you want to use",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(32.dp))

        AccountTypeCard(
            icon = Icons.Filled.DirectionsWalk,
            title = "I'm a Passenger",
            subtitle = "Book rides and travel around the campus",
            enabled = !isLoading,
            onClick = { onSelect(UserType.PASSENGER) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        AccountTypeCard(
            icon = Icons.Filled.LocalTaxi,
            title = "I'm a Driver",
            subtitle = "Provide rides and earn income",
            enabled = !isLoading,
            onClick = { onSelect(UserType.DRIVER) }
        )

        if (isLoading) {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Creating your account...", style = MaterialTheme.typography.bodyMedium)
            }
        }

        ErrorText(error)
    }
}

@Composable
private fun AccountTypeCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    SectionCard(
        modifier = Modifier.clickable(enabled = enabled, onClick = onClick),
        contentPadding = PaddingValues(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun DateField(label: String, value: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Filled.CalendarMonth,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = value.ifBlank { label },
            style = MaterialTheme.typography.bodyLarge,
            color = if (value.isBlank()) MaterialTheme.colorScheme.onSurfaceVariant
            else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun VerifyEmailScreen(
    onResend: () -> Unit,
    onRefresh: () -> Unit,
    onContinue: () -> Unit,
    onSignOut: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Filled.MarkEmailRead,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(44.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text("Verify your email", style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "We sent a verification link to your email. Please tap it, then " +
                "come back and refresh. You can also continue and verify later.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(28.dp))
        PrimaryButton(text = "I've verified — Refresh", onClick = onRefresh)
        Spacer(modifier = Modifier.height(10.dp))
        TextButton(onClick = onResend) { Text("Resend verification email") }
        Spacer(modifier = Modifier.height(4.dp))
        TextButton(onClick = onContinue) { Text("Continue for now") }
        Spacer(modifier = Modifier.height(4.dp))
        TextButton(onClick = onSignOut) {
            Text("Sign out", color = MaterialTheme.colorScheme.error)
        }
    }
}

private fun formatBirthdate(millis: Long): String {
    val sdf = java.text.SimpleDateFormat("MMM d, yyyy", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(millis))
}

@Composable
private fun ErrorText(error: String?) {
    if (error != null) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = error,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
    }
}
