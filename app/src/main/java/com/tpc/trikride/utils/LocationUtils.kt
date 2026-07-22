package com.tpc.trikride.utils

import com.tpc.trikride.models.Location
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object LocationUtils {

    private const val EARTH_RADIUS_KM = 6371.0

    /** Great-circle distance between two points in kilometers (haversine formula). */
    fun distanceKm(from: Location, to: Location): Double {
        val latDistance = Math.toRadians(to.latitude - from.latitude)
        val lonDistance = Math.toRadians(to.longitude - from.longitude)

        val a = sin(latDistance / 2) * sin(latDistance / 2) +
                cos(Math.toRadians(from.latitude)) * cos(Math.toRadians(to.latitude)) *
                sin(lonDistance / 2) * sin(lonDistance / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return EARTH_RADIUS_KM * c
    }

    /** Finds the nearest location in [candidates] to [target], or null if empty. */
    fun nearest(target: Location, candidates: List<Location>): Location? =
        candidates.minByOrNull { distanceKm(target, it) }
}
