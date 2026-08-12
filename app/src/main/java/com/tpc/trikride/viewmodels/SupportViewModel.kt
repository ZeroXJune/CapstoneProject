package com.tpc.trikride.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tpc.trikride.models.AppNotification
import com.tpc.trikride.models.Complaint
import com.tpc.trikride.models.UserType
import com.tpc.trikride.repositories.SupportRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Drives the Support form and the notifications list for the signed-in user. */
@OptIn(ExperimentalCoroutinesApi::class)
class SupportViewModel(
    private val repo: SupportRepository = SupportRepository()
) : ViewModel() {

    private val userId = MutableStateFlow<String?>(null)

    private val _submitting = MutableStateFlow(false)
    val submitting: StateFlow<Boolean> = _submitting

    private val _submitted = MutableStateFlow(false)
    val submitted: StateFlow<Boolean> = _submitted

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _loadingNotifications = MutableStateFlow(true)
    val loadingNotifications: StateFlow<Boolean> = _loadingNotifications

    val notifications: StateFlow<List<AppNotification>> = userId
        .filterNotNull()
        .flatMapLatest { repo.notifications(it) }
        .catch { _error.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val myComplaints: StateFlow<List<Complaint>> = userId
        .filterNotNull()
        .flatMapLatest { repo.myComplaints(it) }
        .catch { _error.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun bind(id: String) {
        userId.value = id
        // The flows emit almost immediately; drop the skeleton once bound.
        viewModelScope.launch {
            kotlinx.coroutines.delay(600)
            _loadingNotifications.value = false
        }
    }

    fun submitComplaint(
        reporterName: String,
        reporterType: UserType,
        category: String,
        description: String
    ) {
        val id = userId.value ?: return
        viewModelScope.launch {
            _submitting.value = true
            _error.value = null
            try {
                repo.submitComplaint(id, reporterName, reporterType, category, description)
                _submitted.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Could not submit your report"
            } finally {
                _submitting.value = false
            }
        }
    }

    fun resetSubmitted() {
        _submitted.value = false
    }

    fun markRead(notificationId: String) {
        val id = userId.value ?: return
        viewModelScope.launch {
            runCatching { repo.markRead(id, notificationId) }
        }
    }

    fun markAllRead() {
        val id = userId.value ?: return
        val unread = notifications.value.filter { !it.read }.map { it.id }
        if (unread.isEmpty()) return
        viewModelScope.launch {
            runCatching { repo.markAllRead(id, unread) }
        }
    }

    fun refreshNotifications() {
        _loadingNotifications.value = false
    }

    fun dismissError() {
        _error.value = null
    }
}
