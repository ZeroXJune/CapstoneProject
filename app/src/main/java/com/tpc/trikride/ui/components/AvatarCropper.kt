package com.tpc.trikride.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Lets the user say which part of a photograph becomes their avatar.
 *
 * The previous behaviour took the middle of the image, which is the wrong
 * answer for most portrait photographs — a head sits near the top, and a centre
 * crop takes the chest. Here the image can be dragged and pinched behind a
 * circular window, and what shows through the window is what gets stored.
 *
 * No cropping library is needed for this. The whole problem is one coordinate
 * mapping, from screen position back to source pixel, applied once on confirm.
 */
@Composable
fun AvatarCropper(
    source: Bitmap,
    onCancel: () -> Unit,
    onConfirm: (Bitmap) -> Unit
) {
    val image = remember(source) { source.asImageBitmap() }

    // Zero until the window has been measured; nothing is drawn before then.
    var viewport by remember { mutableFloatStateOf(0f) }
    var zoom by remember(source) { mutableFloatStateOf(1f) }
    var offset by remember(source) { mutableStateOf(Offset.Zero) }

    // The image starts scaled to cover the window, so no edge can show however
    // it is dragged.
    val baseScale = if (viewport > 0f) {
        max(viewport / source.width, viewport / source.height)
    } else {
        1f
    }

    /** Furthest the image may travel at a given zoom before an edge would show. */
    fun travelLimit(atZoom: Float): Offset {
        val s = baseScale * atZoom
        return Offset(
            max(0f, (source.width * s - viewport) / 2f),
            max(0f, (source.height * s - viewport) / 2f)
        )
    }

    Dialog(onDismissRequest = onCancel) {
        Surface(shape = RoundedCornerShape(24.dp), tonalElevation = 6.dp) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    "Position your photo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Drag to move, pinch to zoom. What is inside the circle is what others see.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .onSizeChanged { viewport = it.width.toFloat() }
                        .pointerInput(source) {
                            detectTransformGestures { _, pan, gestureZoom, _ ->
                                val next = (zoom * gestureZoom).coerceIn(1f, 5f)
                                // Clamped against the new zoom: zooming out
                                // otherwise leaves the image parked off-centre
                                // with a gap behind it.
                                val limit = travelLimit(next)
                                zoom = next
                                offset = Offset(
                                    (offset.x + pan.x).coerceIn(-limit.x, limit.x),
                                    (offset.y + pan.y).coerceIn(-limit.y, limit.y)
                                )
                            }
                        }
                ) {
                    if (viewport > 0f) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val s = baseScale * zoom
                            val w = source.width * s
                            val h = source.height * s
                            drawImage(
                                image = image,
                                dstOffset = IntOffset(
                                    ((viewport - w) / 2f + offset.x).roundToInt(),
                                    ((viewport - h) / 2f + offset.y).roundToInt()
                                ),
                                dstSize = IntSize(w.roundToInt(), h.roundToInt())
                            )

                            // Outside the circle is dimmed rather than hidden,
                            // so the user can see what they are cutting off as
                            // well as what they are keeping.
                            val radius = size.minDimension / 2f
                            val mask = Path().apply {
                                addRect(Rect(Offset.Zero, Size(size.width, size.height)))
                                addOval(Rect(center = center, radius = radius))
                                fillType = PathFillType.EvenOdd
                            }
                            drawPath(mask, Color.Black.copy(alpha = 0.55f))
                            drawCircle(
                                color = Color.White.copy(alpha = 0.9f),
                                radius = radius,
                                style = Stroke(width = 2f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onCancel) { Text("Cancel") }
                    TextButton(
                        enabled = viewport > 0f,
                        onClick = {
                            onConfirm(cropOut(source, viewport, baseScale * zoom, offset))
                        }
                    ) { Text("Use photo") }
                }
            }
        }
    }
}

/**
 * Maps the circular window back to a square of source pixels.
 *
 * A source pixel `p` is drawn at `(viewport - w) / 2 + offset + p * scale`, so
 * the pixel under the centre of the window is `source / 2 - offset / scale`,
 * and the window's half-width in source pixels is `viewport / 2 scale`.
 * Everything else here is keeping that square inside the bitmap.
 */
private fun cropOut(source: Bitmap, viewport: Float, scale: Float, offset: Offset): Bitmap {
    if (scale <= 0f || viewport <= 0f) return source

    val centreX = source.width / 2f - offset.x / scale
    val centreY = source.height / 2f - offset.y / scale
    val half = viewport / (2f * scale)

    val side = (half * 2f).roundToInt()
        .coerceAtLeast(1)
        .coerceAtMost(minOf(source.width, source.height))

    val left = (centreX - half).roundToInt().coerceIn(0, source.width - side)
    val top = (centreY - half).roundToInt().coerceIn(0, source.height - side)

    return Bitmap.createBitmap(source, left, top, side, side)
}
