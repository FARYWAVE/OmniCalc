package com.example.omnicalc.ui.components.display

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.omnicalc.ui.components.KeyPressHandler
import com.example.omnicalc.ui.screens.MainViewModel
import com.example.omnicalc.utils.VariableManager

@Composable
fun VariableCard(modifier: Modifier = Modifier, variable: VariableManager.Variable, viewModel: KeyPressHandler) {
    val mainViewModel: MainViewModel = viewModel()
    Box(modifier = Modifier.padding(10.dp)) {
        Row(modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary), verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.padding(10.dp),
                text = "${variable.name}",
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 22.sp
            )
            Text(
                modifier = Modifier.padding(5.dp),
                text = "=",
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 22.sp
            )
            LazyRow (Modifier.padding(5.dp)){
                item {
                    ExpressionContainer(
                        container = variable.expression,
                        fontSize = 20,
                        viewModel = viewModel as DisplayClickHandler
                    )
                }
            }
        }
    }
}

@Composable
fun ResultText(modifier: Modifier = Modifier, result: Double, accuracy: Int, fontSize: Int) {
    Text(
        modifier = modifier,
        text = when (result) {
            4.583945721467122 -> "Incorrect Input"
            1.583945721467122 -> "True"
            0.583945721467122 -> "False"
            else -> "= ${result.applyAccuracy(accuracy)}"
        },
        color = MaterialTheme.colorScheme.tertiary,
        fontSize = fontSize.sp
    )
}