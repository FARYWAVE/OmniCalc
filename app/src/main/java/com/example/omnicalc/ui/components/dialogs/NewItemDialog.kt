package com.example.omnicalc.ui.components.dialogs

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.omnicalc.R
import com.example.omnicalc.ui.components.ColorPicker
import com.example.omnicalc.ui.screens.function.FunctionViewModel
import com.example.omnicalc.ui.screens.function_selector.FunctionSelectorViewModel
import com.example.omnicalc.ui.screens.settings.SettingsViewModel
import com.example.omnicalc.utils.vh
import com.example.omnicalc.utils.vw

@Composable
fun NewItemDialog(navController: NavController) {
    val insets = WindowInsets.systemBars.asPaddingValues()
    val selectorViewModel: FunctionSelectorViewModel = viewModel()
    val functionViewModel: FunctionViewModel = viewModel()
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
                Spacer(Modifier.height(5.vw()))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.vw()),
                    text = "New",
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                    textAlign = TextAlign.Center
                )
                val chosenOption = remember { mutableIntStateOf(-1) }

                @Composable
                fun Option(index: Int, icon: Int, text: String, selected: Boolean) {
                    Row(modifier = Modifier
                        .padding(10.dp)
                        .clickable(
                        onClick = {
                            chosenOption.intValue = index
                        }
                    ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = "Folder",
                            tint = if (selected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = text,
                            fontSize = 18.sp,
                            color = if (selected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(Modifier.weight(1f))
                    }
                }
                Option(0, R.drawable.function, "Function", chosenOption.intValue == 0)
                Option(1, R.drawable.folder, "Folder", chosenOption.intValue == 1)
                Option(2, R.drawable.download, "Remote Folder", chosenOption.intValue == 2)
                val input = remember { mutableStateOf(" ") }
                if (chosenOption.intValue in 0..1) {
                    OutlinedTextField(
                        modifier = Modifier.padding(10.dp),
                        value = input.value,
                        maxLines = 1,
                        onValueChange = {input.value = it},
                        label = { Text("Enter Name") }
                    )
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
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(0.dp),
                            onClick = {
                                if (input.value.isNotEmpty()) {
                                    when (chosenOption.intValue) {
                                        0 -> {
                                            selectorViewModel.addFunction(input.value)
                                        }

                                        1 -> {
                                            selectorViewModel.createNewFolder(input.value)
                                        }

                                        2 -> {

                                        }
                                    }
                                    navController.popBackStack()

                                }
                            },
                        ) {
                            Text(
                                "Add",
                                color = MaterialTheme.colorScheme.tertiary,
                                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
                Spacer(Modifier.height(5.vw()))
            }
        }
    }
}


