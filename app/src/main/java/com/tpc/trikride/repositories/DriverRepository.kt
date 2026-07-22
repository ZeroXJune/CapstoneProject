package com.tpc.trikride.repositories

import com.tpc.trikride.models.Driver
import com.tpc.trikride.models.Location
import com.tpc.trikride.models.VerificationStatus
import com.tpc.trikride.services.FirebaseService
import kotlinx.coroutines.flow.Flow

class DriverRepository(
    private val firebase: FirebaseService = FirebaseService()
) {

    suspend fun registerDriver(
        userId: String,
        licenseNumber: String,
        licenseExpiry: String,
        tricycleNumber: String
    ): Driver {
        val driver = Driver(
            userId = userId,
            licenseNumber = licenseNumber,
            licenseExpiry = licenseExpiry,
            tricycleNumber = tricycleNumber,
            verificationStatus = VerificationStatus.PENDING,
            isAvailable = false
        )
        firebase.registerDriver(userId, driver)
        return driver
    }

    fun driverProfile(driverId: String): Flow<Driver?> = firebase.getDriverFlow(driverId)

    suspend fun setAvailability(driverId: String, isAvailable: Boolean) {
        firebase.updateDriverAvailability(driverId, isAvailable)
    }

    suspend fun updateLocation(driverId: String, location: Location) {
        firebase.updateDriverLocation(driverId, location)
    }
}
