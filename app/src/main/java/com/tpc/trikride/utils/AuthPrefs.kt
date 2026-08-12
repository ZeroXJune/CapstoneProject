package com.tpc.trikride.utils

import android.content.Context

/** Small local store for the "remember me" email prefill on the login screen. */
object AuthPrefs {
    private const val FILE = "trikride_auth"
    private const val KEY_EMAIL = "remembered_email"
    private const val KEY_SEEN_ONBOARDING = "seen_onboarding"

    private fun prefs(context: Context) =
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    fun rememberedEmail(context: Context): String =
        prefs(context).getString(KEY_EMAIL, "").orEmpty()

    fun setRememberedEmail(context: Context, email: String) {
        prefs(context).edit().putString(KEY_EMAIL, email).apply()
    }

    fun clearRememberedEmail(context: Context) {
        prefs(context).edit().remove(KEY_EMAIL).apply()
    }

    /** The onboarding carousel is shown once, on the first launch. */
    fun hasSeenOnboarding(context: Context): Boolean =
        prefs(context).getBoolean(KEY_SEEN_ONBOARDING, false)

    fun setSeenOnboarding(context: Context) {
        prefs(context).edit().putBoolean(KEY_SEEN_ONBOARDING, true).apply()
    }
}
