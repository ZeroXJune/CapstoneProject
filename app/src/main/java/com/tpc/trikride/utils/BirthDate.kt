package com.tpc.trikride.utils

import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

/**
 * The date of birth, stored as a date rather than as a sentence.
 *
 * It used to be written with `SimpleDateFormat("MMM d, yyyy", Locale.getDefault())`
 * straight into `users/{uid}/birthDate`, so two phones in two locales stored two
 * different, mutually unparseable strings for the same day, and a locale with
 * non-Latin digits stored something nothing here reads back. Birthdate is the
 * only basis the system has for a senior's entitlement, so it has to be a value
 * and not prose.
 *
 * Material's date picker reports a selection as midnight UTC on the chosen day.
 * Rendering that instant in the device's zone moves it to the previous day
 * anywhere behind UTC. `ReportPeriod.customRange` already got this right for
 * report ranges; this is the same reading applied where it was missed.
 */
object BirthDate {

    /** Nobody younger than this may hold an account. */
    const val MINIMUM_AGE = 13

    /** Above this, the entry is a mis-scroll rather than a birthday. */
    const val MAXIMUM_AGE = 120

    private val MONTHS = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    /** Turns the picker's UTC instant into a plain `yyyy-MM-dd`. */
    fun fromPickerUtc(pickedUtcMillis: Long): String {
        val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            timeInMillis = pickedUtcMillis
        }
        return "%04d-%02d-%02d".format(
            Locale.US,
            utc.get(Calendar.YEAR),
            utc.get(Calendar.MONTH) + 1,
            utc.get(Calendar.DAY_OF_MONTH)
        )
    }

    /** "16 August 2004", for showing. Returns the input unchanged if it is not a stored date. */
    fun display(stored: String): String {
        val parts = parse(stored) ?: return stored
        val (y, m, d) = parts
        return "%d %s %d".format(Locale.US, d, MONTHS[m - 1], y)
    }

    /** Whether this is a date somebody could actually have been born on. */
    fun isPlausible(stored: String, now: Long = System.currentTimeMillis()): Boolean {
        val age = ageOn(stored, now) ?: return false
        return age in MINIMUM_AGE..MAXIMUM_AGE
    }

    /** Completed years as at [now], or null if [stored] is not a date. */
    fun ageOn(stored: String, now: Long = System.currentTimeMillis()): Int? {
        val (y, m, d) = parse(stored) ?: return null
        val today = Calendar.getInstance().apply { timeInMillis = now }
        var age = today.get(Calendar.YEAR) - y
        val monthNow = today.get(Calendar.MONTH) + 1
        val dayNow = today.get(Calendar.DAY_OF_MONTH)
        if (monthNow < m || (monthNow == m && dayNow < d)) age--
        return age
    }

    /** Why the chosen date is refused, or null when it is fine. */
    fun reject(stored: String, now: Long = System.currentTimeMillis()): String? {
        val age = ageOn(stored, now) ?: return "Choose your date of birth."
        return when {
            age < 0 -> "That date is in the future."
            age < MINIMUM_AGE -> "You need to be at least $MINIMUM_AGE to use TrikRide."
            age > MAXIMUM_AGE -> "Check the year — that date is over $MAXIMUM_AGE years ago."
            else -> null
        }
    }

    private fun parse(stored: String): Triple<Int, Int, Int>? {
        val parts = stored.split("-")
        if (parts.size != 3) return null
        val y = parts[0].toIntOrNull() ?: return null
        val m = parts[1].toIntOrNull() ?: return null
        val d = parts[2].toIntOrNull() ?: return null
        if (m !in 1..12 || d !in 1..31) return null
        return Triple(y, m, d)
    }
}
