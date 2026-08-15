package com.tpc.trikride.utils

import android.content.Context
import android.location.Geocoder
import com.tpc.trikride.models.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

/**
 * Turns a pinned point into something a driver can read.
 *
 * Uses Android's own [Geocoder], which is free and needs no key. It is not
 * guaranteed to be present or to answer — it depends on a backend the device
 * supplies, and rural Bohol is thinly covered — so every path falls back to the
 * coordinates themselves. A driver can navigate from a pin on a map and a pair
 * of numbers; an empty label would tell them nothing.
 */
object ReverseGeocoder {

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

                // Prefer the most specific line the geocoder gives us, then work
                // outwards. getAddressLine(0) is usually the full street address.
                val line = first.getAddressLine(0)
                    ?: listOfNotNull(
                        first.thoroughfare,
                        first.subLocality,
                        first.locality
                    ).joinToString(", ").ifBlank { null }

                line?.takeIf { it.isNotBlank() } ?: fallback
            } catch (e: Exception) {
                fallback
            }
        }
}
