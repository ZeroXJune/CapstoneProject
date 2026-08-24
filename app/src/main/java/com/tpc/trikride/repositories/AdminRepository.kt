package com.tpc.trikride.repositories

import com.tpc.trikride.models.Driver
import com.tpc.trikride.models.DriverDocument
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

    /**
     * Refuses an application that was never approved, and destroys the licence
     * photograph with it.
     *
     * Keeping a refused applicant's identity document serves no purpose the
     * system has, so it goes at the moment the decision is made rather than in
     * some later clear-out.
     */
    suspend fun rejectDriver(driverId: String) {
        firebase.updateDriverVerification(driverId, VerificationStatus.REJECTED)
        firebase.deleteLicenceImage(driverId)
    }

    /**
     * Withdraws approval from a driver who had it, keeping the photograph.
     *
     * Deliberately not the same operation as refusing an application. Approval
     * is usually withdrawn either because a licence has lapsed or because a
     * concern is being looked into, and destroying the document in either case
     * would remove the thing the decision may later have to be justified
     * against. It goes when the account does.
     */
    suspend fun revokeApproval(driverId: String) =
        firebase.updateDriverVerification(driverId, VerificationStatus.REJECTED)

    /** Fetched only when an administrator opens a specific driver to review. */
    suspend fun licenceDocument(driverId: String): DriverDocument? =
        firebase.getLicenceImage(driverId)
}
