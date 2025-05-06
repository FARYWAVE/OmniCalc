package com.example.omnicalc.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.omnicalc.ui.components.ColorPicker
import com.example.omnicalc.utils.vh
import com.example.omnicalc.utils.vw

@Composable
fun ColorPickerDialog(navController: NavController) {
    val viewModel: SettingsViewModel = viewModel()
    val insets = WindowInsets.systemBars.asPaddingValues()
    Dialog(onDismissRequest = { navController.popBackStack() }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .clickable(
                    onClick = { navController.popBackStack() },
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                )
                .padding(insets),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .align(Alignment.Center),
                color = Color(0f, 0f, 0f, 0.5f),
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(5.vw())
                        .background(MaterialTheme.colorScheme.secondary)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {},
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var hue by remember { mutableStateOf(0f) }
                    Spacer(Modifier.height(5.vw()))
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.vw()),
                        text = "Choose App Color",
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                    )
                    Spacer(Modifier.height(4.vh()))
                    ColorPicker(
                        modifier = Modifier
                            .height(10.vw())
                            .padding(horizontal = 5.vw()),
                        hue = hue,
                        onHueChange = { newHue -> hue = newHue }
                    )
                    Spacer(Modifier.height(4.vw()))
                    Row {
                        Button(
                            shape = RoundedCornerShape(0.dp),
                            onClick = { navController.popBackStack() },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Text(
                                "Cancel",
                                color = MaterialTheme.colorScheme.tertiary,
                                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                                fontSize = 18.sp
                            )
                        }
                        Spacer(Modifier.width(5.vw()))
                        Button(
                            shape = RoundedCornerShape(0.dp),
                            onClick = {
                                viewModel.onPrimaryColorChanged(Color.hsv(hue, 0.9f, 1f))
                                navController.popBackStack()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.hsv(
                                    hue,
                                    1f,
                                    1f
                                )
                            )
                        ) {
                            Text(
                                "Apply",
                                color = MaterialTheme.colorScheme.tertiary,
                                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                                fontSize = 18.sp
                            )
                        }
                    }
                    Spacer(Modifier.height(5.vw()))
                }
            }
        }
    }
}