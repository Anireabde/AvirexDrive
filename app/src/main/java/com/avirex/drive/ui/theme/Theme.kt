package com.avirex.drive.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val AvirexBlack = Color(0xFF05080F)
val AvirexNavy = Color(0xFF0D1424)
val AvirexCardDark = Color(0xFF151F36)
val AvirexCyan = Color(0xFF00E5FF)
val AvirexBlue = Color(0xFF2979FF)

private val DarkColors = darkColorScheme(
    primary = AvirexCyan,
    secondary = AvirexBlue,
    background = AvirexBlack,
    surface = AvirexCardDark,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColors = lightColorScheme(
    primary = AvirexBlue,
    secondary = AvirexCyan,
    background = Color(0xFFF0F4F8),
    surface = Color.White,
    onBackground = Color(0xFF0D1424),
    onSurface = Color(0xFF0D1424)
)

@Composable
fun AvirexTheme(darkTheme: Boolean = true, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}