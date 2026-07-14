package com.talibon.trikride.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.talibon.trikride.models.UserType
import com.talibon.trikride.ui.theme.TrikRidePrimary

@Composable
fun MainAppScreen() {
    var userType by remember { mutableStateOf<UserType?>(null) }
    var isLoggedIn by remember { mutableStateOf(false) }

    when {
        !isLoggedIn -> AuthScreen(
            onLoginSuccess = { type ->
                userType = type
                isLoggedIn = true
            }
        )
        userType == UserType.PASSENGER -> PassengerHomeScreen()
        userType == UserType.DRIVER -> DriverHomeScreen()
        userType == UserType.ADMIN -> AdminDashboardScreen()
        else -> SplashScreen()
    }
}

@Composable
fun SplashScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "TrikRide",
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
            color = TrikRidePrimary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Smart Tricycle Ride System",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(32.dp))
        CircularProgressIndicator()
    }
}

@Composable
fun AuthScreen(onLoginSuccess: (UserType) -> Unit) {
    var showLoginForm by remember { mutableStateOf(true) }

    if (showLoginForm) {
        LoginScreen(
            onLogin = onLoginSuccess,
            onSignUpClick = { showLoginForm = false }
        )
    } else {
        SignUpScreen(
            onSignUp = { type ->
                onLoginSuccess(type)
            },
            onLoginClick = { showLoginForm = true }
        )
    }
}

@Composable
fun LoginScreen(
    onLogin: (UserType) -> Unit,
    onSignUpClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to TrikRide",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(32.dp))

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    onLogin(UserType.PASSENGER)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = onSignUpClick
        ) {
            Text("Don't have an account? Sign Up")
        }
    }
}

@Composable
fun SignUpScreen(
    onSignUp: (UserType) -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(32.dp))

        var selectedUserType by remember { mutableStateOf(UserType.PASSENGER) }
        var firstName by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Text("Account Type:", style = MaterialTheme.typography.titleMedium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedUserType == UserType.PASSENGER,
                onClick = { selectedUserType = UserType.PASSENGER },
                label = { Text("Passenger") }
            )
            FilterChip(
                selected = selectedUserType == UserType.DRIVER,
                onClick = { selectedUserType = UserType.DRIVER },
                label = { Text("Driver") }
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (firstName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                    onSignUp(selectedUserType)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Sign Up")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = onLoginClick
        ) {
            Text("Already have an account? Login")
        }
    }
}

@Composable
fun PassengerHomeScreen() {
    Text("Passenger Home Screen")
}

@Composable
fun DriverHomeScreen() {
    Text("Driver Home Screen")
}

@Composable
fun AdminDashboardScreen() {
    Text("Admin Dashboard")
}
