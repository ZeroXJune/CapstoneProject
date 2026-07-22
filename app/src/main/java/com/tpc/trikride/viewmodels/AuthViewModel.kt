package com.tpc.trikride.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tpc.trikride.models.UserType
import com.tpc.trikride.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    data class AuthUiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val userId: String? = null,
        val userType: UserType? = null,
        // Authenticated but no stored account type yet (needs the picker).
        val needsAccountType: Boolean = false
    )

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val uid = repo.login(email, password)
                val type = repo.loadUserType(uid)
                _state.value = AuthUiState(
                    userId = uid,
                    userType = type,
                    needsAccountType = type == null
                )
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = friendly(e)) }
            }
        }
    }

    fun register(
        fullName: String,
        idNumber: String,
        email: String,
        phone: String,
        password: String,
        userType: UserType
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val uid = repo.register(fullName, idNumber, email, phone, password, userType)
                _state.value = AuthUiState(userId = uid, userType = userType)
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = friendly(e)) }
            }
        }
    }

    /** Used when an authenticated account has no stored type yet. */
    fun chooseAccountType(userType: UserType) {
        val uid = _state.value.userId ?: return
        viewModelScope.launch {
            try {
                repo.setUserType(uid, userType)
                _state.update { it.copy(userType = userType, needsAccountType = false) }
            } catch (e: Exception) {
                _state.update { it.copy(error = friendly(e)) }
            }
        }
    }

    fun signOut() {
        repo.signOut()
        _state.value = AuthUiState()
    }

    fun clearError() = _state.update { it.copy(error = null) }

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
