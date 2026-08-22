package com.tpc.trikride.utils

object Constants {

    /** How long a ride request stays open before expiring, in milliseconds. */
    const val RIDE_REQUEST_TTL_MS = 5 * 60 * 1000L

    /** Seats a tricycle can take on one booking. */
    const val MAX_PASSENGERS = 5

    /**
     * The date carried on the issued legal documents. Consent is recorded
     * against this string, so publishing a revised set is a matter of changing
     * it here: every user is then asked to accept again on their next launch.
     */
    const val LEGAL_VERSION = "2026-08-16"
}
