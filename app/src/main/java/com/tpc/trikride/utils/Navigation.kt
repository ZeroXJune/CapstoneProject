package com.tpc.trikride.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.tpc.trikride.models.Location
import java.util.Locale

/**
 * Hands a point over to a navigation app the driver already has.
 *
 * Waze publishes no SDK that would let turn-by-turn run inside another app —
 * its Transport SDK is for partners under agreement — and every routing service
 * that could be embedded is billed per request. Handing off is therefore not a
 * compromise on a proper integration; it is the integration that exists. It also
 * costs nothing, needs no key, and gives the driver the app they already know.
 *
 * Both targets need coordinates. A fare stop the administrator has not
 * positioned cannot be navigated to, which is why the buttons that use this are
 * hidden rather than shown broken.
 */
object Navigation {

    const val WAZE = "com.waze"
    const val GOOGLE_MAPS = "com.google.android.apps.maps"

    /**
     * Whether the app is on the phone.
     *
     * Android 11 hides other packages unless they are declared in `<queries>`
     * in the manifest, so both of these are listed there. Without that this
     * returns false for an app that is plainly installed.
     */
    fun isInstalled(context: Context, packageName: String): Boolean =
        try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }

    /** Opens Waze with navigation already started. */
    fun openWaze(context: Context, to: Location): Boolean {
        if (!to.hasCoordinates) return false
        val uri = Uri.parse(
            "https://www.waze.com/ul?ll=%s%%2C%s&navigate=yes".format(
                Locale.US, coordinate(to.latitude), coordinate(to.longitude)
            )
        )
        return launch(context, Intent(Intent.ACTION_VIEW, uri).setPackage(WAZE))
    }

    /** Opens Google Maps in turn-by-turn mode. */
    fun openGoogleMaps(context: Context, to: Location): Boolean {
        if (!to.hasCoordinates) return false
        val uri = Uri.parse(
            "google.navigation:q=%s,%s".format(
                Locale.US, coordinate(to.latitude), coordinate(to.longitude)
            )
        )
        return launch(context, Intent(Intent.ACTION_VIEW, uri).setPackage(GOOGLE_MAPS))
    }

    private fun coordinate(value: Double) = "%.6f".format(Locale.US, value)

    private fun launch(context: Context, intent: Intent): Boolean = try {
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        true
    } catch (e: Exception) {
        // Installed a moment ago and gone now, or an install with the launching
        // activity disabled. The caller tells the driver rather than crashing.
        false
    }
}
