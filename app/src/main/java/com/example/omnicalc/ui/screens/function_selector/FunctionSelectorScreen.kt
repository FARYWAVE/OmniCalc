package com.example.omnicalc.ui.screens.function_selector

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.omnicalc.ui.screens.calc.CalcScreen
import java.time.LocalDate

@Composable
fun FunctionSelectorScreen() {
    Text("This is the Function Selector Screen")
    OutlinedTextField(
        value = "Sample Input",
        onValueChange = { },
        label = { Text("Input Field") },
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
    )
}

@Preview
@Composable
fun ScreenPreview() {
    FunctionSelectorScreen()
}