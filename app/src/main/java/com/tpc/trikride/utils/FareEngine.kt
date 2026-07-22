package com.tpc.trikride.utils

import com.tpc.trikride.models.FareConfig
import kotlin.math.ceil

/**
 * Prices a ride from the admin-configured [FareConfig].
 *
 *  - If a fixed route fare exists for the pickup→destination pair, that
 *    official price is used.
 *  - Otherwise it falls back to base fare + per-km distance surcharge.
 *  - Extra passengers add the configured surcharge on top.
 */
object FareEngine {

    private const val BASE_DISTANCE_KM = 2.0

    fun routeFare(config: FareConfig, pickup: String, destination: String): Double? =
        config.routes.firstOrNull { it.pickup == pickup && it.destination == destination }?.fare

    /** Fare before the extra-passenger surcharge. */
    fun baseTotal(config: FareConfig, pickup: String, destination: String, distanceKm: Double): Double {
        routeFare(config, pickup, destination)?.let { return it }
        val extraKm = ceil((distanceKm - BASE_DISTANCE_KM).coerceAtLeast(0.0))
        return config.baseFare + extraKm * config.perKmRate
    }

    fun extraPassengerFare(config: FareConfig, passengerCount: Int): Double =
        (passengerCount - 1).coerceAtLeast(0) * config.perExtraPassenger

    fun total(
        config: FareConfig,
        pickup: String,
        destination: String,
        distanceKm: Double,
        passengerCount: Int
    ): Double = baseTotal(config, pickup, destination, distanceKm) +
        extraPassengerFare(config, passengerCount)
}
