package com.talibon.trikride.utils

import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Fare estimation based on typical municipal tricycle rates in Talibon, Bohol.
 * The base fare covers the first [BASE_DISTANCE_KM]; each additional kilometer
 * (rounded up) adds [PER_KM_RATE].
 */
object FareCalculator {

    const val BASE_FARE_PHP = 15.0
    const val BASE_DISTANCE_KM = 2.0
    const val PER_KM_RATE = 5.0

    /** Average tricycle speed used for duration estimates, in km/h. */
    private const val AVERAGE_SPEED_KMH = 20.0

    fun estimateFare(distanceKm: Double): Double {
        if (distanceKm <= BASE_DISTANCE_KM) return BASE_FARE_PHP
        val extraKm = ceil(distanceKm - BASE_DISTANCE_KM)
        return BASE_FARE_PHP + extraKm * PER_KM_RATE
    }

    /** Estimated travel time in minutes, never less than 3. */
    fun estimateDurationMinutes(distanceKm: Double): Int =
        max(3, (distanceKm / AVERAGE_SPEED_KMH * 60).roundToInt())
}
