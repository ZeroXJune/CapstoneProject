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
        database.getReference("drivers").child(userId).setValue(driver).await()
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
        database.getReference("drivers").child(driverId).child("currentLocation").setValue(location).await()
    }

    suspend fun updateDriverAvailability(driverId: String, isAvailable: Boolean) {
        database.getReference("drivers").child(driverId).child("isAvailable").setValue(isAvailable).await()
    }

    suspend fun updateDriverVerification(driverId: String, status: VerificationStatus) {
        database.getReference("drivers").child(driverId).child("verificationStatus").setValue(status).await()
    }

    // Ride Operations
    suspend fun createRideRequest(rideRequest: RideRequest) {
        database.getReference("rideRequests").child(rideRequest.id).setValue(rideRequest).await()
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
        database.getReference("rideRequests").child(requestId).removeValue().await()
    }

    /**
     * Claims an open request for one driver, and says whether the claim won.
     *
     * Accepting used to be "write a ride, then delete the request" — two
     * independent writes with nothing between them, so two drivers who tapped
     * Accept before either write landed both created a ride for one booking and
     * the passenger tracked whichever the listener handed back first. The
     * transaction is the resolution: the first caller writes `claimedBy` and
     * every later one aborts, so exactly one driver goes on to create the ride.
     */
    suspend fun claimRideRequest(requestId: String, driverId: String): Boolean =
        suspendCancellableCoroutine { cont ->
            database.getReference("rideRequests").child(requestId)
                .runTransaction(object : Transaction.Handler {
                    override fun doTransaction(current: MutableData): Transaction.Result {
                        // Gone already: another driver accepted and cleaned up.
                        if (current.getValue() == null) return Transaction.abort()
                        val claimed = current.child("claimedBy").getValue(String::class.java)
                        if (!claimed.isNullOrBlank() && claimed != driverId) {
                            return Transaction.abort()
                        }
                        current.child("claimedBy").value = driverId
                        return Transaction.success(current)
                    }

                    override fun onComplete(
                        error: DatabaseError?,
                        committed: Boolean,
                        snapshot: DataSnapshot?
                    ) {
                        if (!cont.isActive) return
                        if (error != null) cont.resumeWithException(error.toException())
                        else cont.resume(committed)
                    }
                })
        }

    /**
     * Deletes requests that have run out of time.
     *
     * Nothing removed them before: the open-requests flow filtered expired ones
     * after downloading them, so every request ever made stayed in the node and
     * every approved driver pulled the lot — pickup coordinates and free-text
     * notes included — on every change. A passenger who force-closed the app
     * mid-search left theirs there permanently.
     */
    /** The one unexpired request this passenger has open, if any. */
    suspend fun findOpenRequestFor(
        passengerId: String,
        now: Long = System.currentTimeMillis()
    ): RideRequest? {
        val snapshot = database.getReference("rideRequests")
            .orderByChild("passengerId").equalTo(passengerId).get().await()
        return snapshot.children
            .mapNotNull { it.getValue(RideRequest::class.java) }
            .firstOrNull { (it.expiresAt.toLongOrNull() ?: 0L) > now }
    }

    suspend fun purgeExpiredRideRequests(now: Long = System.currentTimeMillis()) {
        val root = database.getReference("rideRequests")
        val snapshot = root.get().await()
        val dead = snapshot.children.mapNotNull { child ->
            val request = child.getValue(RideRequest::class.java) ?: return@mapNotNull null
            child.key?.takeIf { (request.expiresAt.toLongOrNull() ?: Long.MAX_VALUE) <= now }
        }
        if (dead.isEmpty()) return
        root.updateChildren(dead.associateWith { null as Any? }).await()
    }

    suspend fun createRide(ride: Ride) {
        database.getReference("rides").child(ride.id).setValue(ride).await()
    }

    /**
     * Moves a ride to [status] and stamps the timestamp that goes with it.
     *
     * `startedAt` and `completedAt` are on the record and were never written by
     * anything, so the Started and Completed columns of every exported report
     * were blank. A ride that ends — completed, cancelled or a no-show — stamps
     * `completedAt`, because that is the moment it stopped being live and it is
     * what the reports measure duration against.
     */
    suspend fun updateRideStatus(rideId: String, status: RideStatus) {
        val now = System.currentTimeMillis().toString()
        val updates = mutableMapOf<String, Any?>("status" to status.name)
        when (status) {
            RideStatus.IN_PROGRESS -> updates["startedAt"] = now
            RideStatus.COMPLETED, RideStatus.CANCELLED, RideStatus.NO_SHOW ->
                updates["completedAt"] = now
            else -> Unit
        }
        database.getReference("rides").child(rideId).updateChildren(updates).await()
    }

    /**
     * Records what the driver says was actually collected.
     *
     * Cash changes hands off the app, so the quoted fare and the fare taken can
     * differ. `actualFare` was read by every report and written by nothing,
     * which is why the Actual fare column was a column of zeroes.
     */
    suspend fun recordActualFare(rideId: String, amount: Double) {
        database.getReference("rides").child(rideId).child("actualFare").setValue(amount).await()
    }

    /**
     * Writes the passenger's own two contact fields and nothing else.
     *
     * Named children rather than the whole node, because the rules grant the
     * passenger write on exactly these two and refuse anything wider — the ride
     * itself belongs to the driver.
     */
    suspend fun attachPassengerContact(rideId: String, name: String, phone: String) {
        val ref = database.getReference("rides").child(rideId)
        ref.child("passengerName").setValue(name).await()
        ref.child("passengerPhone").setValue(phone).await()
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

    /**
     * One rating per ride.
     *
     * The key used to be the rater, which meant a passenger held exactly one
     * opinion of a driver however many times they travelled: a second ride with
     * the same driver silently replaced the first. Keying by ride records each
     * journey separately, and it is the shape a rule can check — a ride names
     * both parties and carries the status, so the rules can require that the
     * writer was the passenger on it and that it actually completed.
     */
    suspend fun submitRating(driverId: String, rideId: String, raterId: String, stars: Int) {
        database.getReference("driverRatings").child(driverId).child(rideId)
            .setValue(mapOf("stars" to stars, "raterId" to raterId)).await()
    }

    /**
     * Every rating a driver has been given.
     *
     * Reads both shapes. Ratings written before they were keyed by ride are a
     * bare number under the rater's uid; ones written since are an object with
     * the stars and the rater on it. Dropping the old ones would erase a
     * driver's history the first time they opened the new build.
     */
    fun getRatingsFlow(driverId: String): Flow<List<Int>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(
                    snapshot.children.mapNotNull { child ->
                        child.getValue(Int::class.java)
                            ?: child.child("stars").getValue(Int::class.java)
                    }
                )
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
            .setValue(notification).await()
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
        // Awaited one at a time: forEach returns Unit, so awaiting the loop
        // rather than the writes inside it did not compile and, had it, would
        // have returned before any of them landed.
        ids.forEach { ref.child(it).child("read").setValue(true).await() }
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
            .updateChildren(mapOf("licenceNumber" to number, "licenceExpiry" to expiry)).await()
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
