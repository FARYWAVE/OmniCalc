package com.example.omnicalc.ui.screens.convertor_selector

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.omnicalc.ui.navigation.Screen
import com.example.omnicalc.utils.Measurement
import com.example.omnicalc.utils.vw

@Composable
fun ConvertorSelectorScreen(navController: NavController) {
    val measurements = Measurement.Type.entries.toList()
    val padding = 5.vw()
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (addButton) = createRefs()

        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            items(measurements.chunked(2)) { row ->
                Spacer(modifier = Modifier.padding(padding))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    row.forEachIndexed { index, unit ->
                        MeasurementUnitCard(unit, navController)
                        if (index == 0 && row.size != 1) Spacer(modifier = Modifier.padding(padding))
                    }
                }
            }

            item { Spacer(Modifier.height(38.466f.vw())) }
        }
    }
}


@Preview
@Composable
fun ScreenPreview() {
    ConvertorSelectorScreen(navController = rememberNavController())
}