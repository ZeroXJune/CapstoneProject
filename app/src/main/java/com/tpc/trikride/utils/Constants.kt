package com.tpc.trikride.utils

import com.tpc.trikride.models.Location

object Constants {

    /** How long a ride request stays open before expiring, in milliseconds. */
    const val RIDE_REQUEST_TTL_MS = 5 * 60 * 1000L

    /**
     * Common pickup/dropoff points around Talibon Polytechnic College.
     * Coordinates are approximate landmarks in Talibon, Bohol; refine them
     * during field validation with the drivers' association.
     */
    val CAMPUS_LOCATIONS = listOf(
        Location(10.1478, 124.3288, "TPC Main Gate, San Isidro"),
        Location(10.1502, 124.3204, "Talibon Public Market"),
        Location(10.1531, 124.3251, "Poblacion, Talibon"),
        Location(10.1419, 124.3345, "San Jose, Talibon"),
        Location(10.1553, 124.3178, "Talibon Port Area"),
        Location(10.1367, 124.3122, "Bagacay Crossing"),
        Location(10.1602, 124.3410, "San Agustin, Talibon"),
        Location(10.1330, 124.3480, "Sikatuna, Talibon")
    )
}
