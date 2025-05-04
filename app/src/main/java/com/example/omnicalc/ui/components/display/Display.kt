package com.example.omnicalc.ui.components.display

import android.util.Log
import android.view.Display
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.omnicalc.ui.screens.MainViewModel

@Composable
fun Display() {
    val mainViewModel: MainViewModel = viewModel()
    ExpressionContainer(mainViewModel.rootContainer)
}