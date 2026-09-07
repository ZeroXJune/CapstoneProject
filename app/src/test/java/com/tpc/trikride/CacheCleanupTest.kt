package com.tpc.trikride

import com.tpc.trikride.utils.CacheCleanup
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class CacheCleanupTest {

    @get:Rule
    val temp = TemporaryFolder()

    private fun file(dir: String, name: String, ageMs: Long): File {
        val d = File(temp.root, dir).apply { mkdirs() }
        return File(d, name).apply {
            writeText("x")
            setLastModified(System.currentTimeMillis() - ageMs)
        }
    }

    @Test
    fun `removes a licence capture left behind by the camera`() {
        val old = file("images", "licence.jpg", 2 * CacheCleanup.DEFAULT_MAX_AGE_MS)
        assertEquals(1, CacheCleanup.sweep(temp.root))
        assertFalse(old.exists())
    }

    @Test
    fun `removes an exported report carrying everyone's details`() {
        val old = file("reports", "trikride-rides-2026-03.csv", 2 * CacheCleanup.DEFAULT_MAX_AGE_MS)
        assertEquals(1, CacheCleanup.sweep(temp.root))
        assertFalse(old.exists())
    }

    @Test
    fun `leaves a capture that may still be on its way to the encoder`() {
        val fresh = file("images", "fresh.jpg", 0)
        assertEquals(0, CacheCleanup.sweep(temp.root))
        assertTrue(fresh.exists())
    }

    @Test
    fun `leaves directories it does not own`() {
        val tiles = file("osmdroid", "tile.png", 10 * CacheCleanup.DEFAULT_MAX_AGE_MS)
        CacheCleanup.sweep(temp.root)
        assertTrue("the map tile cache is not ours to clear", tiles.exists())
    }

    @Test
    fun `an absent cache directory is not an error`() {
        assertEquals(0, CacheCleanup.sweep(File(temp.root, "nothing-here")))
    }

    @Test
    fun `counts every file it removes`() {
        file("images", "a.jpg", 2 * CacheCleanup.DEFAULT_MAX_AGE_MS)
        file("images", "b.jpg", 2 * CacheCleanup.DEFAULT_MAX_AGE_MS)
        file("reports", "c.pdf", 2 * CacheCleanup.DEFAULT_MAX_AGE_MS)
        assertEquals(3, CacheCleanup.sweep(temp.root))
    }

    @Test
    fun `a subdirectory is left alone`() {
        val d = File(temp.root, "images/nested").apply { mkdirs() }
        d.setLastModified(System.currentTimeMillis() - 10 * CacheCleanup.DEFAULT_MAX_AGE_MS)
        CacheCleanup.sweep(temp.root)
        assertTrue(d.exists())
    }
}
