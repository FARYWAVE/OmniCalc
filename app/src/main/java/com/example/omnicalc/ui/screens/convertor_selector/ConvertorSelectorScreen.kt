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
import com.example.omnicalc.utils.Measurement
import com.example.omnicalc.utils.vw

@Composable
fun ConvertorSelectorScreen(navController: NavController) {
    val measurements = Measurement.Type.entries.toTypedArray()
    val padding = 5.vw()
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (addButton) = createRefs()
        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            items(measurements.size / 2) { colInd ->
                Spacer(modifier = Modifier.padding(padding))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(2) { rowInd ->
                        val index = colInd * 2 + rowInd
                        if (index < measurements.size) {
                            val unit = measurements[index]
                            MeasurementUnitCard(unit, navController)
                            if (rowInd != 1) Spacer(modifier = Modifier.padding(padding))
                        }
                    }
                }
            }
            if (measurements.size % 2 != 0) {
                val last = measurements.last()
                item {
                    Spacer(modifier = Modifier.padding(padding))
                    MeasurementUnitCard(last, navController)
                }
            }
            item {
                Spacer(Modifier.height(38.466f.vw()))
            }
        }
        Box(
            Modifier
                .padding(10.9f.vw())
                .constrainAs(addButton) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }) {
            Button(
                onClick = {},
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .height(16.666f.vw())
                    .aspectRatio(2f),
                shape = CutCornerShape(0.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Row (verticalAlignment = Alignment.CenterVertically){
                    Text(
                        "New",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 24.sp
                    )
                    Spacer(Modifier.width(4.vw()))
                    Text(
                        "+",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 30.sp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ScreenPreview() {
    ConvertorSelectorScreen(navController = rememberNavController())
}