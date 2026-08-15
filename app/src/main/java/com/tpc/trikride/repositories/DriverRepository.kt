package com.tpc.trikride.repositories

import com.tpc.trikride.models.Driver
import com.tpc.trikride.models.Location
import com.tpc.trikride.models.VerificationStatus
import com.tpc.trikride.services.FirebaseService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

    /** The assigned driver's live position, for the passenger's tracking map. */
    fun driverLocation(driverId: String): Flow<Location?> =
        firebase.getDriverFlow(driverId).map { it?.currentLocation }

    suspend fun updateLocation(driverId: String, location: Location) {
        firebase.updateDriverLocation(driverId, location)
    }
}
