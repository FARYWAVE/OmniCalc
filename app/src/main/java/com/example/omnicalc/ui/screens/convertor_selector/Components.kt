package com.example.omnicalc.ui.screens.convertor_selector

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.omnicalc.ui.navigation.Screen
import com.example.omnicalc.utils.Measurement
import com.example.omnicalc.utils.vw


@Composable
fun MeasurementUnitCard(type: Measurement.Type, navController: NavController) {
    Button(
        onClick = { navController.navigate(Screen.Convertor.withId(type.typeName)) },
        modifier = Modifier
            .height(35.vw())
            .aspectRatio(1f),
        shape = CutCornerShape(0.dp),
        contentPadding = PaddingValues(0.dp),
        colors = buttonStyle()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(type.iconResId),
                tint = MaterialTheme.colorScheme.tertiary,
                contentDescription = type.typeName
            )
            Text(
                text = type.typeName,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}

@Composable
private fun buttonStyle() = ButtonDefaults.buttonColors(
    containerColor = MaterialTheme.colorScheme.secondary,
    contentColor = MaterialTheme.colorScheme.tertiary
)