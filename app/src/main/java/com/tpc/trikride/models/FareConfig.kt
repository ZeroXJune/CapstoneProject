package com.tpc.trikride.models

import com.google.firebase.database.Exclude

/**
 * One priced destination from the posted FeTODAT fare sheet.
 *
 * The sheet is organised by zone, and within a zone by stop, with two columns:
 * the regular rate and the discounted rate for seniors, persons with
 * disabilities and students. There is no distance formula on the sheet, so
 * there is none in the app either — the stop carries its own price.
 *
 * [needsReview] marks a row that came out of transcription with a problem
 * worth a human look. [active] false keeps a stop out of the passenger's
 * picker entirely, which is what happens when a rate is missing or clearly
 * wrong.
 */
data class FareStop(
    val id: String = "",
    val zone: String = "",
    val name: String = "",
    val regularFare: Double = 0.0,
    val discountedFare: Double = 0.0,
    val active: Boolean = true,
    val needsReview: Boolean = false,
    val confidence: String = "High",
    val note: String = "",
    // Optional. The posted sheet gives names, not coordinates, so these are
    // filled in by the administrator over time. A stop without them still
    // prices and books normally; it just does not appear on the map.
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) {
    /**
     * What the passenger sees in the picker. Excluded from the database write,
     * since Firebase would otherwise persist every public getter as a field and
     * then fail to find somewhere to put it on the way back in.
     */
    @get:Exclude
    val label: String get() = if (zone.isBlank()) name else "$name — $zone"

    @get:Exclude
    val hasCoordinates: Boolean get() = latitude != 0.0 || longitude != 0.0

    @get:Exclude
    val location: Location
        get() = Location(latitude = latitude, longitude = longitude, address = label)
}

/** Which column of the fare sheet applies to this passenger. */
enum class FareType {
    REGULAR,
    DISCOUNTED;

    val label: String
        get() = when (this) {
            REGULAR -> "Regular"
            DISCOUNTED -> "Senior / PWD / Student"
        }
}

/**
 * The parts of the fare sheet that are not tied to one stop: the two minimum
 * fares, the flat rates, and whether a fare is charged per head.
 *
 * Stored at config/fare. The stops live separately under config/fareStops so
 * that correcting one price is a small write rather than a rewrite of all 240.
 */
data class FareConfig(
    val minimumRegular: Double = 15.0,
    val minimumDiscounted: Double = 12.0,
    val poblacionFlat: Double = 15.0,
    val terminalRoundTrip: Double = 25.0,
    val chargePerPassenger: Boolean = true,
    val source: String = DEFAULT_SOURCE,
    val seededAt: String = ""
) {
    companion object {
        const val DEFAULT_SOURCE =
            "FeTODAT — ordinance amending Section 1 of Municipal Ordinance No. 2018-05, " +
                "enacted 8 November 2022"
        const val POBLACION_LABEL = "Any point within Poblacion"
        const val TERMINAL_ROUND_TRIP_LABEL =
            "Talibon Integrated Bus Terminal (TIBT) to NCBI, round trip"
    }
}

/** A priced ride, broken down so the passenger can see where the number came from. */
data class FareQuote(
    val perPassenger: Double = 0.0,
    val passengers: Int = 1,
    val total: Double = 0.0,
    val fareType: FareType = FareType.REGULAR,
    /** True when the stop's own rate fell below the ordinance minimum. */
    val minimumApplied: Boolean = false,
    val stopLabel: String = ""
)
