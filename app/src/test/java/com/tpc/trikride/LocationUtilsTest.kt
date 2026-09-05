package com.tpc.trikride

import com.tpc.trikride.models.Location
import com.tpc.trikride.utils.LocationUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LocationUtilsTest {

    private val talibon = Location(10.1531, 124.3251)

    @Test
    fun `the distance from a point to itself is zero`() {
        assertEquals(0.0, LocationUtils.distanceKm(talibon, talibon), 0.0001)
    }

    @Test
    fun `a degree of latitude is about a hundred and eleven kilometres`() {
        val north = talibon.copy(latitude = talibon.latitude + 1.0)
        assertEquals(111.2, LocationUtils.distanceKm(talibon, north), 1.0)
    }

    @Test
    fun `distance is symmetric`() {
        val other = Location(9.8, 124.1)
        assertEquals(
            LocationUtils.distanceKm(talibon, other),
            LocationUtils.distanceKm(other, talibon),
            0.0001
        )
    }

    @Test
    fun `nearest picks the closest candidate`() {
        val near = Location(10.1540, 124.3260)
        val far = Location(11.0, 125.0)
        assertEquals(near, LocationUtils.nearest(talibon, listOf(far, near)))
    }

    @Test
    fun `nearest returns null for an empty list`() {
        assertNull(LocationUtils.nearest(talibon, emptyList()))
    }

    /**
     * A default Location is 0,0 — a point in the Atlantic — so anything that
     * plots one has to be able to tell it apart from a real position.
     */
    @Test
    fun `a default location reports no coordinates`() {
        assertTrue(!Location().hasCoordinates)
        assertTrue(talibon.hasCoordinates)
        assertTrue(Location(latitude = 0.0, longitude = 124.0).hasCoordinates)
    }
}
