package com.ladystoneco.plangym.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.ladystoneco.plangym.util.AppTheme

private val CalmColorScheme = lightColorScheme(
    primary = CalmPrimary,
    secondary = CalmSecondary,
    background = CalmBackground,
    surface = CalmSurface,
    onPrimary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)

private val VioletColorScheme = darkColorScheme(
    primary = VioletPrimary,
    secondary = VioletSecondary,
    background = VioletBackground,
    surface = VioletSurface,
    onPrimary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

private val FitnessColorScheme = darkColorScheme(
    primary = FitnessPrimary,
    secondary = FitnessSecondary,
    background = FitnessBackground,
    surface = FitnessSurface,
    onPrimary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    primaryContainer = FitnessPrimary.copy(alpha = 0.2f),
    onPrimaryContainer = FitnessPrimary
)

@Composable
fun PlanGymTheme(
    appTheme: AppTheme = AppTheme.GLASS_VIOLET,
    content: @Composable () -> Unit
) {
    val colorScheme = when (appTheme) {
        AppTheme.CALM_MINIMAL -> CalmColorScheme
        AppTheme.GLASS_VIOLET -> VioletColorScheme
        AppTheme.FITNESS_DARK -> FitnessColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
