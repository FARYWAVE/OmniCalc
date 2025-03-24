package com.example.omnicalc.utils


import android.graphics.Canvas
import android.graphics.Paint
import android.util.LayoutDirection
import android.util.Size
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.shape.*
import androidx.compose.ui.Modifier
import com.example.omnicalc.ui.theme.ThemeManager.appColor
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.omnicalc.ui.theme.FarywaveTheme
import com.example.omnicalc.ui.theme.White


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

@Composable
fun SquareButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = appColor.value,
    contentColor: Color = White,
    content: @Composable () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        shape = CutCornerShape(0.dp),
    ) {
        content()
    }
}