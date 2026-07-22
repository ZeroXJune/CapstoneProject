package com.tpc.trikride.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.tpc.trikride.viewmodels.AuthViewModel
import kotlinx.coroutines.delay

private enum class AppScreen { SPLASH, LOGIN, REGISTER, ACCOUNT_SELECTION }

private data class RegistrationData(
    val fullName: String,
    val idNumber: String,
    val email: String,
    val phone: String,
    val password: String
)

@Composable
fun MainAppScreen(authViewModel: AuthViewModel = viewModel()) {
    val state by authViewModel.state.collectAsState()
    var screen by remember { mutableStateOf(AppScreen.SPLASH) }
    var pendingReg by remember { mutableStateOf<RegistrationData?>(null) }

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
            onSelect = { authViewModel.chooseAccountType(it) },
            onBack = { authViewModel.signOut(); screen = AppScreen.LOGIN }
        )
        return
    }

    when (screen) {
        AppScreen.SPLASH -> SplashScreen(onFinish = { screen = AppScreen.LOGIN })
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
            onSelect = { type ->
                val reg = pendingReg
                if (reg != null) {
                    authViewModel.register(reg.fullName, reg.idNumber, reg.email, reg.phone, reg.password, type)
                }
            },
            onBack = { screen = AppScreen.REGISTER }
        )
    }
}

@Composable
private fun SplashScreen(onFinish: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2200)
        onFinish()
    }

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
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher),
                    contentDescription = "TrikRide logo",
                    modifier = Modifier.size(96.dp)
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
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = { }, modifier = Modifier.align(Alignment.End)) {
            Text("Forgot Password?", color = MaterialTheme.colorScheme.primary)
        }

        ErrorText(error)
        Spacer(modifier = Modifier.height(16.dp))

        PrimaryButton(
            text = if (isLoading) "Logging in..." else "Login",
            onClick = { onLogin(email, password) },
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

@Composable
private fun RegisterScreen(
    isLoading: Boolean,
    error: String?,
    onNext: (RegistrationData) -> Unit,
    onLoginClick: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var idNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }

    val passwordsMatch = password.isNotBlank() && password == confirm
    val canSubmit = !isLoading && fullName.isNotBlank() && email.isNotBlank() &&
        phone.isNotBlank() && passwordsMatch

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
        TrikTextField(email, { email = it }, "Email", Icons.Filled.Email, keyboardType = KeyboardType.Email)
        Spacer(modifier = Modifier.height(14.dp))
        TrikTextField(phone, { phone = it }, "Phone Number", Icons.Filled.Phone, keyboardType = KeyboardType.Phone)
        Spacer(modifier = Modifier.height(14.dp))
        TrikTextField(password, { password = it }, "Password", Icons.Filled.Lock, isPassword = true)
        Spacer(modifier = Modifier.height(14.dp))
        TrikTextField(confirm, { confirm = it }, "Confirm Password", Icons.Filled.Lock, isPassword = true)

        if (confirm.isNotBlank() && !passwordsMatch) {
            Spacer(modifier = Modifier.height(6.dp))
            Text("Passwords do not match", style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error)
        }

        ErrorText(error)
        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Continue",
            onClick = {
                onNext(RegistrationData(fullName.trim(), idNumber.trim(), email.trim(), phone.trim(), password))
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
    onSelect: (UserType) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        IconButton(onClick = onBack) {
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
            onClick = { onSelect(UserType.PASSENGER) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        AccountTypeCard(
            icon = Icons.Filled.LocalTaxi,
            title = "I'm a Driver",
            subtitle = "Provide rides and earn income",
            onClick = { onSelect(UserType.DRIVER) }
        )
    }
}

@Composable
private fun AccountTypeCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    SectionCard(
        modifier = Modifier.clickable(onClick = onClick),
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
