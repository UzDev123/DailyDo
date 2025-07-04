package com.sultonuzdev.dailydo.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Complete color scheme for Daily Do app with our custom colors
private val DarkColorScheme = darkColorScheme(
    // 60% - Main background colors
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = DarkOnBackground,
    onSurface = DarkOnSurface,

    // 30% - Primary and container colors
    primary = DarkPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimary = DarkOnPrimary,
    onPrimaryContainer = DarkOnPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline,

    // 10% - Accent colors
    secondary = DarkSecondary,
    secondaryContainer = DarkSecondary.copy(alpha = 0.12f),
    onSecondary = DarkOnSecondary,
    onSecondaryContainer = DarkOnSecondary,

    tertiary = DarkTertiary,
    tertiaryContainer = DarkTertiary.copy(alpha = 0.12f),
    onTertiary = DarkOnTertiary,
    onTertiaryContainer = DarkOnTertiary
)

private val LightColorScheme = lightColorScheme(
    // 60% - Main background colors
    background = LightBackground,
    surface = LightSurface,
    onBackground = LightOnBackground,
    onSurface = LightOnSurface,

    // 30% - Primary and container colors
    primary = LightPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimary = LightOnPrimary,
    onPrimaryContainer = LightOnBackground,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline,

    // 10% - Accent colors
    secondary = LightSecondary,
    secondaryContainer = LightSecondary.copy(alpha = 0.12f),
    onSecondary = LightOnSecondary,
    onSecondaryContainer = LightOnBackground,

    tertiary = LightTertiary,
    tertiaryContainer = LightTertiary.copy(alpha = 0.12f),
    onTertiary = LightOnTertiary,
    onTertiaryContainer = LightOnBackground
)

@Composable
fun DailyDoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars =
                !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}