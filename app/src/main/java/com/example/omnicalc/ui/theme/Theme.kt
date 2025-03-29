package com.example.omnicalc.ui.theme

import android.app.Activity
import android.os.Build
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


object ThemeManager {
    private val _appColor = mutableStateOf(Orange)
    val appColor: State<Color> get() = _appColor

    fun setAppColor(color: Color) {
        _appColor.value = color
    }
}

@Composable
fun FarywaveTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val appColor = ThemeManager.appColor.value
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = appColor,
            secondary = GrayDark,
            tertiary = White,
            background = Black
        )
    } else {
        lightColorScheme (
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