package com.tpc.trikride.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * App-wide theme override. `null` means "follow the system setting";
 * `true`/`false` force dark/light. Held as Compose state so that toggling
 * it from the Settings screen recomposes the whole app instantly.
 */
object ThemeState {
    var darkModeOverride by mutableStateOf<Boolean?>(null)
}
