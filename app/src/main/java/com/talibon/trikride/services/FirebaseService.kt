package com.talibon.trikride.services

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.talibon.trikride.models.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseService {
    private val database = FirebaseDatabase.getInstance()

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
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val rides = snapshot.children.mapNotNull { it.getValue(Ride::class.java) }
                    .filter { it.passengerId == passengerId && it.status != RideStatus.COMPLETED }
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

    // Document Upload
    suspend fun uploadDocument(driverId: String, document: Document) {
        database.getReference("drivers").child(driverId).child("documents").push()
            .setValue(document)
    }
}
