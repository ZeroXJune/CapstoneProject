package com.tpc.trikride.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.tpc.trikride.models.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

/**
 * Turns a pinned point into something a person can read.
 *
 * Uses Android's own [Geocoder], which is free and needs no key. It is not
 * guaranteed to be present or to answer — it depends on a backend the device
 * supplies, and rural Bohol is thinly covered — so every path falls back to the
 * coordinates themselves. A driver can navigate from a pin on a map and a pair
 * of numbers; an empty label would tell them nothing.
 */
object ReverseGeocoder {

    /**
     * An Open Location Code, as in "4XV8+G3".
     *
     * Google hands one of these back as the first address line wherever it has
     * no street address to give, which is most of Talibon, so the line that is
     * supposed to be the most specific is the one nobody can read. The named
     * parts of the same result — barangay, municipality, province — are the
     * ones worth showing, and a plus code is stripped wherever it appears.
     */
    private val PLUS_CODE = Regex(
        "\\b[23456789CFGHJMPQRVWX]{4,8}\\+[23456789CFGHJMPQRVWX]{2,3}\\b"
    )

    suspend fun describe(context: Context, location: Location): String =
        withContext(Dispatchers.IO) {
            val fallback = "Pinned location (%.5f, %.5f)".format(
                Locale.US, location.latitude, location.longitude
            )
            if (!Geocoder.isPresent()) return@withContext fallback

            try {
                @Suppress("DEPRECATION")
                val results = Geocoder(context, Locale.getDefault())
                    .getFromLocation(location.latitude, location.longitude, 1)
                val first = results?.firstOrNull() ?: return@withContext fallback
                readable(first) ?: fallback
            } catch (e: Exception) {
                fallback
            }
        }

    /** Named places first, then whatever survives cleaning the address line. */
    private fun readable(address: Address): String? {
        val named = listOfNotNull(
            address.thoroughfare,
            address.subLocality,
            address.locality,
            address.subAdminArea
        ).map { it.trim() }
            .filter { it.isNotBlank() && !PLUS_CODE.containsMatchIn(it) }
            .distinct()
            .take(3)

        if (named.isNotEmpty()) return named.joinToString(", ")

        val line = address.getAddressLine(0) ?: return null
        return line.replace(PLUS_CODE, "")
            .split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .joinToString(", ")
            .ifBlank { null }
    }
}
