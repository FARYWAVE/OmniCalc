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
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val appColor = ThemeManager.appColor.value
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = appColor,
            primaryContainer = GrayDark,
            onPrimary = White,
            secondary = appColor,
            secondaryContainer = GrayDark,
            onSecondary = White,
            background = Black,
            onBackground = White,
            surface = GrayDark,
            onSurface = White,
            onError = RedDark,
            error = RedDark,
            outline = GrayDark
        )
    } else {
        lightColorScheme (
            primary = appColor,
            primaryContainer = GrayLight,
            onPrimary = White,
            secondary = appColor,
            secondaryContainer = GrayLight,
            onSecondary = Black,
            background = White,
            onBackground = Black,
            surface = GrayLight,
            onSurface = Black,
            onError = RedDark,
            error = RedDark,
            outline = GrayLight
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = FarywaveTypo,
        content = content,
    )
}

/*
@Composable
fun OmniCalcTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}*/
