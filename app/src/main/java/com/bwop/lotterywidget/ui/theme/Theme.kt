package com.bwop.lotterywidget.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Neon Cyberpunk Colors
val NeonPurple = Color(0xFF9D4EDD)
val NeonPink = Color(0xFFFF006E)
val NeonCyan = Color(0xFF00F5FF)
val NeonGreen = Color(0xFF39FF14)
val NeonOrange = Color(0xFFFF6B35)
val NeonYellow = Color(0xFFFFE500)

val GradientStart = Color(0xFF667EEA)
val GradientEnd = Color(0xFF764BA2)

val DarkBg = Color(0xFF0D0D1A)
val DarkSurface = Color(0xFF1A1A2E)
val DarkCard = Color(0xFF16213E)

val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFB0B0B0)

val Success = Color(0xFF4CAF50)
val Warning = Color(0xFFFF9800)
val Error = Color(0xFFF44336)

// Gradient brushes
val PurpleGradient = Brush.linearGradient(listOf(GradientStart, GradientEnd))
val NeonGradient = Brush.linearGradient(listOf(NeonPurple, NeonPink, NeonCyan))
val CyberGradient = Brush.linearGradient(listOf(NeonCyan, NeonGreen))

private val DarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    secondary = NeonPurple,
    tertiary = NeonPink,
    background = DarkBg,
    surface = DarkSurface,
    onPrimary = DarkBg,
    onSecondary = TextPrimary,
    onTertiary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = Error,
    onError = TextPrimary
)

@Composable
fun BwopLotteryTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography(),
        content = content
    )
}
