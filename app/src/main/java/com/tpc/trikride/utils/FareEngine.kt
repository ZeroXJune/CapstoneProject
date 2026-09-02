package com.tpc.trikride.utils

import com.tpc.trikride.models.FareConfig
import com.tpc.trikride.models.FareQuote
import com.tpc.trikride.models.FareStop
import com.tpc.trikride.models.FareType

/**
 * Prices a ride against the posted FeTODAT fare table.
 *
 * The sheet gives a flat amount per stop, one column for the regular rate and
 * one for the senior, PWD and student rate, so pricing is a lookup rather than
 * a calculation. Two things sit on top of the lookup: the ordinance minimum
 * fare, which a stop rate can never fall below, and the per-head multiplier,
 * since a tricycle fare is charged per passenger.
 */
object FareEngine {

    fun minimumFor(config: FareConfig, fareType: FareType): Double = when (fareType) {
        FareType.REGULAR -> config.minimumRegular
        FareType.DISCOUNTED -> config.minimumDiscounted
    }

    fun rateFor(stop: FareStop, fareType: FareType): Double = when (fareType) {
        FareType.REGULAR -> stop.regularFare
        FareType.DISCOUNTED -> stop.discountedFare
    }

    /**
     * Prices a party that may hold both kinds of passenger.
     *
     * Each column is raised to its own ordinance minimum before it is
     * multiplied, so a senior travelling beside a regular passenger is charged
     * the senior rate and the regular passenger is not.
     *
     * Where the sheet is not charged per head, one fare covers the tricycle,
     * and it is the regular rate unless everybody aboard is entitled to the
     * discount — charging the discounted rate for a mixed party would undercharge,
     * and charging the regular rate to a party of seniors would overcharge them.
     */
    fun quote(
        config: FareConfig,
        stop: FareStop,
        regularCount: Int,
        discountedCount: Int
    ): FareQuote {
        val regularRate = maxOf(
            rateFor(stop, FareType.REGULAR), minimumFor(config, FareType.REGULAR)
        )
        val discountedRate = maxOf(
            rateFor(stop, FareType.DISCOUNTED), minimumFor(config, FareType.DISCOUNTED)
        )
        val regular = regularCount.coerceAtLeast(0)
        val discounted = discountedCount.coerceAtLeast(0)

        val total = if (config.chargePerPassenger) {
            regular * regularRate + discounted * discountedRate
        } else {
            if (regular > 0) regularRate else discountedRate
        }

        return FareQuote(
            regularCount = regular,
            discountedCount = discounted,
            regularRate = regularRate,
            discountedRate = discountedRate,
            total = total,
            minimumApplied = rateFor(stop, FareType.REGULAR) < minimumFor(config, FareType.REGULAR) ||
                rateFor(stop, FareType.DISCOUNTED) < minimumFor(config, FareType.DISCOUNTED),
            stopLabel = stop.label
        )
    }

    /**
     * The two rates on the sheet that are not tied to a numbered stop, shaped
     * as stops so the picker and the pricing path treat them like any other
     * destination. Neither is split by rate column on the posted sheet, so both
     * columns carry the same amount.
     */
    fun flatStops(config: FareConfig): List<FareStop> = listOf(
        FareStop(
            id = FLAT_POBLACION,
            zone = FLAT_ZONE,
            name = FareConfig.POBLACION_LABEL,
            regularFare = config.poblacionFlat,
            discountedFare = config.poblacionFlat
        ),
        FareStop(
            id = FLAT_TERMINAL,
            zone = FLAT_ZONE,
            name = FareConfig.TERMINAL_ROUND_TRIP_LABEL,
            regularFare = config.terminalRoundTrip,
            discountedFare = config.terminalRoundTrip
        )
    )

    const val FLAT_ZONE = "Flat rate"
    private const val FLAT_POBLACION = "flat__poblacion"
    private const val FLAT_TERMINAL = "flat__terminal_ncbi_round_trip"
}
