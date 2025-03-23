package com.example.omnicalc.ui.screens.convertor_selector

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.omnicalc.ui.screens.calc.CalcScreen

@Composable
fun ConvertorSelectorScreen() {
    Text("This is the Convertor Selector Screen")
    Card(
        modifier = Modifier.padding(bottom = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Card Title")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "This is a simple card to check the theme.")
        }
    }

    // Elevated Button
    ElevatedButton(onClick = { /* Handle elevated button click */ }) {
        Text("Elevated Button")
    }
}


@Preview
@Composable
fun ScreenPreview() {
    ConvertorSelectorScreen()
}