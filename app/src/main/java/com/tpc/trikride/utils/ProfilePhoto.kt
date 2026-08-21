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
 * Profile photos, small enough to live in the Realtime Database.
 *
 * Firebase Cloud Storage is the natural home for an image, but new Firebase
 * projects need a paid plan before Storage can be provisioned, and this project
 * runs on the free tier. An avatar shown at 96dp does not need a full-resolution
 * file: squared off at 256 pixels and compressed, it comes to a few kilobytes,
 * which the database holds without complaint.
 *
 * Photos are written to their own top-level node rather than into the user
 * record, so that reading a list of users — which the admin screens do
 * constantly — does not drag every avatar across the network with it.
 */
object ProfilePhoto {

    /** Avatars render at 96dp; 256 square is generous even on a dense screen. */
    private const val TARGET_PX = 256

    /** Compression is retried down this ladder until the result fits. */
    private val QUALITY_STEPS = intArrayOf(70, 55, 40, 25)

    /** Roughly 24 KB of base64, which is about 18 KB of JPEG. */
    private const val MAX_ENCODED_BYTES = 24_000

    /**
     * Reads the image at [uri], squares and shrinks it, and returns it as a
     * base64 JPEG. Returns null if the image cannot be read or cannot be
     * compressed small enough.
     */
    fun encode(context: Context, uri: Uri): String? {
        val source = decodeScaled(context, uri) ?: return null
        val square = cropToSquare(source)
        val scaled = Bitmap.createScaledBitmap(square, TARGET_PX, TARGET_PX, true)
        if (square !== source) source.recycle()

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
     * Loads the image at a reduced sample size. A modern phone camera produces
     * something in the region of 4000 pixels wide, and decoding that at full
     * size to make a 256-pixel thumbnail is how an app runs out of memory.
     */
    private fun decodeScaled(context: Context, uri: Uri): Bitmap? {
        // decodeStream returns null whenever inJustDecodeBounds is set — that is
        // its contract, not a failure — so whether the image could be read has
        // to be judged from the size it wrote into the options. Testing the
        // return value here instead rejects every image ever chosen.
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, bounds)
        }
        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null

        var sample = 1
        while (bounds.outWidth / sample > TARGET_PX * 2 &&
            bounds.outHeight / sample > TARGET_PX * 2
        ) {
            sample *= 2
        }

        val options = BitmapFactory.Options().apply { inSampleSize = sample }
        return context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, options)
        }
    }

    private fun cropToSquare(bitmap: Bitmap): Bitmap {
        val side = minOf(bitmap.width, bitmap.height)
        if (bitmap.width == bitmap.height) return bitmap
        val x = (bitmap.width - side) / 2
        val y = (bitmap.height - side) / 2
        return Bitmap.createBitmap(bitmap, x, y, side, side)
    }
}
