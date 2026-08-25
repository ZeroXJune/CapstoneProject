package com.tpc.trikride.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tpc.trikride.models.UserType
import com.tpc.trikride.repositories.AuthRepository
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class AuthViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    data class AuthUiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val userId: String? = null,
        val userType: UserType? = null,
        // Authenticated but no stored account type yet (needs the picker).
        val needsAccountType: Boolean = false,
        // True while we check for an existing signed-in session on launch.
        val isBootstrapping: Boolean = true,
        // Known synchronously at startup: is someone already signed in?
        val hasExistingSession: Boolean = false,
        // Set after a password-reset email is requested from the sign-in screen.
        val resetNotice: String? = null
    )

    private val _state = MutableStateFlow(
        AuthUiState(hasExistingSession = repo.currentUserId != null)
    )
    val state: StateFlow<AuthUiState> = _state

    init {
        bootstrap()
    }

    /**
     * Firebase keeps the user signed in across app restarts, so on launch we
     * check for an existing session and, if present, restore it straight to
     * the dashboard — no re-login required.
     */
    private fun bootstrap() {
        viewModelScope.launch {
            val uid = repo.currentUserId
            if (uid == null) {
                // Nothing to restore. Go straight to onboarding or sign-in
                // rather than hold a loading screen for its own sake.
                _state.update { it.copy(isBootstrapping = false) }
                return@launch
            }
            try {
                val type = withTimeout(DB_TIMEOUT_MS) { repo.loadUserType(uid) }
                _state.value = AuthUiState(
                    isBootstrapping = false,
                    userId = uid,
                    userType = type,
                    needsAccountType = type == null
                )
            } catch (e: Exception) {
                // Session exists but we couldn't confirm the type; let them
                // re-pick (or see the DB error) rather than getting stuck.
                _state.value = AuthUiState(
                    isBootstrapping = false,
                    userId = uid,
                    needsAccountType = true
                )
            }
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val uid = withTimeout(DB_TIMEOUT_MS) { repo.login(email, password) }
                val type = withTimeout(DB_TIMEOUT_MS) { repo.loadUserType(uid) }
                _state.value = AuthUiState(
                    isBootstrapping = false,
                    userId = uid,
                    userType = type,
                    needsAccountType = type == null
                )
            } catch (e: TimeoutCancellationException) {
                _state.update { it.copy(isLoading = false, error = DB_UNREACHABLE) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = friendly(e)) }
            }
        }
    }

    fun register(
        fullName: String,
        birthDate: String,
        email: String,
        phone: String,
        password: String,
        userType: UserType
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val uid = withTimeout(DB_TIMEOUT_MS) {
                    repo.register(fullName, birthDate, email, phone, password, userType)
                }
                _state.value = AuthUiState(
                    isBootstrapping = false, userId = uid, userType = userType
                )
            } catch (e: TimeoutCancellationException) {
                _state.update { it.copy(isLoading = false, error = DB_UNREACHABLE) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = friendly(e)) }
            }
        }
    }

    /** Used when an authenticated account has no stored type yet. */
    fun chooseAccountType(userType: UserType) {
        val uid = _state.value.userId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                withTimeout(DB_TIMEOUT_MS) { repo.setUserType(uid, userType) }
                _state.update {
                    it.copy(isLoading = false, userType = userType, needsAccountType = false)
                }
            } catch (e: TimeoutCancellationException) {
                _state.update { it.copy(isLoading = false, error = DB_UNREACHABLE) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = friendly(e)) }
            }
        }
    }

    /**
     * Asks Firebase to email a reset link.
     *
     * The same message comes back whether or not the address has an account.
     * Firebase reports "no user record" for an unknown one, and repeating that
     * turns the sign-in screen into a way of finding out who is registered.
     */
    fun sendPasswordReset(email: String) {
        if (email.isBlank()) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, resetNotice = null) }
            val notice = "If ${email.trim()} has an account, a reset link is on its way. " +
                "Check the spam folder if it does not arrive."
            try {
                withTimeout(DB_TIMEOUT_MS) { repo.sendPasswordReset(email) }
                _state.update { it.copy(isLoading = false, resetNotice = notice) }
            } catch (e: TimeoutCancellationException) {
                _state.update { it.copy(isLoading = false, error = DB_UNREACHABLE) }
            } catch (e: Exception) {
                val msg = e.message.orEmpty()
                if (msg.contains("no user record", ignoreCase = true)) {
                    _state.update { it.copy(isLoading = false, resetNotice = notice) }
                } else {
                    _state.update { it.copy(isLoading = false, error = friendly(e)) }
                }
            }
        }
    }

    fun signOut() {
        repo.signOut()
        _state.value = AuthUiState(isBootstrapping = false)
    }

    fun clearError() = _state.update { it.copy(error = null) }

    fun clearResetNotice() = _state.update { it.copy(resetNotice = null) }

    private companion object {
        const val DB_TIMEOUT_MS = 12_000L
        const val DB_UNREACHABLE =
            "Couldn't reach the database. Make sure the Realtime Database is created in " +
                "Firebase, then re-download google-services.json and replace it in the app/ " +
                "folder. (If you just did, check the database Rules allow writes.)"
    }

    private fun friendly(e: Exception): String {
        val msg = e.message ?: return "Something went wrong. Please try again."
        return when {
            msg.contains("password is invalid", ignoreCase = true) ||
                msg.contains("credential is incorrect", ignoreCase = true) ->
                "Incorrect email or password."
            msg.contains("no user record", ignoreCase = true) ->
                "No account found with that email."
            msg.contains("email address is already in use", ignoreCase = true) ->
                "That email is already registered."
            msg.contains("badly formatted", ignoreCase = true) ->
                "Please enter a valid email address."
            msg.contains("at least 6 characters", ignoreCase = true) ->
                "Password must be at least 6 characters."
            msg.contains("network error", ignoreCase = true) ->
                "Network error. Check your connection and try again."
            else -> msg
        }
    }
}
