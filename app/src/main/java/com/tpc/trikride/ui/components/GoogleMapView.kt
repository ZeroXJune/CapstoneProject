package com.tpc.trikride.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.tpc.trikride.models.Location

/**
 * Google Maps rendering, used when a key is configured.
 *
 * The plain [MapView] rather than the maps-compose wrapper: the wrapper adds a
 * dependency whose version has to track Compose's, and everything here is a
 * handful of calls either way.
 *
 * Nothing in this file uses a Map ID or cloud-based styling. That matters — a
 * Map ID turns each map load into a billed Dynamic Maps call, whereas
 * client-styled maps render at no charge. The styling stays in code for that
 * reason as much as for convenience.
 */
@Composable
internal fun GoogleTrikMap(
    pins: List<MapPin>,
    modifier: Modifier,
    height: Dp,
    fallbackCentre: Location,
    zoom: Double,
    connectPins: Boolean,
    routeColor: Color
) {
    val mapView = rememberMapViewWithLifecycle()
    val currentPins by rememberUpdatedState(pins)

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(18.dp)),
        factory = { mapView },
        update = { view ->
            view.getMapAsync { map ->
                map.clear()
                map.uiSettings.isZoomControlsEnabled = false
                map.uiSettings.isMapToolbarEnabled = false

                if (connectPins && currentPins.size >= 2) {
                    map.addPolyline(
                        PolylineOptions()
                            .addAll(currentPins.take(2).map { it.location.toLatLng() })
                            .color(routeColor.toArgb())
                            .width(10f)
                    )
                }

                currentPins.forEach { pin ->
                    map.addMarker(
                        MarkerOptions()
                            .position(pin.location.toLatLng())
                            .title(pin.label)
                            .icon(BitmapDescriptorFactory.defaultMarker(pin.color.toMapHue()))
                    )
                }

                when {
                    currentPins.isEmpty() -> map.moveCamera(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition.fromLatLngZoom(
                                fallbackCentre.toLatLng(), zoom.toFloat()
                            )
                        )
                    )
                    currentPins.size == 1 -> map.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            currentPins.first().location.toLatLng(), zoom.toFloat()
                        )
                    )
                    else -> {
                        val bounds = LatLngBounds.builder()
                            .apply { currentPins.forEach { include(it.location.toLatLng()) } }
                            .build()
                        // Padding keeps pins off the edge. Wrapped because the
                        // camera throws if the view has not been laid out yet.
                        runCatching {
                            map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 120))
                        }
                    }
                }
            }
        }
    )
}

/**
 * Google Maps version of the centre-pin picker. Reports the camera target once
 * the user stops moving the map.
 */
@Composable
internal fun GooglePickerMap(
    centre: Location,
    modifier: Modifier,
    height: Dp,
    onMoved: (Location) -> Unit
) {
    val mapView = rememberMapViewWithLifecycle()
    val currentOnMoved by rememberUpdatedState(onMoved)
    val currentCentre by rememberUpdatedState(centre)

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(18.dp)),
        factory = { mapView },
        update = { view ->
            view.getMapAsync { map ->
                map.uiSettings.isZoomControlsEnabled = false
                map.uiSettings.isMapToolbarEnabled = false

                map.setOnCameraIdleListener {
                    val target = map.cameraPosition.target
                    currentOnMoved(
                        Location(
                            latitude = target.latitude,
                            longitude = target.longitude,
                            timestamp = System.currentTimeMillis().toString()
                        )
                    )
                }

                // Only move the camera when the incoming point is somewhere
                // else, so reporting the centre back does not fight the pan.
                val target = map.cameraPosition.target
                val moved = kotlin.math.abs(target.latitude - currentCentre.latitude) > 1e-5 ||
                    kotlin.math.abs(target.longitude - currentCentre.longitude) > 1e-5
                if (moved) {
                    map.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(currentCentre.toLatLng(), 17f)
                    )
                }
            }
        }
    )
}

/**
 * A [MapView] driven by the composition's lifecycle.
 *
 * Google's MapView is an old-style view that expects onCreate, onResume, onPause
 * and onDestroy to be forwarded to it by hand. Skipping them leaks the map's
 * renderer and its native resources.
 */
@Composable
private fun rememberMapViewWithLifecycle(): MapView {
    val context = androidx.compose.ui.platform.LocalContext.current
    val mapView = remember { MapView(context).apply { id = android.view.View.generateViewId() } }
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(lifecycle, mapView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(null)
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> Unit
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
            mapView.onDestroy()
        }
    }
    return mapView
}

private fun Location.toLatLng() = LatLng(latitude, longitude)

/**
 * Google's default markers come from a fixed hue wheel rather than an arbitrary
 * colour, so the app's palette is mapped onto the nearest available hue.
 */
private fun Color.toMapHue(): Float {
    val hsv = FloatArray(3)
    android.graphics.Color.colorToHSV(toArgb(), hsv)
    return hsv[0]
}

/** Whether a Maps key was supplied at build time. */
internal val hasGoogleMapsKey: Boolean
    get() = com.tpc.trikride.BuildConfig.MAPS_API_KEY.isNotBlank()
