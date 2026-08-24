package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = ImmersiveBlue,
    onPrimary = AmoledBlack,
    primaryContainer = Zinc900,
    onPrimaryContainer = ImmersiveBlueLight,
    secondary = ImmersivePurple,
    onSecondary = TextWhite,
    secondaryContainer = Zinc800,
    onSecondaryContainer = ImmersivePurple,
    tertiary = LiquidEmerald,
    onTertiary = AmoledBlack,
    background = AmoledBlack,
    onBackground = TextWhite,
    surface = DarkSurfaceGlass,
    onSurface = TextWhite,
    surfaceVariant = Zinc900,
    onSurfaceVariant = TextMuted,
    outline = GlassSpecular
)

@Composable
fun MyApplicationTheme(
    isAmoledBlack: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = AmoledBlack.toArgb()
            window.navigationBarColor = AmoledBlack.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
