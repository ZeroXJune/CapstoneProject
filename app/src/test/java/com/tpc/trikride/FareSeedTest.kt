package com.tpc.trikride

import com.tpc.trikride.models.FareConfig
import com.tpc.trikride.utils.FareEngine
import com.tpc.trikride.utils.FareSeed
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The seeded fare table is transcribed from a photograph of a laminated sheet,
 * so it is data that can be wrong in ways only a check will catch. These guard
 * the properties that must hold whatever the sheet says.
 */
class FareSeedTest {

    private val config = FareConfig()

    @Test
    fun `every stop has a unique id`() {
        val ids = FareSeed.STOPS.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun `no stop is nameless or zoneless`() {
        FareSeed.STOPS.forEach {
            assertTrue("blank name on ${it.id}", it.name.isNotBlank())
            assertTrue("blank zone on ${it.id}", it.zone.isNotBlank())
        }
    }

    @Test
    fun `no stop carries a negative rate`() {
        FareSeed.STOPS.forEach {
            assertTrue("negative regular on ${it.id}", it.regularFare >= 0.0)
            assertTrue("negative discounted on ${it.id}", it.discountedFare >= 0.0)
        }
    }

    /**
     * The one that matters. Whatever the transcription says, no priced booking
     * may charge the discounted column more than the regular one.
     */
    @Test
    fun `no bookable stop ever prices a discounted passenger above a regular one`() {
        FareSeed.STOPS.filter { it.active }.forEach { stop ->
            val quote = FareEngine.quote(config, stop, regularCount = 1, discountedCount = 1)
            assertTrue(
                "${stop.id} prices discounted at ${quote.discountedRate} " +
                    "against regular ${quote.regularRate}",
                quote.discountedRate <= quote.regularRate
            )
        }
    }

    /**
     * Documents the rows the transcription got wrong, so that correcting the
     * table in the admin screen — or re-transcribing the sheet — shows up here
     * rather than passing silently.
     */
    @Test
    fun `the known inverted rows are the only inverted rows`() {
        val inverted = FareSeed.STOPS
            .filter { it.active && it.discountedFare > it.regularFare }
            .map { it.id }
            .toSet()
        assertEquals(
            setOf(
                "balintawak__ka_ano_garcia",
                "san_isidro__mar_auguis",
                "san_roque__dancy",
                "san_roque__arlen_to_centro_special_trip"
            ),
            inverted
        )
    }

    @Test
    fun `a stop with no usable rate is not bookable`() {
        FareSeed.STOPS.filter { it.regularFare == 0.0 && it.discountedFare == 0.0 }
            .forEach { assertTrue("${it.id} is active with no rate", !it.active) }
    }

    @Test
    fun `every zone named on the sheet has at least one stop`() {
        val zones = FareSeed.STOPS.map { it.zone }.toSet()
        FareSeed.ZONES.forEach { assertTrue("no stops in $it", it in zones) }
    }

    @Test
    fun `no stop uses a zone the sheet does not list`() {
        FareSeed.STOPS.forEach {
            assertTrue("${it.id} is in unlisted zone ${it.zone}", it.zone in FareSeed.ZONES)
        }
    }

    @Test
    fun `a stop without coordinates reports that it has none`() {
        FareSeed.STOPS.forEach {
            assertEquals(
                it.latitude != 0.0 || it.longitude != 0.0,
                it.hasCoordinates
            )
        }
    }

    @Test
    fun `the table is the two hundred and forty rows the sheet holds`() {
        assertEquals(240, FareSeed.STOPS.size)
    }
}
