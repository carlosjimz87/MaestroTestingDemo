package com.carlosjimz87.maestrotestingdemo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Teal80,
    secondary = TealGrey80,
    tertiary = Amber80
)

private val LightColorScheme = lightColorScheme(
    primary = Teal40,
    secondary = TealGrey40,
    tertiary = Amber40
)

@Composable
fun MaestroTestingDemoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Dynamic color disabled for consistent AI visual assertions
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
