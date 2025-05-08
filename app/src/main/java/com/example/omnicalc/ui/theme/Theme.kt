package com.example.omnicalc.ui.theme

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.omnicalc.ui.screens.settings.SettingsViewModel
import kotlinx.coroutines.flow.collect

fun Color.darken(factor: Float): Color {
    // factor = 0.0f (no change), to 1.0f (black)
    return Color(
        red = red * (1f - factor),
        green = green * (1f - factor),
        blue = blue * (1f - factor),
        alpha = alpha
    )
}

object ThemeManager {
    fun applyTheme(isDarkMode: Boolean) {
        val nightMode = if (isDarkMode) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }
}

@Composable
fun FarywaveTheme(
    settingsViewModel: SettingsViewModel,
    content: @Composable () -> Unit
) {
    val isDarkMode by settingsViewModel.isDarkMode.collectAsState(initial = isSystemInDarkTheme())
    val primaryColor by settingsViewModel.primaryColor.collectAsState(initial = Color(0xFF000000))

    Log.d("FARYWAVE THEME", primaryColor.toArgb().toString())

    val colorScheme = if (isDarkMode) {
        darkColorScheme(
            primary = primaryColor.darken(0.2f),
            secondary = GrayDark,
            tertiary = White.darken(0.2f),
            background = Black
        )
    } else {
        lightColorScheme(
            primary = primaryColor,
            secondary = GrayLight,
            tertiary = Black.darken(-0.2f),
            background = White
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = FarywaveTypo,
        content = content,
    )
}