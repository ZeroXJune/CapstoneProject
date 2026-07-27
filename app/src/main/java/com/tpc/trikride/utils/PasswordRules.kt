package com.tpc.trikride.utils

/** Password strength rules used on the Register screen. */
object PasswordRules {

    data class Check(val label: String, val passed: Boolean)

    fun evaluate(password: String): List<Check> = listOf(
        Check("At least 8 characters", password.length >= 8),
        Check("An uppercase letter (A–Z)", password.any { it.isUpperCase() }),
        Check("A lowercase letter (a–z)", password.any { it.isLowerCase() }),
        Check("A number (0–9)", password.any { it.isDigit() }),
        Check("A symbol (!@#\$…)", password.any { !it.isLetterOrDigit() })
    )

    /** Strong enough when every rule except the (optional) symbol rule passes. */
    fun isStrong(password: String): Boolean {
        val checks = evaluate(password)
        // Require the first four; the symbol is recommended but not mandatory.
        return checks.take(4).all { it.passed }
    }
}
