package com.example.safespot.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun SafeSpotTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = PrimaryBlue,
            secondary = AccentOrange,
            background = LightGrayBackground
        )
    } else {
        lightColorScheme(
            primary = PrimaryBlue,
            secondary = AccentOrange,
            background = LightGrayBackground
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
