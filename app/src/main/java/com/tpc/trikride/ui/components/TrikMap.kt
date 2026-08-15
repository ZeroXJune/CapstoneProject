package com.tpc.trikride.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.edit
import com.tpc.trikride.models.Location
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.Polyline
import java.io.File

/** Talibon town centre, used when nothing better is known yet. */
val TALIBON_CENTRE = Location(10.1531, 124.3251, "Talibon, Bohol")

/** A pin to draw on the map. */
data class MapPin(
    val location: Location,
    val label: String,
    val color: Color,
    /** Drawn larger, for the thing the user is actually tracking. */
    val emphasis: Boolean = false
)

/**
 * Configures osmdroid once per process.
 *
 * Two things matter here. The tile cache goes in app-private storage, which
 * keeps osmdroid from asking for a storage permission it does not need on any
 * Android version this app supports. And the user agent is set to the package
 * name: OpenStreetMap's tile servers block osmdroid's default agent, because
 * too many apps shipped without changing it, so leaving it alone means blank
 * tiles.
 */
private fun configureOsmdroid(context: Context) {
    val config = Configuration.getInstance()
    if (config.userAgentValue == context.packageName) return

    val prefs = context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
    config.load(context, prefs)
    config.userAgentValue = context.packageName
    config.osmdroidBasePath = File(context.cacheDir, "osmdroid").apply { mkdirs() }
    config.osmdroidTileCache = File(config.osmdroidBasePath, "tiles").apply { mkdirs() }
    prefs.edit { putString("osmdroid.basePath", config.osmdroidBasePath.absolutePath) }
}

/**
 * An OpenStreetMap view with pins on it.
 *
 * OpenStreetMap rather than Google Maps because it needs no API key and no
 * billing account, so there is nothing that can expire or be suspended and take
 * the maps down with it. Google's Android SDK renders at no charge too, as long
 * as no Map ID is used, but the key behind it depends on a live billing account.
 *
 * The tradeoff is OpenStreetMap's tile usage policy, which suits a pilot at this
 * scale but would need a dedicated tile source if the app ever grew. Swapping
 * renderer means changing this file and nothing else.
 */
@Composable
fun TrikMap(
    pins: List<MapPin>,
    modifier: Modifier = Modifier,
    height: Dp,
    /** Centre here when there are no pins to frame. */
    fallbackCentre: Location = TALIBON_CENTRE,
    zoom: Double = 15.5,
    /** Draws a straight line between the first two pins. */
    connectPins: Boolean = false
) {
    val context = LocalContext.current
    remember { configureOsmdroid(context); true }

    val outline = MaterialThemeOutline()

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(18.dp)),
        factory = { ctx ->
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                // The built-in +/- buttons overlap our own controls and are
                // redundant once pinch-zoom is on.
                zoomController.setVisibility(
                    org.osmdroid.views.CustomZoomButtonsController.Visibility.NEVER
                )
                controller.setZoom(zoom)
                controller.setCenter(fallbackCentre.toGeoPoint())
            }
        },
        update = { map ->
            map.overlays.clear()

            if (connectPins && pins.size >= 2) {
                map.overlays.add(
                    Polyline(map).apply {
                        setPoints(pins.take(2).map { it.location.toGeoPoint() })
                        outlinePaint.color = outline
                        outlinePaint.strokeWidth = 7f
                        outlinePaint.isAntiAlias = true
                    }
                )
            }

            pins.forEach { pin ->
                map.overlays.add(
                    Marker(map).apply {
                        position = pin.location.toGeoPoint()
                        title = pin.label
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                        icon = dotDrawable(pin.color.toArgb(), if (pin.emphasis) 34 else 24)
                    }
                )
            }

            when {
                pins.isEmpty() -> map.controller.setCenter(fallbackCentre.toGeoPoint())
                pins.size == 1 -> map.controller.animateTo(pins.first().location.toGeoPoint())
                else -> {
                    // Frame everything, with room so pins are not on the edge.
                    val box = org.osmdroid.util.BoundingBox.fromGeoPointsSafe(
                        pins.map { it.location.toGeoPoint() }
                    )
                    map.post { runCatching { map.zoomToBoundingBox(box.increaseByScale(1.6f), true) } }
                }
            }
            map.invalidate()
        },
        // osmdroid holds a tile-download thread pool and a tile cache handle.
        // Without onDetach they outlive the screen.
        onRelease = { it.onDetach() }
    )
}

/**
 * A centre-pinned map for choosing a point. The map moves under a fixed pin,
 * which is easier on a small screen than dragging a marker, and [onMoved]
 * reports wherever the pin ends up.
 */
@Composable
fun PickerMap(
    centre: Location,
    modifier: Modifier = Modifier,
    height: Dp,
    pinColor: Color,
    onMoved: (Location) -> Unit
) {
    val context = LocalContext.current
    remember { configureOsmdroid(context); true }
    val argb = pinColor.toArgb()
    // The overlay below is built once in factory{}, so it would otherwise hold
    // the first onMoved forever.
    val currentOnMoved by rememberUpdatedState(onMoved)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(18.dp))
    ) {
        AndroidView(
            modifier = Modifier.fillMaxWidth().height(height),
            factory = { ctx ->
                MapView(ctx).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)
                    zoomController.setVisibility(
                        org.osmdroid.views.CustomZoomButtonsController.Visibility.NEVER
                    )
                    controller.setZoom(17.0)
                    controller.setCenter(centre.toGeoPoint())

                    // Report the centre once the user stops moving the map.
                    overlays.add(object : Overlay() {
                        override fun onTouchEvent(
                            event: android.view.MotionEvent?,
                            view: MapView?
                        ): Boolean {
                            if (event?.action == android.view.MotionEvent.ACTION_UP && view != null) {
                                val c = view.mapCenter
                                currentOnMoved(
                                    Location(
                                        latitude = c.latitude,
                                        longitude = c.longitude,
                                        timestamp = System.currentTimeMillis().toString()
                                    )
                                )
                            }
                            return false
                        }
                    })
                }
            },
            update = { map ->
                // Only recentre when the incoming point is somewhere else, so
                // that reporting the centre back does not fight the user's pan.
                val c = map.mapCenter
                val moved = kotlin.math.abs(c.latitude - centre.latitude) > 1e-5 ||
                    kotlin.math.abs(c.longitude - centre.longitude) > 1e-5
                if (moved) map.controller.animateTo(centre.toGeoPoint())
            },
            onRelease = { it.onDetach() }
        )

        // The fixed pin sits at the centre of the viewport.
        androidx.compose.foundation.Canvas(
            modifier = Modifier.fillMaxWidth().height(height)
        ) {
            val cx = size.width / 2f
            val cy = size.height / 2f
            drawCircle(Color.Black.copy(alpha = 0.18f), radius = 7f, center =
                androidx.compose.ui.geometry.Offset(cx, cy + 16f))
            drawLine(
                color = Color(argb),
                start = androidx.compose.ui.geometry.Offset(cx, cy + 14f),
                end = androidx.compose.ui.geometry.Offset(cx, cy - 12f),
                strokeWidth = 5f
            )
            drawCircle(Color.White, radius = 15f,
                center = androidx.compose.ui.geometry.Offset(cx, cy - 20f))
            drawCircle(Color(argb), radius = 11f,
                center = androidx.compose.ui.geometry.Offset(cx, cy - 20f))
        }
    }
}

@Composable
private fun MaterialThemeOutline(): Int =
    androidx.compose.material3.MaterialTheme.colorScheme.primary.toArgb()

private fun Location.toGeoPoint() = GeoPoint(latitude, longitude)

/** A filled circle with a white ring, drawn rather than shipped as an asset. */
private fun dotDrawable(argb: Int, sizePx: Int): Drawable = object : Drawable() {
    private val fill = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = argb }
    private val ring = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = sizePx / 7f
    }

    override fun draw(canvas: Canvas) {
        val r = sizePx / 2f
        canvas.drawCircle(r, r, r - ring.strokeWidth / 2f, fill)
        canvas.drawCircle(r, r, r - ring.strokeWidth / 2f, ring)
    }

    override fun getIntrinsicWidth() = sizePx
    override fun getIntrinsicHeight() = sizePx
    override fun setAlpha(alpha: Int) = Unit
    override fun setColorFilter(colorFilter: android.graphics.ColorFilter?) = Unit
    @Deprecated("Deprecated in Drawable")
    override fun getOpacity() = android.graphics.PixelFormat.TRANSLUCENT
}
