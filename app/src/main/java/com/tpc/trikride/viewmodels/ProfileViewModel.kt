package com.tpc.trikride.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tpc.trikride.models.User
import com.tpc.trikride.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Backs the shared Settings/Profile screens for passenger, driver and admin. */
class ProfileViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    data class ProfileUiState(
        val isLoading: Boolean = true,
        val isSaving: Boolean = false,
        val isUploadingPhoto: Boolean = false,
        val user: User? = null,
        /** The profile photo as base64, empty when the user has not set one. */
        val photo: String = "",
        val message: String? = null,
        val error: String? = null
    )

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state

    private var boundId: String? = null

    fun bind(userId: String) {
        if (boundId == userId && _state.value.user != null) return
        boundId = userId
        refresh()
    }

    fun refresh() {
        val uid = boundId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val user = repo.loadUser(uid)
                val photo = runCatching { repo.loadProfilePhoto(uid) }.getOrDefault("")
                _state.update { it.copy(isLoading = false, user = user, photo = photo) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message ?: "Failed to load profile") }
            }
        }
    }

    fun saveProfile(fullName: String, phone: String) {
        val uid = boundId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null, message = null) }
            try {
                repo.updateProfile(uid, fullName.trim(), phone.trim())
                val updated = _state.value.user?.copy(
                    firstName = fullName.trim(),
                    phoneNumber = phone.trim()
                )
                _state.update {
                    it.copy(isSaving = false, user = updated, message = "Profile updated")
                }
            } catch (e: Exception) {
                _state.update { it.copy(isSaving = false, error = e.message ?: "Failed to save") }
            }
        }
    }

    fun uploadPhoto(image: android.graphics.Bitmap) {
        val uid = boundId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isUploadingPhoto = true, error = null, message = null) }
            try {
                val encoded = repo.saveProfilePhoto(uid, image)
                _state.update {
                    it.copy(isUploadingPhoto = false, photo = encoded, message = "Photo updated")
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isUploadingPhoto = false,
                        error = e.message ?: "Could not save the photo"
                    )
                }
            }
        }
    }

    fun sendPasswordReset() {
        viewModelScope.launch {
            try {
                repo.sendPasswordReset()
                _state.update { it.copy(message = "Password reset email sent") }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "Failed to send reset email") }
            }
        }
    }

    fun clearMessages() {
        _state.update { it.copy(message = null, error = null) }
    }
}
