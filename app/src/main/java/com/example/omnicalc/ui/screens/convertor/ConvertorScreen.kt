package com.example.omnicalc.ui.screens.convertor

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
import com.example.omnicalc.utils.Measurement

@Composable
fun ConvertorScreen(id: String) {
    val viewModel: ConvertorViewModel = viewModel()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        UnitSelectorBar(Measurement.getType(id))
        ConstraintLayout(
            Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {

        }
        CalcKeyboard(viewModel)
    }
}

@Preview
@Composable
fun ScreenPreview() {
    ConvertorScreen(Measurement.Type.AREA.typeName)
}