package com.tpc.trikride.repositories

import android.content.Context
import android.net.Uri
import com.tpc.trikride.models.Driver
import com.tpc.trikride.models.DriverDocument
import com.tpc.trikride.models.Location
import com.tpc.trikride.models.VerificationStatus
import com.tpc.trikride.services.FirebaseService
import com.tpc.trikride.utils.LicenceImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

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

    /**
     * Stores the driver's licence photograph.
     *
     * Encoding happens off the main thread: a camera photograph is several
     * megapixels, and decoding and compressing it is not something to do on the
     * frame the button was pressed on. [consentedAt] is passed in rather than
     * stamped here, because it belongs to the moment the driver agreed, not the
     * moment the write happened.
     */
    suspend fun saveLicenceImage(context: Context, driverId: String, imageUri: Uri, consentedAt: String) {
        val encoded = withContext(Dispatchers.IO) { LicenceImage.encode(context, imageUri) }
            ?: throw IllegalStateException("That image could not be prepared. Try another photo.")
        firebase.saveLicenceImage(
            driverId,
            DriverDocument(
                image = encoded,
                uploadedAt = System.currentTimeMillis().toString(),
                consentedAt = consentedAt
            )
        )
    }

    suspend fun licenceImage(driverId: String): DriverDocument? =
        firebase.getLicenceImage(driverId)

    /** Lets a driver withdraw the photograph they sent. */
    suspend fun deleteLicenceImage(driverId: String) {
        firebase.deleteLicenceImage(driverId)
    }
}
