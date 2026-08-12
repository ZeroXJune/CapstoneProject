package com.tpc.trikride.services

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tpc.trikride.models.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseService {
    private val database = FirebaseDatabase.getInstance()

    /** Bridges a Google Play Services [Task] into a coroutine. */
    private suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { cont ->
        addOnSuccessListener { cont.resume(it) }
        addOnFailureListener { cont.resumeWithException(it) }
    }

    // User Operations
    suspend fun createUser(userId: String, user: User) {
        database.getReference("users").child(userId).setValue(user)
    }

    fun getUserFlow(userId: String): Flow<User?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                trySend(user)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        val ref = database.getReference("users").child(userId)
        ref.addValueEventListener(listener)

        awaitClose { ref.removeEventListener(listener) }
    }

    suspend fun updateUserProfile(userId: String, firstName: String, phoneNumber: String) {
        val updates = mapOf<String, Any?>(
            "firstName" to firstName,
            "phoneNumber" to phoneNumber,
            "updatedAt" to System.currentTimeMillis().toString()
        )
        database.getReference("users").child(userId).updateChildren(updates).await()
    }

    // Driver Operations
    suspend fun registerDriver(userId: String, driver: Driver) {
        database.getReference("drivers").child(userId).setValue(driver)
    }

    fun getDriverFlow(driverId: String): Flow<Driver?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val driver = snapshot.getValue(Driver::class.java)
                trySend(driver)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        val ref = database.getReference("drivers").child(driverId)
        ref.addValueEventListener(listener)

        awaitClose { ref.removeEventListener(listener) }
    }

    fun getAvailableDriversFlow(): Flow<List<Driver>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val drivers = snapshot.children.mapNotNull { it.getValue(Driver::class.java) }
                    .filter { it.isAvailable }
                trySend(drivers)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        val ref = database.getReference("drivers")
        ref.addValueEventListener(listener)

        awaitClose { ref.removeEventListener(listener) }
    }

    /** All registered drivers, regardless of availability (admin view). */
    fun getAllDriversFlow(): Flow<List<Driver>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val drivers = snapshot.children.mapNotNull { it.getValue(Driver::class.java) }
                trySend(drivers)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        val ref = database.getReference("drivers")
        ref.addValueEventListener(listener)

        awaitClose { ref.removeEventListener(listener) }
    }

    /** All users (admin view — used to resolve driver names). */
    fun getAllUsersFlow(): Flow<List<User>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.children.mapNotNull { it.getValue(User::class.java) }
                trySend(users)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        val ref = database.getReference("users")
        ref.addValueEventListener(listener)

        awaitClose { ref.removeEventListener(listener) }
    }

    /** All rides across the system (admin monitoring). */
    fun getAllRidesFlow(): Flow<List<Ride>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val rides = snapshot.children.mapNotNull { it.getValue(Ride::class.java) }
                trySend(rides)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        val ref = database.getReference("rides")
        ref.addValueEventListener(listener)

        awaitClose { ref.removeEventListener(listener) }
    }

    suspend fun updateDriverLocation(driverId: String, location: Location) {
        database.getReference("drivers").child(driverId).child("currentLocation").setValue(location)
    }

    suspend fun updateDriverAvailability(driverId: String, isAvailable: Boolean) {
        database.getReference("drivers").child(driverId).child("isAvailable").setValue(isAvailable)
    }

    suspend fun updateDriverVerification(driverId: String, status: VerificationStatus) {
        database.getReference("drivers").child(driverId).child("verificationStatus").setValue(status)
    }

    // Ride Operations
    suspend fun createRideRequest(rideRequest: RideRequest) {
        database.getReference("rideRequests").child(rideRequest.id).setValue(rideRequest)
    }

    fun getRideRequestFlow(requestId: String): Flow<RideRequest?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val request = snapshot.getValue(RideRequest::class.java)
                trySend(request)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        val ref = database.getReference("rideRequests").child(requestId)
        ref.addValueEventListener(listener)

        awaitClose { ref.removeEventListener(listener) }
    }

    fun getOpenRideRequestsFlow(): Flow<List<RideRequest>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val now = System.currentTimeMillis()
                val requests = snapshot.children.mapNotNull { it.getValue(RideRequest::class.java) }
                    .filter { (it.expiresAt.toLongOrNull() ?: Long.MAX_VALUE) > now }
                trySend(requests)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        val ref = database.getReference("rideRequests")
        ref.addValueEventListener(listener)

        awaitClose { ref.removeEventListener(listener) }
    }

    suspend fun removeRideRequest(requestId: String) {
        database.getReference("rideRequests").child(requestId).removeValue()
    }

    suspend fun createRide(ride: Ride) {
        database.getReference("rides").child(ride.id).setValue(ride)
    }

    fun getRideFlow(rideId: String): Flow<Ride?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ride = snapshot.getValue(Ride::class.java)
                trySend(ride)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        val ref = database.getReference("rides").child(rideId)
        ref.addValueEventListener(listener)

        awaitClose { ref.removeEventListener(listener) }
    }

    suspend fun updateRideStatus(rideId: String, status: RideStatus) {
        database.getReference("rides").child(rideId).child("status").setValue(status)
    }

    suspend fun updateRideRoute(rideId: String, route: List<Location>) {
        database.getReference("rides").child(rideId).child("route").setValue(route)
    }

    fun getActiveRidesFlow(passengerId: String): Flow<List<Ride>> = callbackFlow {
        val terminalStatuses = setOf(RideStatus.COMPLETED, RideStatus.CANCELLED, RideStatus.NO_SHOW)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val rides = snapshot.children.mapNotNull { it.getValue(Ride::class.java) }
                    .filter { it.passengerId == passengerId && it.status !in terminalStatuses }
                trySend(rides)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        val ref = database.getReference("rides")
        ref.addValueEventListener(listener)

        awaitClose { ref.removeEventListener(listener) }
    }

    fun getDriverActiveRidesFlow(driverId: String): Flow<List<Ride>> = callbackFlow {
        val terminalStatuses = setOf(RideStatus.COMPLETED, RideStatus.CANCELLED, RideStatus.NO_SHOW)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val rides = snapshot.children.mapNotNull { it.getValue(Ride::class.java) }
                    .filter { it.driverId == driverId && it.status !in terminalStatuses }
                trySend(rides)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        val ref = database.getReference("rides")
        ref.addValueEventListener(listener)

        awaitClose { ref.removeEventListener(listener) }
    }

    fun getPassengerRideHistoryFlow(passengerId: String): Flow<List<Ride>> = callbackFlow {
        val finished = setOf(RideStatus.COMPLETED, RideStatus.CANCELLED, RideStatus.NO_SHOW)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(
                    snapshot.children.mapNotNull { it.getValue(Ride::class.java) }
                        .filter { it.passengerId == passengerId && it.status in finished }
                        .sortedByDescending { it.requestedAt.toLongOrNull() ?: 0L }
                )
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        val ref = database.getReference("rides")
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    fun getDriverRideHistoryFlow(driverId: String): Flow<List<Ride>> = callbackFlow {
        val finished = setOf(RideStatus.COMPLETED, RideStatus.CANCELLED, RideStatus.NO_SHOW)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(
                    snapshot.children.mapNotNull { it.getValue(Ride::class.java) }
                        .filter { it.driverId == driverId && it.status in finished }
                        .sortedByDescending { it.requestedAt.toLongOrNull() ?: 0L }
                )
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        val ref = database.getReference("rides")
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    // Review Operations
    suspend fun submitReview(review: RideReview) {
        database.getReference("reviews").child(review.id).setValue(review)
    }

    fun getReviewsFlow(userId: String): Flow<List<RideReview>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reviews = snapshot.children.mapNotNull { it.getValue(RideReview::class.java) }
                    .filter { it.revieweeId == userId }
                trySend(reviews)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        val ref = database.getReference("reviews")
        ref.addValueEventListener(listener)

        awaitClose { ref.removeEventListener(listener) }
    }

    // Complaints
    suspend fun submitComplaint(complaint: Complaint) {
        database.getReference("complaints").child(complaint.id).setValue(complaint).await()
    }

    fun getAllComplaintsFlow(): Flow<List<Complaint>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.children.mapNotNull { it.getValue(Complaint::class.java) })
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        val ref = database.getReference("complaints")
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    fun getUserComplaintsFlow(userId: String): Flow<List<Complaint>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(
                    snapshot.children.mapNotNull { it.getValue(Complaint::class.java) }
                        .filter { it.reporterId == userId }
                )
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        val ref = database.getReference("complaints")
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    suspend fun updateComplaintStatus(id: String, status: ComplaintStatus, note: String) {
        val updates = mutableMapOf<String, Any?>(
            "status" to status.name,
            "adminNote" to note
        )
        if (status == ComplaintStatus.RESOLVED) {
            updates["resolvedAt"] = System.currentTimeMillis().toString()
        }
        database.getReference("complaints").child(id).updateChildren(updates).await()
    }

    // Notifications
    suspend fun pushNotification(notification: AppNotification) {
        database.getReference("notifications")
            .child(notification.userId)
            .child(notification.id)
            .setValue(notification)
            .await()
    }

    fun getNotificationsFlow(userId: String): Flow<List<AppNotification>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.children.mapNotNull { it.getValue(AppNotification::class.java) })
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        val ref = database.getReference("notifications").child(userId)
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    suspend fun markNotificationRead(userId: String, id: String) {
        database.getReference("notifications").child(userId).child(id)
            .child("read").setValue(true).await()
    }

    suspend fun markAllNotificationsRead(userId: String, ids: List<String>) {
        val ref = database.getReference("notifications").child(userId)
        ids.forEach { ref.child(it).child("read").setValue(true) }
    }

    // Fare Configuration (admin-managed pricing)
    fun getFareConfigFlow(): Flow<FareConfig> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val config = snapshot.getValue(FareConfig::class.java) ?: FareConfig()
                trySend(config)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        val ref = database.getReference("config").child("fare")
        ref.addValueEventListener(listener)

        awaitClose { ref.removeEventListener(listener) }
    }

    suspend fun getFareConfigOnce(): FareConfig {
        val snapshot = database.getReference("config").child("fare").get().await()
        return snapshot.getValue(FareConfig::class.java) ?: FareConfig()
    }

    suspend fun updateFareConfig(config: FareConfig) {
        database.getReference("config").child("fare").setValue(config).await()
    }

    // Document Upload
    suspend fun uploadDocument(driverId: String, document: Document) {
        database.getReference("drivers").child(driverId).child("documents").push()
            .setValue(document)
    }
}
