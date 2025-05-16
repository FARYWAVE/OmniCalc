package com.example.omnicalc.ui.components.dialogs

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.omnicalc.R
import com.example.omnicalc.ui.components.ActionBarHandler
import com.example.omnicalc.ui.navigation.Screen
import com.example.omnicalc.ui.screens.function.FunctionViewModel
import com.example.omnicalc.ui.screens.function_selector.FunctionSelectorViewModel
import com.example.omnicalc.utils.vw

@Composable
fun ConfirmActionDialog(
    vmID: String,
    navController: NavController,
    keyName: String,
    itemName: String
) {
    val insets = WindowInsets.systemBars.asPaddingValues()
    val selectorViewModel: FunctionSelectorViewModel = viewModel()
    val functionViewModel: FunctionViewModel = viewModel()
    val key = ActionBarHandler.Key.keyByName(keyName)
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
                var text = when (key) {
                    ActionBarHandler.Key.DELETE -> "Delete "
                    ActionBarHandler.Key.COPY -> "Copy "
                    else -> ""
                }
                text += "$itemName?"
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.vw()),
                    text = text,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.width(5.vw()))
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
                            if (vmID == "0") {
                                functionViewModel.onKeyPressed(key)
                                if (key == ActionBarHandler.Key.DELETE) {
                                    navController.navigate(Screen.FunctionSelector.route)
                                    return@Button
                                }
                            }
                            else {
                                selectorViewModel.onKeyPressed(key)
                            }
                            navController.popBackStack()
                        },
                    ) {
                        Text(
                            "Confirm",
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

