package com.tpc.trikride.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tpc.trikride.models.UserType
import com.tpc.trikride.repositories.AuthRepository
import com.tpc.trikride.utils.Constants.LEGAL_VERSION
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Decides whether a signed-in account still has to agree to the current legal
 * documents, and records the answer.
 *
 * This runs between authentication and the dashboard. An account created before
 * consent was tracked, or one that agreed to an older version of the documents,
 * is stopped here until it accepts. Drivers additionally have to accept the
 * Driver Agreement, which passengers never see.
 */
class ConsentViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    data class ConsentUiState(
        val isChecking: Boolean = true,
        /** The terms, privacy notice and community guidelines are outstanding. */
        val needsLegal: Boolean = false,
        /** The driver agreement is outstanding; only ever true for drivers. */
        val needsDriverAgreement: Boolean = false,
        val isSaving: Boolean = false,
        val error: String? = null
    ) {
        val needsConsent: Boolean get() = needsLegal || needsDriverAgreement
    }

    private val _state = MutableStateFlow(ConsentUiState())
    val state: StateFlow<ConsentUiState> = _state

    private var checkedFor: Pair<String, UserType>? = null

    fun check(userId: String, userType: UserType) {
        val key = userId to userType
        if (checkedFor == key) return
        checkedFor = key

        viewModelScope.launch {
            _state.value = ConsentUiState(isChecking = true)
            try {
                val (legal, driverAgreement) = repo.loadConsent(userId)
                _state.value = ConsentUiState(
                    isChecking = false,
                    needsLegal = legal != LEGAL_VERSION,
                    needsDriverAgreement = userType == UserType.DRIVER &&
                        driverAgreement != LEGAL_VERSION
                )
            } catch (e: Exception) {
                // If the record cannot be read we ask again rather than let the
                // user through on an assumption about what they agreed to.
                _state.value = ConsentUiState(
                    isChecking = false,
                    needsLegal = true,
                    needsDriverAgreement = userType == UserType.DRIVER,
                    error = e.message
                )
            }
        }
    }

    fun accept(userId: String, userType: UserType) {
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null) }
            try {
                repo.recordConsent(
                    uid = userId,
                    version = LEGAL_VERSION,
                    includeDriverAgreement = userType == UserType.DRIVER
                )
                _state.value = ConsentUiState(isChecking = false)
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isSaving = false,
                        error = e.message ?: "Could not record your agreement. Try again."
                    )
                }
            }
        }
    }

    /** Called on sign-out so the next account is checked from scratch. */
    fun reset() {
        checkedFor = null
        _state.value = ConsentUiState()
    }
}
