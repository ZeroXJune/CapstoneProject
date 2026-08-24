package com.tpc.trikride.services

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
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

    suspend fun updateRideStatus(rideId: String, status: RideStatus) {
        database.getReference("rides").child(rideId).child("status").setValue(status)
    }

    fun getActiveRidesFlow(passengerId: String): Flow<List<Ride>> = callbackFlow {
        val terminalStatuses = setOf(RideStatus.COMPLETED, RideStatus.CANCELLED, RideStatus.NO_SHOW)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val rides = snapshot.children.mapNotNull { it.getValue(Ride::class.java) }
                    .filter { it.status !in terminalStatuses }
                trySend(rides)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        // Scoped at the source rather than after the download: the rule
        // on /rides requires this exact constraint, so the client
        // cannot ask for anyone else's rides.
        val ref = database.getReference("rides")
            .orderByChild("passengerId").equalTo(passengerId)
        ref.addValueEventListener(listener)

        awaitClose { ref.removeEventListener(listener) }
    }

    fun getDriverActiveRidesFlow(driverId: String): Flow<List<Ride>> = callbackFlow {
        val terminalStatuses = setOf(RideStatus.COMPLETED, RideStatus.CANCELLED, RideStatus.NO_SHOW)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val rides = snapshot.children.mapNotNull { it.getValue(Ride::class.java) }
                    .filter { it.status !in terminalStatuses }
                trySend(rides)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        // Scoped at the source rather than after the download: the rule
        // on /rides requires this exact constraint, so the client
        // cannot ask for anyone else's rides.
        val ref = database.getReference("rides")
            .orderByChild("driverId").equalTo(driverId)
        ref.addValueEventListener(listener)

        awaitClose { ref.removeEventListener(listener) }
    }

    fun getPassengerRideHistoryFlow(passengerId: String): Flow<List<Ride>> = callbackFlow {
        val finished = setOf(RideStatus.COMPLETED, RideStatus.CANCELLED, RideStatus.NO_SHOW)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(
                    snapshot.children.mapNotNull { it.getValue(Ride::class.java) }
                        .filter { it.status in finished }
                        .sortedByDescending { it.requestedAt.toLongOrNull() ?: 0L }
                )
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        // Scoped at the source rather than after the download: the rule
        // on /rides requires this exact constraint, so the client
        // cannot ask for anyone else's rides.
        val ref = database.getReference("rides")
            .orderByChild("passengerId").equalTo(passengerId)
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    fun getDriverRideHistoryFlow(driverId: String): Flow<List<Ride>> = callbackFlow {
        val finished = setOf(RideStatus.COMPLETED, RideStatus.CANCELLED, RideStatus.NO_SHOW)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(
                    snapshot.children.mapNotNull { it.getValue(Ride::class.java) }
                        .filter { it.status in finished }
                        .sortedByDescending { it.requestedAt.toLongOrNull() ?: 0L }
                )
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        // Scoped at the source rather than after the download: the rule
        // on /rides requires this exact constraint, so the client
        // cannot ask for anyone else's rides.
        val ref = database.getReference("rides")
            .orderByChild("driverId").equalTo(driverId)
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    // Ratings
    //
    // A rating is written by the passenger to `driverRatings/{driver}/{rater}`,
    // one value per passenger per driver, because that is a path the security
    // rules can scope to the person writing it. The driver record itself is
    // writable only by the driver, so a passenger cannot be the one to update
    // the average there — see `publishRating`.

    suspend fun submitRating(driverId: String, raterId: String, stars: Int) {
        database.getReference("driverRatings").child(driverId).child(raterId)
            .setValue(stars).await()
    }

    /** Every rating a driver has been given. */
    fun getRatingsFlow(driverId: String): Flow<List<Int>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.children.mapNotNull { it.getValue(Int::class.java) })
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        val ref = database.getReference("driverRatings").child(driverId)
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    /**
     * Caches a driver's average onto their own record.
     *
     * Called from the driver's device, because only they may write there. The
     * admin screens and the exported reports read the cached figure rather than
     * averaging every rating in the database on every list refresh.
     */
    suspend fun publishRating(driverId: String, average: Double, count: Int) {
        database.getReference("drivers").child(driverId).updateChildren(
            mapOf("rating" to average, "ratingCount" to count)
        ).await()
    }

    /** Counts one more finished ride against the driver, for their totals. */
    suspend fun recordCompletedRide(driverId: String): Unit =
        suspendCancellableCoroutine { cont ->
            database.getReference("drivers").child(driverId)
                .runTransaction(object : Transaction.Handler {
                    override fun doTransaction(current: MutableData): Transaction.Result {
                        val total = current.child("totalRides").getValue(Int::class.java) ?: 0
                        current.child("totalRides").value = total + 1
                        return Transaction.success(current)
                    }

                    override fun onComplete(
                        error: DatabaseError?,
                        committed: Boolean,
                        snapshot: DataSnapshot?
                    ) {
                        if (cont.isActive) {
                            if (error != null) cont.resumeWithException(error.toException())
                            else cont.resume(Unit)
                        }
                    }
                })
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
                trySend(snapshot.children.mapNotNull { it.getValue(Complaint::class.java) })
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        // Scoped at the source: the rule on /complaints requires this
        // constraint, so nobody can read what somebody else reported.
        val ref = database.getReference("complaints")
            .orderByChild("reporterId").equalTo(userId)
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

    suspend fun updateFareConfig(config: FareConfig) {
        database.getReference("config").child("fare").setValue(config).await()
    }

    // Fare Stops (the posted per-destination rate table)
    fun getFareStopsFlow(): Flow<List<FareStop>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val stops = snapshot.children.mapNotNull { it.getValue(FareStop::class.java) }
                trySend(stops)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        val ref = database.getReference("config").child("fareStops")
        ref.addValueEventListener(listener)

        awaitClose { ref.removeEventListener(listener) }
    }

    suspend fun saveFareStop(stop: FareStop) {
        database.getReference("config").child("fareStops").child(stop.id).setValue(stop).await()
    }

    suspend fun deleteFareStop(stopId: String) {
        database.getReference("config").child("fareStops").child(stopId).removeValue().await()
    }

    /**
     * Writes the whole rate table in one request. Existing stops with the same
     * id are overwritten; ones the admin added by hand are left alone.
     */
    suspend fun importFareStops(stops: List<FareStop>) {
        val updates = stops.associate { it.id to it as Any? }
        database.getReference("config").child("fareStops").updateChildren(updates).await()
    }

    // Driver documents
    //
    // The licence photograph lives in its own top-level node, away from the
    // driver record, so that listing drivers does not pull every image with it.
    // The `hasLicenceImage` flag on the driver is written alongside, because
    // that is what the admin list needs in order to say whether there is
    // anything to look at.

    /**
     * Records the licence number and expiry a driver typed at registration.
     *
     * These live beside the photograph rather than on the driver record: a
     * licence number is a government-issued identifier, and `drivers/{uid}` is
     * readable by every signed-in account because passengers need the
     * availability and position held there.
     */
    suspend fun saveLicenceDetails(driverId: String, number: String, expiry: String) {
        database.getReference("driverDocuments").child(driverId).child("licence")
            .updateChildren(mapOf("licenceNumber" to number, "licenceExpiry" to expiry))
            .await()
    }

    /**
     * Stores the photograph, leaving the number and expiry as they are.
     *
     * updateChildren rather than setValue, or sending a photograph would erase
     * the details the driver typed when they registered.
     */
    suspend fun saveLicenceImage(driverId: String, image: String, uploadedAt: String, consentedAt: String) {
        database.getReference("driverDocuments").child(driverId).child("licence")
            .updateChildren(
                mapOf(
                    "image" to image,
                    "uploadedAt" to uploadedAt,
                    "consentedAt" to consentedAt
                )
            ).await()
        database.getReference("drivers").child(driverId).child("hasLicenceImage")
            .setValue(true).await()
    }

    suspend fun getLicenceImage(driverId: String): DriverDocument? =
        database.getReference("driverDocuments").child(driverId).child("licence")
            .get().await().getValue(DriverDocument::class.java)

    /**
     * Removes a driver's licence photograph.
     *
     * Called when an application is refused and when an account is deleted. The
     * flag is cleared in the same breath, or the admin list would keep offering
     * a document that is no longer there.
     */
    /**
     * Destroys the photograph and nothing else.
     *
     * The number and expiry stay: they are what the driver typed and what an
     * administrator compared the photograph against, and a refusal that erased
     * them would leave no record of what was refused. Removing the whole node
     * here would also take them, which is why the children are named.
     */
    suspend fun deleteLicenceImage(driverId: String) {
        database.getReference("driverDocuments").child(driverId).child("licence")
            .updateChildren(
                mapOf<String, Any?>(
                    "image" to null,
                    "uploadedAt" to null,
                    "consentedAt" to null
                )
            ).await()
        database.getReference("drivers").child(driverId).child("hasLicenceImage")
            .setValue(false).await()
    }
}
