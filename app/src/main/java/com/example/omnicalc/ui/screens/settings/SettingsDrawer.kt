package com.example.omnicalc.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.omnicalc.ui.navigation.Screen
import com.example.omnicalc.ui.theme.ThemeManager
import com.example.omnicalc.utils.vh
import com.example.omnicalc.utils.vw


@Composable
fun SettingsDrawer(onClose: () -> Unit, navController: NavController) {
    val viewModel: SettingsViewModel = viewModel()
    val isDarkMode by viewModel.isDarkMode.collectAsState(initial = false)
    val isLeftHanded by viewModel.isLeftHanded.collectAsState(initial = false)
    val accuracy by viewModel.accuracy.collectAsState(initial = 0)
    LaunchedEffect(isDarkMode) {
        ThemeManager.applyTheme(isDarkMode)
    }
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Box (modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)){
            Column(modifier = Modifier.padding(5.vw())) {
                Text(
                    text = "Settings",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                    fontSize = 22.sp)
                Spacer(Modifier.height(4.vh()))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Dark Mode",
                        fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { viewModel.toggleDarkMode(it) },
                        colors = SwitchDefaults.colors(
                            checkedIconColor = MaterialTheme.colorScheme.primary,
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.background,
                            checkedBorderColor = MaterialTheme.colorScheme.background,
                            uncheckedIconColor = MaterialTheme.colorScheme.tertiary,
                            uncheckedThumbColor = MaterialTheme.colorScheme.tertiary,
                            uncheckedTrackColor = MaterialTheme.colorScheme.background,
                            uncheckedBorderColor = MaterialTheme.colorScheme.background
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Left-Handed Mode",
                        fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = isLeftHanded,
                        onCheckedChange = { viewModel.toggleLeftHanded(it) },
                        colors = SwitchDefaults.colors(
                            checkedIconColor = MaterialTheme.colorScheme.primary,
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.background,
                            checkedBorderColor = MaterialTheme.colorScheme.background,
                            uncheckedIconColor = MaterialTheme.colorScheme.tertiary,
                            uncheckedThumbColor = MaterialTheme.colorScheme.tertiary,
                            uncheckedTrackColor = MaterialTheme.colorScheme.background,
                            uncheckedBorderColor = MaterialTheme.colorScheme.background
                        )
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "App Color",
                        fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Box(Modifier.padding(2.vw())) {
                        Button(modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .width(10.vw())
                            .aspectRatio(1f)
                            .padding(2.vw()),
                            onClick = { navController.navigate(Screen.ColorPickerDialog.route) },
                            shape = RoundedCornerShape(0.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                        }
                    }
                }
                Row(modifier = Modifier.height(10.vw()), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Accuracy: ${if (accuracy == 11) "MAX" else accuracy}",
                        fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Slider(
                        modifier = Modifier.width(27f.vw()).height(12.dp),
                        value = accuracy.toFloat(),
                        onValueChange = { viewModel.updateAccuracy(it.toInt()) },
                        valueRange = (0f .. 11f),
                        steps = 11,
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.background,
                            inactiveTrackColor = MaterialTheme.colorScheme.background,
                            activeTickColor = Color.Transparent,
                            inactiveTickColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }
}