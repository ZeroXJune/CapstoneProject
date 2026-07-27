package com.tpc.trikride.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.tpc.trikride.models.User
import com.tpc.trikride.models.UserType
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Real authentication backed by Firebase Auth (email/password). On
 * registration the user's profile — including their account type — is
 * persisted to the Realtime Database so subsequent logins route straight
 * to the correct dashboard.
 */
class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
) {
    val currentUserId: String? get() = auth.currentUser?.uid
    val isEmailVerified: Boolean get() = auth.currentUser?.isEmailVerified == true

    suspend fun login(email: String, password: String): String {
        val result = auth.signInWithEmailAndPassword(email.trim(), password).await()
        return result.user?.uid ?: error("Login failed")
    }

    suspend fun register(
        fullName: String,
        idNumber: String,
        birthDate: String,
        email: String,
        phone: String,
        password: String,
        userType: UserType
    ): String {
        val result = auth.createUserWithEmailAndPassword(email.trim(), password).await()
        val uid = result.user?.uid ?: error("Registration failed")
        // Send the email-verification link.
        runCatching { result.user?.sendEmailVerification()?.await() }
        val user = User(
            id = uid,
            email = email.trim(),
            phoneNumber = phone,
            firstName = fullName,
            idNumber = idNumber,
            birthDate = birthDate,
            userType = userType,
            createdAt = System.currentTimeMillis().toString()
        )
        database.getReference("users").child(uid).setValue(user).await()
        return uid
    }

    /** Re-sends the verification email to the signed-in user. */
    suspend fun resendVerification() {
        auth.currentUser?.sendEmailVerification()?.await()
    }

    /** Reloads the user and returns the latest verification status. */
    suspend fun refreshEmailVerified(): Boolean {
        auth.currentUser?.reload()?.await()
        return auth.currentUser?.isEmailVerified == true
    }

    /** Returns the stored account type for a user, or null if not set. */
    suspend fun loadUserType(uid: String): UserType? {
        val snapshot = database.getReference("users").child(uid).child("userType").get().await()
        val raw = snapshot.getValue(String::class.java) ?: return null
        return runCatching { UserType.valueOf(raw) }.getOrNull()
    }

    suspend fun setUserType(uid: String, userType: UserType) {
        database.getReference("users").child(uid).child("userType").setValue(userType.name).await()
    }

    fun signOut() = auth.signOut()

    /** Bridges a Google Play Services [Task] into a coroutine. */
    private suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { cont ->
        addOnSuccessListener { cont.resume(it) }
        addOnFailureListener { cont.resumeWithException(it) }
    }
}
