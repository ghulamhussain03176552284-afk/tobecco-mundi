package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = MandiGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = MandiGreenLight,
    onPrimaryContainer = MandiGreenDark,
    secondary = TobaccoGoldPrimary,
    onSecondary = Color.White,
    secondaryContainer = TobaccoGoldLight,
    onSecondaryContainer = TobaccoGoldDark,
    tertiary = TobaccoGoldDark,
    onTertiary = Color.White,
    background = MandiBackground,
    onBackground = MandiTextPrimary,
    surface = MandiSurface,
    onSurface = MandiTextPrimary,
    surfaceVariant = Color(0xFFF1F5F0),
    onSurfaceVariant = MandiTextSecondary,
    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4CAF50),
    onPrimary = Color(0xFF00390E),
    primaryContainer = Color(0xFF1B5E20),
    onPrimaryContainer = Color(0xFFA5D6A7),
    secondary = Color(0xFFFBBF24),
    onSecondary = Color(0xFF451A03),
    secondaryContainer = Color(0xFF78350F),
    onSecondaryContainer = Color(0xFFFDE68A),
    background = DarkMandiBackground,
    onBackground = DarkMandiTextPrimary,
    surface = DarkMandiSurface,
    onSurface = DarkMandiTextPrimary,
    surfaceVariant = DarkMandiSurfaceVariant,
    onSurfaceVariant = DarkMandiTextSecondary,
    outline = Color(0xFF334155),
    outlineVariant = Color(0xFF1E293B)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // We prefer the authentic Tobacco Mandi color palette for brand consistency
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
