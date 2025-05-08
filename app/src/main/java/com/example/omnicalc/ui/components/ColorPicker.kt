package com.example.omnicalc.ui.components


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.omnicalc.utils.vh
import com.example.omnicalc.utils.vw

@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    hue: Float,
    onHueChange: (Float) -> Unit
) {
    ConstraintLayout(modifier = modifier) {
        val (pattern, slider) = createRefs()
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .constrainAs(pattern) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.vw())
            ) {
                val colors = (0..360 step 30).map { Color.hsv(it.toFloat(), 1f, 0.8f)}.toMutableList()
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = colors
                    )
                )
            }
        }

        Slider(
            value = hue,
            onValueChange = onHueChange,
            valueRange = 0f..360f,
            colors = SliderDefaults.colors(
                thumbColor = Color.Black,
                activeTrackColor = Color.Transparent,
                inactiveTrackColor = Color.Transparent,
                inactiveTickColor = Color.Black,
                disabledInactiveTickColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(slider) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
    }
}

@Preview
@Composable
fun ScreenPreview() {
    ColorPicker(hue = 0f) {

    }
}