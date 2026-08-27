package com.tpc.trikride.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tpc.trikride.models.Driver
import com.tpc.trikride.models.DriverDocument
import com.tpc.trikride.models.FareConfig
import com.tpc.trikride.models.FareStop
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.User
import com.tpc.trikride.models.Complaint
import com.tpc.trikride.models.ComplaintStatus
import com.tpc.trikride.repositories.AdminRepository
import com.tpc.trikride.repositories.FareRepository
import com.tpc.trikride.models.NotificationType
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

    /**
     * Licences the administrator has opened, keyed by driver.
     *
     * Fetched one at a time when a card is expanded rather than loaded with the
     * driver list. There is no reason to pull a dozen identity documents across
     * the network so that one of them can be looked at, and every one not
     * fetched is one not sitting in memory. The number and expiry come with the
     * photograph because they live on the same protected node.
     */
    private val _licences = MutableStateFlow<Map<String, DriverDocument?>>(emptyMap())
    val licences: StateFlow<Map<String, DriverDocument?>> = _licences

    fun openLicence(driverId: String) {
        if (_licences.value.containsKey(driverId)) return
        viewModelScope.launch {
            // The key going in ahead of the value is what stops a second tap
            // from starting a second fetch.
            _licences.value = _licences.value + (driverId to null)
            val doc = runCatching { repo.licenceDocument(driverId) }.getOrNull()
            _licences.value = _licences.value + (driverId to doc)
        }
    }

    fun closeLicence(driverId: String) {
        _licences.value = _licences.value - driverId
    }

    fun approveDriver(driverId: String) {
        viewModelScope.launch {
            try {
                repo.approveDriver(driverId)
                notifyDecision(
                    driverId,
                    "Your application was approved",
                    "You can now go online and accept passengers."
                )
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to approve driver"
            }
        }
    }

    fun rejectDriver(driverId: String) {
        viewModelScope.launch {
            try {
                repo.rejectDriver(driverId)
                notifyDecision(
                    driverId,
                    "Your application was not approved",
                    "The licence photograph you submitted has been deleted. Speak to " +
                        "the administrator before applying again."
                )
                // The image is gone from the database; drop the copy held here
                // too rather than leaving a deleted document on screen.
                closeLicence(driverId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to reject driver"
            }
        }
    }

    /** Withdraws approval without destroying the licence photograph. */
    fun revokeApproval(driverId: String) {
        viewModelScope.launch {
            try {
                repo.revokeApproval(driverId)
                notifyDecision(
                    driverId,
                    "Your approval has been withdrawn",
                    "You cannot accept passengers for now. Speak to the administrator."
                )
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to revoke approval"
            }
        }
    }

    /**
     * Tells the driver what was decided.
     *
     * A failure here is deliberately not surfaced as an error: the decision it
     * describes has already been written and taken effect, and reporting the
     * notification as a failed approval would be worse than a missing message.
     */
    private suspend fun notifyDecision(driverId: String, title: String, message: String) {
        runCatching {
            supportRepo.notify(
                userId = driverId,
                title = title,
                message = message,
                type = NotificationType.ACCOUNT
            )
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
