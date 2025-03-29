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

@Composable
fun CalcScreen() {
    val viewModel: CalcViewModel = viewModel()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        ConstraintLayout(
            Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {

        }
        Column {
            CalcKeyboard(viewModel)
        }
    }
}

@Preview
@Composable
fun ScreenPreview() {
    CalcScreen()
}