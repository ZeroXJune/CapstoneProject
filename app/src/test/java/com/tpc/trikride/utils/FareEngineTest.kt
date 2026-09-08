package com.tpc.trikride.utils

import com.tpc.trikride.models.FareConfig
import com.tpc.trikride.models.FareStop
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FareEngineTest {

    private val config = FareConfig(
        minimumRegular = 25.0,
        minimumDiscounted = 20.0,
        chargePerPassenger = true
    )

    private val stop = FareStop(
        id = "stop1",
        zone = "Zone 1",
        name = "Test Stop",
        regularFare = 30.0,
        discountedFare = 24.0
    )

    @Test
    fun `mixed party prices each column separately`() {
        val quote = FareEngine.quote(config, stop, regularCount = 2, discountedCount = 1)

        assertEquals(30.0, quote.regularRate, 0.001)
        assertEquals(24.0, quote.discountedRate, 0.001)
        assertEquals(2 * 30.0 + 1 * 24.0, quote.total, 0.001)
        assertEquals(3, quote.passengers)
        assertEquals("2 regular, 1 discounted", quote.partyLabel)
    }

    @Test
    fun `all regular party charges only the regular column`() {
        val quote = FareEngine.quote(config, stop, regularCount = 3, discountedCount = 0)

        assertEquals(3 * 30.0, quote.total, 0.001)
        assertEquals("3 regular", quote.partyLabel)
    }

    @Test
    fun `all discounted party charges only the discounted column`() {
        val quote = FareEngine.quote(config, stop, regularCount = 0, discountedCount = 4)

        assertEquals(4 * 24.0, quote.total, 0.001)
        assertEquals("4 discounted", quote.partyLabel)
    }

    @Test
    fun `each column is raised to its own minimum before multiplying`() {
        val cheapStop = stop.copy(regularFare = 15.0, discountedFare = 10.0)

        val quote = FareEngine.quote(config, cheapStop, regularCount = 1, discountedCount = 1)

        assertEquals(25.0, quote.regularRate, 0.001)
        assertEquals(20.0, quote.discountedRate, 0.001)
        assertEquals(45.0, quote.total, 0.001)
        assertTrue(quote.minimumApplied)
    }

    @Test
    fun `minimumApplied is false when posted rates already clear the minimums`() {
        val quote = FareEngine.quote(config, stop, regularCount = 1, discountedCount = 1)

        assertFalse(quote.minimumApplied)
    }

    @Test
    fun `flat rate stops are not charged per head`() {
        val flatConfig = config.copy(chargePerPassenger = false)
        val flatStop = stop.copy(regularFare = 25.0, discountedFare = 25.0)

        val allRegular = FareEngine.quote(flatConfig, flatStop, regularCount = 3, discountedCount = 0)
        val allDiscounted = FareEngine.quote(flatConfig, flatStop, regularCount = 0, discountedCount = 3)
        val mixed = FareEngine.quote(flatConfig, flatStop, regularCount = 2, discountedCount = 1)

        assertEquals(25.0, allRegular.total, 0.001)
        assertEquals(25.0, allDiscounted.total, 0.001)
        assertEquals(
            "a mixed party on a non-per-head stop should be charged the regular rate, not undercharged at the discounted rate",
            25.0,
            mixed.total,
            0.001
        )
    }

    @Test
    fun `negative counts are treated as zero`() {
        val quote = FareEngine.quote(config, stop, regularCount = -1, discountedCount = 2)

        assertEquals(0, quote.regularCount)
        assertEquals(2, quote.discountedCount)
        assertEquals(2 * 24.0, quote.total, 0.001)
    }
}
