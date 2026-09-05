package com.tpc.trikride

import com.tpc.trikride.models.FareConfig
import com.tpc.trikride.models.FareStop
import com.tpc.trikride.models.FareType
import com.tpc.trikride.utils.FareEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pricing is the part of this app that takes money off people, so it is the
 * part that gets tested first.
 */
class FareEngineTest {

    private val config = FareConfig()
    private fun stop(regular: Double, discounted: Double) =
        FareStop(id = "t", zone = "Z", name = "N", regularFare = regular, discountedFare = discounted)

    @Test
    fun `charges the posted rate per head`() {
        val quote = FareEngine.quote(config, stop(40.0, 32.0), regularCount = 2, discountedCount = 1)
        assertEquals(40.0, quote.regularRate, 0.001)
        assertEquals(32.0, quote.discountedRate, 0.001)
        assertEquals(112.0, quote.total, 0.001)
        assertEquals(3, quote.passengers)
    }

    @Test
    fun `raises a rate below the ordinance minimum to the minimum`() {
        val quote = FareEngine.quote(config, stop(20.0, 16.0), regularCount = 1, discountedCount = 1)
        assertEquals(25.0, quote.regularRate, 0.001)
        assertEquals(20.0, quote.discountedRate, 0.001)
        assertEquals(45.0, quote.total, 0.001)
        assertTrue(quote.minimumApplied)
    }

    @Test
    fun `does not flag the minimum when the posted rate is above it`() {
        assertFalse(FareEngine.quote(config, stop(40.0, 32.0), 1, 0).minimumApplied)
    }

    /**
     * Three rows of the transcribed table carry a discounted rate above the
     * regular one. Charging a senior, a person with a disability or a student
     * more than the passenger beside them is what RA 9994 and RA 10754 forbid,
     * so the engine clamps rather than trusting the table.
     */
    @Test
    fun `never charges a discounted passenger more than a regular one`() {
        // san_roque__arlen_to_centro_special_trip, as transcribed.
        val quote = FareEngine.quote(config, stop(60.0, 80.0), regularCount = 1, discountedCount = 1)
        assertEquals(60.0, quote.regularRate, 0.001)
        assertEquals(60.0, quote.discountedRate, 0.001)
        assertEquals(120.0, quote.total, 0.001)
    }

    @Test
    fun `clamps an inverted rate that an administrator types in`() {
        val quote = FareEngine.quote(config, stop(30.0, 999.0), regularCount = 0, discountedCount = 2)
        assertEquals(60.0, quote.total, 0.001)
    }

    @Test
    fun `prices one tricycle when the sheet is not charged per head`() {
        val flat = config.copy(chargePerPassenger = false)
        assertEquals(40.0, FareEngine.quote(flat, stop(40.0, 32.0), 2, 1).total, 0.001)
        // Everybody aboard entitled to the discount pays the discounted rate.
        assertEquals(32.0, FareEngine.quote(flat, stop(40.0, 32.0), 0, 3).total, 0.001)
    }

    @Test
    fun `treats a negative count as none`() {
        val quote = FareEngine.quote(config, stop(40.0, 32.0), regularCount = -5, discountedCount = 1)
        assertEquals(0, quote.regularCount)
        assertEquals(32.0, quote.total, 0.001)
    }

    @Test
    fun `an empty party costs nothing`() {
        assertEquals(0.0, FareEngine.quote(config, stop(40.0, 32.0), 0, 0).total, 0.001)
    }

    @Test
    fun `a stop with no rate at all still respects the minimum`() {
        val quote = FareEngine.quote(config, stop(0.0, 0.0), 1, 0)
        assertEquals(25.0, quote.total, 0.001)
    }

    @Test
    fun `flat stops carry the configured flat rates`() {
        val flats = FareEngine.flatStops(config)
        assertEquals(2, flats.size)
        assertEquals(config.poblacionFlat, flats[0].regularFare, 0.001)
        assertEquals(config.terminalRoundTrip, flats[1].regularFare, 0.001)
    }

    @Test
    fun `party label describes a mixed booking`() {
        assertEquals(
            "2 regular, 1 discounted",
            FareEngine.quote(config, stop(40.0, 32.0), 2, 1).partyLabel
        )
        assertEquals("no passengers", FareEngine.quote(config, stop(40.0, 32.0), 0, 0).partyLabel)
    }

    @Test
    fun `minimum lookup follows the rate column`() {
        assertEquals(25.0, FareEngine.minimumFor(config, FareType.REGULAR), 0.001)
        assertEquals(20.0, FareEngine.minimumFor(config, FareType.DISCOUNTED), 0.001)
    }
}
