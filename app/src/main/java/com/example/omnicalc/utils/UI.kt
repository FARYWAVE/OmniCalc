package com.example.omnicalc.utils


import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun Int.vw(): Dp {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    return (this * screenWidth / 100).dp
}

@Composable
fun Int.vh(): Dp {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    return (this * screenHeight / 100).dp
}

@Composable
fun Float.vw(): Dp {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    return (this * screenWidth / 100).dp
}

@Composable
fun Float.vh(): Dp {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    return (this * screenHeight / 100).dp
}