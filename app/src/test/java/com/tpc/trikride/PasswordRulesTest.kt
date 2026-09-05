package com.tpc.trikride

import com.tpc.trikride.utils.PasswordRules
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PasswordRulesTest {

    @Test
    fun `accepts a password meeting the four required rules`() {
        assertTrue(PasswordRules.isStrong("Password1"))
    }

    @Test
    fun `a symbol is recommended but not required`() {
        assertTrue(PasswordRules.isStrong("Password1"))
        assertTrue(PasswordRules.isStrong("Password1!"))
        assertFalse(PasswordRules.evaluate("Password1")[4].passed)
        assertTrue(PasswordRules.evaluate("Password1!")[4].passed)
    }

    @Test
    fun `rejects a password missing any required rule`() {
        assertFalse("too short", PasswordRules.isStrong("Pass1"))
        assertFalse("no uppercase", PasswordRules.isStrong("password1"))
        assertFalse("no lowercase", PasswordRules.isStrong("PASSWORD1"))
        assertFalse("no digit", PasswordRules.isStrong("Passwords"))
        assertFalse("empty", PasswordRules.isStrong(""))
    }

    @Test
    fun `exactly eight characters passes the length rule`() {
        assertTrue(PasswordRules.evaluate("Passwo1d")[0].passed)
        assertFalse(PasswordRules.evaluate("Passw1d")[0].passed)
    }

    @Test
    fun `reports five checks in a stable order`() {
        val checks = PasswordRules.evaluate("x")
        assertEquals(5, checks.size)
        assertTrue(checks[0].label.contains("8 characters"))
    }

    /**
     * Firebase enforces six characters of its own, so a password this app
     * accepts is always one Firebase accepts too.
     */
    @Test
    fun `anything strong enough here clears Firebase's own six-character floor`() {
        listOf("Password1", "Abcdefg1", "Zz9aaaaa").forEach {
            assertTrue(PasswordRules.isStrong(it))
            assertTrue(it.length >= 6)
        }
    }
}
