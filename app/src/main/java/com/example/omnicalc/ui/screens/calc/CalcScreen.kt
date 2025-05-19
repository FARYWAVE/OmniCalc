package com.example.omnicalc.ui.screens.calc


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.omnicalc.ui.components.CalcKeyboard
import com.example.omnicalc.ui.components.display.Display
import com.example.omnicalc.ui.components.display.ResultText
import com.example.omnicalc.ui.screens.MainViewModel
import com.example.omnicalc.ui.screens.settings.SettingsViewModel

@Composable
fun CalcScreen() {
    val viewModel: MainViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()
    val accuracy by settingsViewModel.accuracy.collectAsState(initial = 11)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Column(
            Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Display()
        }
        val result = MainViewModel.result.value
        LazyRow {
            item {
                ResultText(Modifier.padding(20.dp), result, accuracy, 30)
            }
        }

        CalcKeyboard()
    }
}

@Preview
@Composable
fun ScreenPreview() {
    CalcScreen()
}