package com.example.buildthehouse.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Background = Color(0xFFF4F3EF)
val Ground = Color(0xFFBFC9C2)
val Outline = Color(0xFF1B2420)
val Roof = Color(0xFF3B7A4F)
val Window = Color(0xFFB3E5C5)
val Door = Color(0xFF1B2420)
val TopBar = Color(0xFFE3EFE7)

private val LightColorScheme = lightColorScheme(
    primary = Roof,
    onPrimary = Color.White,
    primaryContainer = Roof,
    onPrimaryContainer = Color.White,
    background = Background,
    surface = Background,
    onSurface = Outline,
)

// The house colours are fixed by the lab spec, so the app always uses the light scheme.
@Composable
fun BuildTheHouseTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = LightColorScheme, content = content)
}
