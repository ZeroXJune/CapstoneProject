package com.tpc.trikride.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tpc.trikride.models.Driver
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.User
import com.tpc.trikride.repositories.AdminRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminViewModel(
    private val repo: AdminRepository = AdminRepository()
) : ViewModel() {

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    val drivers: StateFlow<List<Driver>> = repo.drivers()
        .catch { _error.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val users: StateFlow<List<User>> = repo.users()
        .catch { _error.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val rides: StateFlow<List<Ride>> = repo.rides()
        .catch { _error.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun approveDriver(driverId: String) {
        viewModelScope.launch {
            try {
                repo.approveDriver(driverId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to approve driver"
            }
        }
    }

    fun rejectDriver(driverId: String) {
        viewModelScope.launch {
            try {
                repo.rejectDriver(driverId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to reject driver"
            }
        }
    }

    fun dismissError() {
        _error.value = null
    }
}
