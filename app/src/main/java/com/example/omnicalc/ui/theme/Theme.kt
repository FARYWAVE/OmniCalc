package com.example.omnicalc.ui.theme

import android.app.Activity
import android.os.Build
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.omnicalc.ui.screens.settings.SettingsViewModel


object ThemeManager {
    private val _appColor = mutableStateOf(Red)
    val appColor: State<Color> get() = _appColor

    fun setAppColor(color: Color) {
        _appColor.value = color
    }
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

    val appColor = ThemeManager.appColor.value
    val colorScheme = if (isDarkMode) {
        darkColorScheme(
            primary = appColor,
            secondary = GrayDark,
            tertiary = White,
            background = Black
        )
    } else {
        lightColorScheme(
            primary = appColor,
            secondary = GrayLight,
            tertiary = Black,
            background = White
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = FarywaveTypo,
        content = content,
    )
}