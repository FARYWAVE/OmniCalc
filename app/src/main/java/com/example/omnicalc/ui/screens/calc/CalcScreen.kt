package com.example.omnicalc.ui.screens.calc


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.omnicalc.ui.components.CalcKeyboard
import com.example.omnicalc.ui.components.display.Display

@Composable
fun CalcScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Column (
            Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Display()
        }
        CalcKeyboard()
    }
}

@Preview
@Composable
fun ScreenPreview() {
    CalcScreen()
}