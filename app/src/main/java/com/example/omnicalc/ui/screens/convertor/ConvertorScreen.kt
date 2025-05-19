package com.example.omnicalc.ui.screens.convertor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.omnicalc.ui.components.CalcKeyboard
import com.example.omnicalc.ui.components.display.Display
import com.example.omnicalc.ui.components.display.ResultText
import com.example.omnicalc.ui.screens.MainViewModel
import com.example.omnicalc.ui.screens.settings.SettingsViewModel
import com.example.omnicalc.utils.Length
import com.example.omnicalc.utils.Measurement
import com.example.omnicalc.utils.MeasurementUnit

@Composable
fun ConvertorScreen(id: String) {
    val convertorViewModel: ConvertorViewModel = viewModel()
    val mainViewModel: MainViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()
    val accuracy by settingsViewModel.accuracy.collectAsState(initial = 11)
    val measurement = Measurement.getType(id)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        UnitSelectorBar(measurement)
        Column(
            Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Display()
        }
        Row (Modifier.padding(5.dp)){
            val result = MainViewModel.result.value
            var converted by remember { mutableStateOf(0.0) }
            LaunchedEffect(result, convertorViewModel.from, convertorViewModel.to, convertorViewModel.reversed) {
                converted = convertorViewModel.convert(result, measurement)
            }

            Column(Modifier.weight(1f)) {
                LazyRow {
                    item {
                        ResultText(
                            Modifier,
                            if (convertorViewModel.reversed) converted else result,
                            accuracy,
                            20,
                            false
                        )
                    }
                }
                Text(
                    text = convertorViewModel.from.unitName,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Column(Modifier.weight(1f)) {
                LazyRow {
                    item {
                        ResultText(
                            Modifier,
                            if (!convertorViewModel.reversed) converted else result,
                            accuracy,
                            20,
                            false
                        )
                    }
                }
                Text(
                    text = convertorViewModel.to.unitName,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        CalcKeyboard()
    }
}


@Preview
@Composable
fun ScreenPreview() {
    ConvertorScreen(Measurement.Type.AREA.typeName)
}