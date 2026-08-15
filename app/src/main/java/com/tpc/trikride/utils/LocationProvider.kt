package com.tpc.trikride.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.tpc.trikride.models.Location
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * The device's own position, from the fused location provider.
 *
 * Nothing here is billed. Reading GPS is a platform capability; only the
 * mapping and routing web services carry a price, and the app uses neither.
 *
 * Updates are delivered while a screen is collecting and stop when it is not,
 * because the flow removes its callback on cancellation. There is no background
 * service and no background location permission: a driver's position is
 * published only while the app is open and they are online. That is a
 * deliberate limit — background tracking would need a foreground service and a
 * persistent notification, and it is not worth the battery or the intrusion for
 * a pilot.
 */
object LocationProvider {

    private const val UPDATE_INTERVAL_MS = 5_000L
    private const val FASTEST_INTERVAL_MS = 3_000L

    fun hasPermission(context: Context): Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED

    /** A stream of positions, updating roughly every five seconds. */
    @SuppressLint("MissingPermission")
    fun updates(context: Context): Flow<Location> = callbackFlow {
        if (!hasPermission(context)) {
            close()
            return@callbackFlow
        }

        val client = LocationServices.getFusedLocationProviderClient(context)
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, UPDATE_INTERVAL_MS)
            .setMinUpdateIntervalMillis(FASTEST_INTERVAL_MS)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { fix ->
                    trySend(
                        Location(
                            latitude = fix.latitude,
                            longitude = fix.longitude,
                            timestamp = System.currentTimeMillis().toString()
                        )
                    )
                }
            }
        }

        client.requestLocationUpdates(request, callback, Looper.getMainLooper())
        awaitClose { client.removeLocationUpdates(callback) }
    }

    /** A single position, or null if permission is missing or no fix is available. */
    @SuppressLint("MissingPermission")
    suspend fun current(context: Context): Location? {
        if (!hasPermission(context)) return null
        val client = LocationServices.getFusedLocationProviderClient(context)
        return suspendCancellableCoroutine { cont ->
            client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { fix ->
                    cont.resume(
                        fix?.let {
                            Location(
                                latitude = it.latitude,
                                longitude = it.longitude,
                                timestamp = System.currentTimeMillis().toString()
                            )
                        }
                    )
                }
                .addOnFailureListener { cont.resume(null) }
        }
    }
}
