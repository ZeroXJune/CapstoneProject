package com.tpc.trikride.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.tpc.trikride.models.User
import com.tpc.trikride.models.UserType
import com.tpc.trikride.utils.Constants
import com.tpc.trikride.utils.ProfilePhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
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

    suspend fun login(email: String, password: String): String {
        val result = auth.signInWithEmailAndPassword(email.trim(), password).await()
        return result.user?.uid ?: error("Login failed")
    }

    suspend fun register(
        fullName: String,
        birthDate: String,
        email: String,
        phone: String,
        password: String,
        userType: UserType
    ): String {
        val result = auth.createUserWithEmailAndPassword(email.trim(), password).await()
        val uid = result.user?.uid ?: error("Registration failed")
        val user = User(
            id = uid,
            email = email.trim(),
            phoneNumber = phone,
            firstName = fullName,
            birthDate = birthDate,
            userType = userType,
            createdAt = System.currentTimeMillis().toString(),
            // The sign-up form requires the terms, privacy notice and community
            // guidelines to be ticked, so that consent is recorded here. Drivers
            // are asked for the Driver Agreement separately once their account
            // type is known.
            acceptedLegalVersion = Constants.LEGAL_VERSION,
            acceptedLegalAt = System.currentTimeMillis().toString()
        )
        database.getReference("users").child(uid).setValue(user).await()
        return uid
    }

    /** Loads the full profile record for a user. */
    suspend fun loadUser(uid: String): User? {
        val snapshot = database.getReference("users").child(uid).get().await()
        return snapshot.getValue(User::class.java)
    }

    /**
     * Shrinks the chosen image and stores it in the database as base64.
     *
     * Cloud Storage would be the usual home for this, but new Firebase projects
     * require a paid plan to provision a bucket and this one runs on the free
     * tier. Photos live under their own node so that reading a list of users
     * does not pull every avatar with it. Returns the encoded photo.
     */
    suspend fun saveProfilePhoto(uid: String, image: android.graphics.Bitmap): String {
        val encoded = withContext(Dispatchers.IO) { ProfilePhoto.encodeBitmap(image) }
            ?: error("That image could not be processed. Try a different photo.")
        database.getReference("profilePhotos").child(uid).setValue(
            mapOf(
                "data" to encoded,
                "updatedAt" to System.currentTimeMillis().toString()
            )
        ).await()
        return encoded
    }

    /**
     * What the account has already agreed to. Returned as the accepted legal
     * version and the accepted driver-agreement version, either of which is
     * blank when that consent has not been given.
     */
    suspend fun loadConsent(uid: String): Pair<String, String> {
        val ref = database.getReference("users").child(uid)
        val legal = ref.child("acceptedLegalVersion").get().await()
            .getValue(String::class.java).orEmpty()
        val driver = ref.child("acceptedDriverAgreementVersion").get().await()
            .getValue(String::class.java).orEmpty()
        return legal to driver
    }

    /** Records acceptance of the current documents against the account. */
    suspend fun recordConsent(uid: String, version: String, includeDriverAgreement: Boolean) {
        val updates = mutableMapOf<String, Any?>(
            "acceptedLegalVersion" to version,
            "acceptedLegalAt" to System.currentTimeMillis().toString()
        )
        if (includeDriverAgreement) {
            updates["acceptedDriverAgreementVersion"] = version
        }
        database.getReference("users").child(uid).updateChildren(updates).await()
    }

    /** The stored photo for a user, or an empty string when there is none. */
    suspend fun loadProfilePhoto(uid: String): String {
        val snapshot = database.getReference("profilePhotos").child(uid).child("data").get().await()
        return snapshot.getValue(String::class.java).orEmpty()
    }

    suspend fun updateProfile(uid: String, fullName: String, phone: String) {
        val updates = mapOf<String, Any?>(
            "firstName" to fullName,
            "phoneNumber" to phone,
            "updatedAt" to System.currentTimeMillis().toString()
        )
        database.getReference("users").child(uid).updateChildren(updates).await()
    }

    /**
     * Sends a password-reset email to the signed-in user's address, and returns
     * the address it went to so the caller can show it.
     *
     * Worth showing: Firebase reports success once it has accepted the request,
     * not once anything is delivered, so the address on the account is the only
     * part of the outcome the app can actually vouch for.
     */
    suspend fun sendPasswordReset(): String {
        val email = auth.currentUser?.email ?: error("No email on this account")
        auth.sendPasswordResetEmail(email).await()
        return email
    }

    /**
     * Sends a password-reset email to any address, for someone who cannot sign
     * in to ask for one.
     */
    suspend fun sendPasswordReset(email: String) {
        auth.sendPasswordResetEmail(email.trim()).await()
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
