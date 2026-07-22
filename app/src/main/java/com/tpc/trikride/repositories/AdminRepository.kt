package com.tpc.trikride.repositories

import com.tpc.trikride.models.Driver
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.User
import com.tpc.trikride.models.VerificationStatus
import com.tpc.trikride.services.FirebaseService
import kotlinx.coroutines.flow.Flow

class AdminRepository(
    private val firebase: FirebaseService = FirebaseService()
) {
    fun drivers(): Flow<List<Driver>> = firebase.getAllDriversFlow()
    fun users(): Flow<List<User>> = firebase.getAllUsersFlow()
    fun rides(): Flow<List<Ride>> = firebase.getAllRidesFlow()

    suspend fun approveDriver(driverId: String) =
        firebase.updateDriverVerification(driverId, VerificationStatus.APPROVED)

    suspend fun rejectDriver(driverId: String) =
        firebase.updateDriverVerification(driverId, VerificationStatus.REJECTED)
}
