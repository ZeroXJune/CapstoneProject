package com.tpc.trikride

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.tpc.trikride.ui.screens.MainAppScreen
import com.tpc.trikride.ui.theme.ThemeState
import com.tpc.trikride.ui.theme.TrikRideTheme
import com.tpc.trikride.utils.CacheCleanup

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Camera captures and exported reports are written to the cache and were
        // never removed. A licence photograph and a report naming every user are
        // both things that should not outlive the action that made them.
        CacheCleanup.sweep(cacheDir)

        setContent {
            // Follow the system theme unless the user has overridden it in Settings.
            val darkTheme = ThemeState.darkModeOverride ?: isSystemInDarkTheme()

            TrikRideTheme(darkTheme = darkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainAppScreen()
                }
            }
        }
    }
}
