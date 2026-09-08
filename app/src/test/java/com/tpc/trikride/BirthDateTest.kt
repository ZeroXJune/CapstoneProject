package com.tpc.trikride

import com.tpc.trikride.utils.BirthDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class BirthDateTest {

    private fun utcMidnight(y: Int, m: Int, d: Int): Long =
        Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            clear(); set(y, m - 1, d, 0, 0, 0)
        }.timeInMillis

    private fun localNoon(y: Int, m: Int, d: Int): Long =
        Calendar.getInstance().apply { clear(); set(y, m - 1, d, 12, 0, 0) }.timeInMillis

    /**
     * The picker hands back midnight UTC. Read in a zone behind UTC that instant
     * is the previous evening, which is how a birthday moves a day.
     */
    @Test
    fun `the picker's instant keeps its calendar date in any time zone`() {
        val picked = utcMidnight(2004, 8, 16)
        val zones = listOf("UTC", "Asia/Manila", "America/New_York", "Pacific/Honolulu", "Pacific/Kiritimati")
        val previous = TimeZone.getDefault()
        try {
            for (z in zones) {
                TimeZone.setDefault(TimeZone.getTimeZone(z))
                assertEquals("wrong date in $z", "2004-08-16", BirthDate.fromPickerUtc(picked))
            }
        } finally {
            TimeZone.setDefault(previous)
        }
    }

    @Test
    fun `the stored form is the same digits in any locale`() {
        val previous = Locale.getDefault()
        try {
            Locale.setDefault(Locale.forLanguageTag("ar-EG-u-nu-arab"))
            assertEquals("2004-08-16", BirthDate.fromPickerUtc(utcMidnight(2004, 8, 16)))
            Locale.setDefault(Locale.GERMANY)
            assertEquals("2004-08-16", BirthDate.fromPickerUtc(utcMidnight(2004, 8, 16)))
        } finally {
            Locale.setDefault(previous)
        }
    }

    @Test
    fun `age counts completed years`() {
        // Born 2004-08-16, so 22 on and after the 2026 birthday and 21 the day before.
        assertEquals(22, BirthDate.ageOn("2004-08-16", localNoon(2026, 8, 16)))
        assertEquals(21, BirthDate.ageOn("2004-08-16", localNoon(2026, 8, 15)))
        assertEquals(22, BirthDate.ageOn("2004-08-16", localNoon(2026, 9, 1)))
    }

    @Test
    fun `a date in the future is refused`() {
        assertEquals("That date is in the future.", BirthDate.reject("2090-01-01", localNoon(2026, 9, 8)))
        assertFalse(BirthDate.isPlausible("2090-01-01", localNoon(2026, 9, 8)))
    }

    @Test
    fun `too young is refused`() {
        assertTrue(BirthDate.reject("2020-01-01", localNoon(2026, 9, 8))!!.contains("at least 13"))
    }

    @Test
    fun `an implausible year is refused`() {
        assertTrue(BirthDate.reject("1850-01-01", localNoon(2026, 9, 8))!!.contains("over 120"))
    }

    @Test
    fun `a plausible date is accepted`() {
        assertNull(BirthDate.reject("2004-08-16", localNoon(2026, 9, 8)))
        assertTrue(BirthDate.isPlausible("2004-08-16", localNoon(2026, 9, 8)))
    }

    @Test
    fun `exactly the minimum age is accepted`() {
        assertNull(BirthDate.reject("2013-09-08", localNoon(2026, 9, 8)))
        assertTrue(BirthDate.reject("2013-09-09", localNoon(2026, 9, 8))!!.contains("at least 13"))
    }

    @Test
    fun `an unset or unreadable value is refused rather than accepted`() {
        assertEquals("Choose your date of birth.", BirthDate.reject("", localNoon(2026, 9, 8)))
        assertEquals("Choose your date of birth.", BirthDate.reject("Aug 16, 2004", localNoon(2026, 9, 8)))
        assertFalse(BirthDate.isPlausible("not-a-date"))
    }

    @Test
    fun `display renders a stored date and passes anything else through`() {
        assertEquals("16 August 2004", BirthDate.display("2004-08-16"))
        // Records written before the format changed are shown as they are
        // rather than as an error.
        assertEquals("Aug 16, 2004", BirthDate.display("Aug 16, 2004"))
    }
}
