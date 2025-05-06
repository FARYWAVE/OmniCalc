package com.example.omnicalc.ui.components.display

import android.util.Log
import android.view.Display
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.omnicalc.ui.screens.MainViewModel

@Composable
fun Display() {
    val mainViewModel: MainViewModel = viewModel()
    ExpressionContainer(modifier = Modifier.padding(10.dp), container = mainViewModel.rootContainer, fontSize = 25, viewModel = mainViewModel)
}

interface DisplayClickHandler {
    fun onDisplayClicked(hash: Int, after: Boolean = true)
    fun onSpecialClicked(hash: Int, start:Boolean = true)
}
