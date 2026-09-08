package com.tpc.trikride.ui.theme

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tpc.trikride.utils.AuthPrefs

/**
 * App-wide theme override. `null` means "follow the system setting";
 * `true`/`false` force dark/light. Held as Compose state so that toggling
 * it from the Settings screen recomposes the whole app instantly, and written
 * through to preferences so the choice survives a launch — before, it was a
 * process-global and every restart silently reverted it.
 */
object ThemeState {
    var darkModeOverride by mutableStateOf<Boolean?>(null)
        private set

    /** Called once at start-up, before the first frame is composed. */
    fun load(context: Context) {
        darkModeOverride = AuthPrefs.darkModeOverride(context)
    }

    fun set(context: Context, dark: Boolean?) {
        darkModeOverride = dark
        AuthPrefs.setDarkModeOverride(context, dark)
    }
}
