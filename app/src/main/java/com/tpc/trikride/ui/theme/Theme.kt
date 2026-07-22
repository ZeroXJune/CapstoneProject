package com.tpc.trikride.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = GreenBright,
    onPrimary = Color.White,
    secondary = AccentBlueDark,
    onSecondary = Color.White,
    tertiary = GreenBright,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkCard,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkDivider,
    error = ErrorColor,
    onError = Color.White,
    primaryContainer = ForestGreen,
    onPrimaryContainer = Color.White,
    secondaryContainer = DarkSurface,
    onSecondaryContainer = DarkTextPrimary,
    errorContainer = Color(0xFF3B1218),
    onErrorContainer = Color(0xFFFECACA)
)

private val LightColorScheme = lightColorScheme(
    primary = EmeraldGreen,
    onPrimary = Color.White,
    secondary = AccentBlue,
    onSecondary = Color.White,
    tertiary = ForestGreen,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightCard,
    onSurfaceVariant = LightTextSecondary,
    outline = LightDivider,
    error = ErrorColor,
    onError = Color.White,
    primaryContainer = Color(0xFFDCFCE7),
    onPrimaryContainer = ForestGreen,
    secondaryContainer = Color(0xFFEFF6FF),
    onSecondaryContainer = AccentBlue,
    errorContainer = Color(0xFFFEE2E2),
    onErrorContainer = Color(0xFF991B1B)
)

/**
 * TrikRide theme. Brand colors are used directly (dynamic Material You color
 * is intentionally disabled) so the emerald-green identity is consistent on
 * every device. Follows the system light/dark setting by default.
 */
@Composable
fun TrikRideTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
