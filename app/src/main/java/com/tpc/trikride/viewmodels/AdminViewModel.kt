package com.tpc.trikride.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tpc.trikride.models.Driver
import com.tpc.trikride.models.FareConfig
import com.tpc.trikride.models.FareStop
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.User
import com.tpc.trikride.models.Complaint
import com.tpc.trikride.models.ComplaintStatus
import com.tpc.trikride.repositories.AdminRepository
import com.tpc.trikride.repositories.FareRepository
import com.tpc.trikride.repositories.SupportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminViewModel(
    private val repo: AdminRepository = AdminRepository(),
    private val fareRepo: FareRepository = FareRepository(),
    private val supportRepo: SupportRepository = SupportRepository()
) : ViewModel() {

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _fareSaved = MutableStateFlow(false)
    val fareSaved: StateFlow<Boolean> = _fareSaved

    private val _importing = MutableStateFlow(false)
    val importing: StateFlow<Boolean> = _importing

    val fareConfig: StateFlow<FareConfig> = fareRepo.fareConfig()
        .catch { _error.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), FareConfig())

    val fareStops: StateFlow<List<FareStop>> = fareRepo.fareStops()
        .catch { _error.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val drivers: StateFlow<List<Driver>> = repo.drivers()
        .catch { _error.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val users: StateFlow<List<User>> = repo.users()
        .catch { _error.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val rides: StateFlow<List<Ride>> = repo.rides()
        .catch { _error.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val complaints: StateFlow<List<Complaint>> = supportRepo.allComplaints()
        .catch { _error.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun updateComplaint(complaint: Complaint, status: ComplaintStatus, note: String) {
        viewModelScope.launch {
            try {
                supportRepo.updateComplaint(complaint, status, note)
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to update the report"
            }
        }
    }

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

    fun saveFareConfig(config: FareConfig) {
        viewModelScope.launch {
            try {
                fareRepo.save(config)
                _fareSaved.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to save fares"
            }
        }
    }

    fun saveFareStop(stop: FareStop) {
        viewModelScope.launch {
            try {
                fareRepo.saveStop(stop)
                _fareSaved.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to save the stop"
            }
        }
    }

    fun deleteFareStop(stopId: String) {
        viewModelScope.launch {
            try {
                fareRepo.deleteStop(stopId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to remove the stop"
            }
        }
    }

    /** Writes the transcribed FeTODAT table into the database in one go. */
    fun importOfficialRates() {
        if (_importing.value) return
        viewModelScope.launch {
            _importing.value = true
            try {
                fareRepo.importOfficialRates(fareConfig.value)
                _fareSaved.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to import the fare table"
            } finally {
                _importing.value = false
            }
        }
    }

    fun acknowledgeFareSaved() {
        _fareSaved.value = false
    }

    fun dismissError() {
        _error.value = null
    }
}
