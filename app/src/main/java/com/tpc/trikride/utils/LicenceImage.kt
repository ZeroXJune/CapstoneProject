package com.tpc.trikride.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream

/**
 * The photograph of a driver's licence, sized so an administrator can read it.
 *
 * This is the same trick as [ProfilePhoto] — base64 in the Realtime Database,
 * because Cloud Storage needs the paid plan — but with different targets. An
 * avatar only has to look like the person at 96dp. A licence has to be legible:
 * the administrator is checking the number, the name and the expiry date
 * against what the driver typed, and a 256-pixel thumbnail cannot carry that.
 *
 * So the long edge goes to 1280 pixels and the budget rises to about 200 KB.
 * Aspect ratio is kept, because a licence is a card and squaring it off would
 * cut the ends of the number.
 */
object LicenceImage {

    /** Enough to read a licence number photographed at arm's length. */
    private const val TARGET_LONG_EDGE = 1280

    /** Compression is retried down this ladder until the result fits. */
    private val QUALITY_STEPS = intArrayOf(80, 70, 60, 50, 40)

    /** Roughly 200 KB of base64, which is about 150 KB of JPEG. */
    private const val MAX_ENCODED_BYTES = 200_000

    /**
     * Reads the image at [uri], scales its long edge to [TARGET_LONG_EDGE], and
     * returns it as a base64 JPEG. Returns null if the image cannot be read, or
     * cannot be compressed small enough to store.
     */
    fun encode(context: Context, uri: Uri): String? {
        val source = decodeScaled(context, uri) ?: return null
        val scaled = scaleToLongEdge(source)
        if (scaled !== source) source.recycle()

        for (quality in QUALITY_STEPS) {
            val bytes = ByteArrayOutputStream().use { out ->
                scaled.compress(Bitmap.CompressFormat.JPEG, quality, out)
                out.toByteArray()
            }
            val encoded = Base64.encodeToString(bytes, Base64.NO_WRAP)
            if (encoded.length <= MAX_ENCODED_BYTES) {
                scaled.recycle()
                return encoded
            }
        }
        scaled.recycle()
        return null
    }

    fun decode(data: String?): ImageBitmap? {
        if (data.isNullOrBlank()) return null
        return try {
            val bytes = Base64.decode(data, Base64.NO_WRAP)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Loads at a reduced sample size first. A phone camera photograph of a
     * licence is several thousand pixels wide, and decoding that at full size
     * before shrinking it is how an app runs out of memory.
     */
    private fun decodeScaled(context: Context, uri: Uri): Bitmap? {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, bounds)
        } ?: return null

        var sample = 1
        while (maxOf(bounds.outWidth, bounds.outHeight) / sample > TARGET_LONG_EDGE * 2) {
            sample *= 2
        }

        val options = BitmapFactory.Options().apply { inSampleSize = sample }
        return context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, options)
        }
    }

    /** Never enlarges: a small photograph stays small rather than going soft. */
    private fun scaleToLongEdge(bitmap: Bitmap): Bitmap {
        val longEdge = maxOf(bitmap.width, bitmap.height)
        if (longEdge <= TARGET_LONG_EDGE) return bitmap
        val ratio = TARGET_LONG_EDGE.toFloat() / longEdge
        return Bitmap.createScaledBitmap(
            bitmap,
            (bitmap.width * ratio).toInt().coerceAtLeast(1),
            (bitmap.height * ratio).toInt().coerceAtLeast(1),
            true
        )
    }
}
