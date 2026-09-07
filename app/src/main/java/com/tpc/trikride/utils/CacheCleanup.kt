package com.tpc.trikride.utils

import java.io.File

/**
 * Clears the working files the app leaves in its cache directory.
 *
 * Two directories collect things that are only ever needed for a moment.
 * `images/` holds the full-resolution frame the camera writes before it is
 * shrunk and encoded — for a driver's licence that is a photograph of a
 * government identity document. `reports/` holds an exported PDF or spreadsheet
 * on its way to the share sheet, carrying the name, email and telephone number
 * of every user in the system.
 *
 * Neither was ever deleted. The database copy of a licence goes when the driver
 * withdraws it, and the app tells them so in as many words, while the original
 * capture stayed on the phone indefinitely. Android reclaims a cache directory
 * only under storage pressure, which is not a retention policy.
 *
 * Takes a [File] rather than a Context so the rule can be tested without a
 * device. An hour is long enough that nothing in flight is removed — a capture
 * is consumed the moment the camera returns — and short enough that a document
 * does not outlive the session that made it.
 */
object CacheCleanup {

    /** Directories holding files that exist only for the length of one action. */
    private val TRANSIENT = listOf("images", "reports")

    const val DEFAULT_MAX_AGE_MS: Long = 60 * 60 * 1000L

    /**
     * Deletes transient files last modified more than [maxAgeMs] before [now].
     * Returns how many were removed. Never throws: a cache that cannot be swept
     * is not a reason to fail a launch.
     */
    fun sweep(
        cacheDir: File,
        now: Long = System.currentTimeMillis(),
        maxAgeMs: Long = DEFAULT_MAX_AGE_MS
    ): Int {
        var removed = 0
        for (name in TRANSIENT) {
            val dir = File(cacheDir, name)
            val files = runCatching { dir.listFiles() }.getOrNull() ?: continue
            for (file in files) {
                if (!file.isFile) continue
                if (now - file.lastModified() < maxAgeMs) continue
                if (runCatching { file.delete() }.getOrDefault(false)) removed++
            }
        }
        return removed
    }
}
