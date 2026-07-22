package com.tpc.trikride.models

/**
 * A fixed official fare for a specific pickup → destination route.
 * When present, it overrides the distance-based formula.
 */
data class RouteFare(
    val pickup: String = "",
    val destination: String = "",
    val fare: Double = 0.0
)

/**
 * Admin-editable pricing. Stored at config/fare in the database so the real
 * fares can be uploaded and updated without changing code. The defaults here
 * are only placeholders until the official prices are entered.
 */
data class FareConfig(
    val baseFare: Double = 15.0,
    val perKmRate: Double = 5.0,
    val perExtraPassenger: Double = 5.0,
    val routes: List<RouteFare> = emptyList()
)
