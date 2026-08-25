package com.tpc.trikride.ui.screens

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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tpc.trikride.models.UserType
import com.tpc.trikride.ui.components.PrimaryButton
import com.tpc.trikride.ui.components.SectionCard
import com.tpc.trikride.ui.components.TrikTextField
import com.tpc.trikride.utils.AuthPrefs
import com.tpc.trikride.utils.PasswordRules
import com.tpc.trikride.viewmodels.AuthViewModel
import com.tpc.trikride.viewmodels.ConsentViewModel

private enum class AppScreen { LOGIN, REGISTER, ACCOUNT_SELECTION }

/** Fills the window with the theme background for the frame before routing. */
@Composable
private fun StartupSurface() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    )
}

private data class RegistrationData(
    val fullName: String,
    val birthDate: String,
    val email: String,
    val phone: String,
    val password: String
)

@Composable
fun MainAppScreen(
    authViewModel: AuthViewModel = viewModel(),
    consentViewModel: ConsentViewModel = viewModel()
) {
    val state by authViewModel.state.collectAsState()
    val consent by consentViewModel.state.collectAsState()
    val context = LocalContext.current
    var screen by remember { mutableStateOf(AppScreen.LOGIN) }
    var pendingReg by remember { mutableStateOf<RegistrationData?>(null) }
    var seenOnboarding by remember { mutableStateOf(AuthPrefs.hasSeenOnboarding(context)) }

    val uid = state.userId
    val type = state.userType

    // Start the consent check the moment we know who is signed in, so it
    // overlaps the rest of start-up rather than following it.
    LaunchedEffect(uid, type) {
        if (uid != null && type != null) consentViewModel.check(uid, type)
    }

    // Start-up has two waits — restoring the session, then reading what the
    // account has agreed to — but they are one wait as far as anyone looking at
    // the screen is concerned.
    val settlingConsent = uid != null && type != null && consent.isChecking
    if (state.isBootstrapping || settlingConsent) {
        // Someone signed in gets the welcome artwork while that happens. With no
        // session there is nothing to wait for — the check is a synchronous read
        // — so a plain surface passes in a frame instead of holding a branded
        // screen in front of someone who just wants to get on with it.
        if (uid != null || state.hasExistingSession) WelcomeBackScreen() else StartupSurface()
        return
    }

    // First launch for a signed-out user: run the carousel once.
    if (uid == null && !seenOnboarding) {
        OnboardingScreen(
            onFinish = {
                AuthPrefs.setSeenOnboarding(context)
                seenOnboarding = true
            }
        )
        return
    }

    // Signed in with a known account type. Before the dashboard, the account
    // has to have agreed to the current legal documents — which catches both
    // accounts made before consent was tracked and any later amendment.
    if (uid != null && type != null) {
        if (consent.needsConsent) {
            ConsentScreen(
                includeLegal = consent.needsLegal,
                includeDriverAgreement = consent.needsDriverAgreement,
                isSaving = consent.isSaving,
                error = consent.error,
                onAccept = { consentViewModel.accept(uid, type) },
                onDecline = {
                    consentViewModel.reset()
                    authViewModel.signOut()
                    screen = AppScreen.LOGIN
                }
            )
            return
        }

        when (type) {
            UserType.PASSENGER -> PassengerHomeScreen(
                userId = uid,
                onSignOut = { consentViewModel.reset(); authViewModel.signOut() }
            )
            UserType.DRIVER -> DriverHomeScreen(
                userId = uid,
                onSignOut = { consentViewModel.reset(); authViewModel.signOut() }
            )
            UserType.ADMIN -> AdminDashboardScreen(
                userId = uid,
                onSignOut = { consentViewModel.reset(); authViewModel.signOut() }
            )
        }
        return
    }

    // Signed in but no account type stored yet → pick one (persists to DB).
    if (uid != null && state.needsAccountType) {
        AccountSelectionScreen(
            isLoading = state.isLoading,
            error = state.error,
            onSelect = { authViewModel.chooseAccountType(it) },
            onBack = {
                consentViewModel.reset()
                authViewModel.signOut()
                screen = AppScreen.LOGIN
            }
        )
        return
    }

    when (screen) {
        AppScreen.LOGIN -> LoginScreen(
            isLoading = state.isLoading,
            error = state.error,
            resetNotice = state.resetNotice,
            onLogin = { email, password -> authViewModel.login(email, password) },
            onForgotPassword = { authViewModel.sendPasswordReset(it) },
            onDismissResetNotice = { authViewModel.clearResetNotice() },
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
                        reg.fullName, reg.birthDate,
                        reg.email, reg.phone, reg.password, type
                    )
                }
            },
            onBack = { authViewModel.clearError(); screen = AppScreen.REGISTER }
        )
    }
}

@Composable
private fun LoginScreen(
    isLoading: Boolean,
    error: String?,
    resetNotice: String?,
    onLogin: (String, String) -> Unit,
    onForgotPassword: (String) -> Unit,
    onDismissResetNotice: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf(AuthPrefs.rememberedEmail(context)) }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(email.isNotBlank()) }
    var askingReset by remember { mutableStateOf(false) }

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
            TextButton(onClick = { askingReset = true }) {
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

    if (askingReset) {
        var resetEmail by remember { mutableStateOf(email) }
        AlertDialog(
            onDismissRequest = { askingReset = false },
            title = { Text("Reset your password") },
            text = {
                Column {
                    Text(
                        "We will email you a link to set a new one.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    TrikTextField(
                        resetEmail, { resetEmail = it }, "Email",
                        Icons.Filled.Email, keyboardType = KeyboardType.Email
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { askingReset = false; onForgotPassword(resetEmail) },
                    enabled = resetEmail.isNotBlank() && !isLoading
                ) { Text("Send link") }
            },
            dismissButton = {
                TextButton(onClick = { askingReset = false }) { Text("Cancel") }
            }
        )
    }

    if (resetNotice != null) {
        AlertDialog(
            onDismissRequest = onDismissResetNotice,
            title = { Text("Check your email") },
            text = { Text(resetNotice) },
            confirmButton = {
                TextButton(onClick = onDismissResetNotice) { Text("OK") }
            }
        )
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
        Row(verticalAlignment = Alignment.Top) {
            Checkbox(checked = accepted, onCheckedChange = { accepted = it })
            Column(modifier = Modifier.padding(top = 12.dp)) {
                Text("I have read and agree to TrikRide's",
                    style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(2.dp))
                Text(LegalDoc.TERMS.title, style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { showDoc = LegalDoc.TERMS })
                Text(LegalDoc.PRIVACY.title, style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { showDoc = LegalDoc.PRIVACY })
                Text(LegalDoc.COMMUNITY.title, style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { showDoc = LegalDoc.COMMUNITY })
                Spacer(modifier = Modifier.height(4.dp))
                Text("Tap a title to read it.", style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        ErrorText(error)
        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Continue",
            onClick = {
                onNext(
                    RegistrationData(
                        fullName.trim(), birthDate,
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
