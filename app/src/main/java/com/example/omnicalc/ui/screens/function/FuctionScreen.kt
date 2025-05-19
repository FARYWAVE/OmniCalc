package com.example.omnicalc.ui.screens.function

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.omnicalc.ui.components.ActionBar
import com.example.omnicalc.ui.components.ActionBarHandler
import com.example.omnicalc.ui.components.CalcKeyboard
import com.example.omnicalc.ui.components.display.Display
import com.example.omnicalc.ui.components.display.ResultText
import com.example.omnicalc.ui.screens.MainViewModel
import com.example.omnicalc.ui.screens.settings.SettingsViewModel

@Composable
fun FunctionScreen(id: String, navController: NavController) {
    val viewModel: FunctionViewModel = viewModel()
    val mainViewModel: MainViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()
    val accuracy by settingsViewModel.accuracy.collectAsState(initial = 11)
    LaunchedEffect(Unit){
        viewModel.setFunction(id.toInt())
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Column(
            Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            ActionBar(
                viewModel,
                arrayOf(
                    ActionBarHandler.Key.SAVE,
                    ActionBarHandler.Key.DELETE,
                    ActionBarHandler.Key.COPY
                ),
                false,
                    navController
                )
            Display()
        }
        mainViewModel.onKeyPress("blank")
        val result = MainViewModel.result.value
        LazyRow {
            item {
                ResultText(Modifier.padding(20.dp), result, accuracy, 30)
            }
        }

        CalcKeyboard()
    }
}
