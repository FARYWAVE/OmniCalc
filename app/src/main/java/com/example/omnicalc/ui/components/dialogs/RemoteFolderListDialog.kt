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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.omnicalc.ui.navigation.Screen
import com.example.omnicalc.ui.screens.function.FunctionViewModel
import com.example.omnicalc.ui.screens.function_selector.FunctionSelectorViewModel
import com.example.omnicalc.ui.screens.settings.SettingsViewModel
import com.example.omnicalc.utils.FirebaseRepository
import com.example.omnicalc.utils.vh
import com.example.omnicalc.utils.vw

@Composable
fun RemoteFolderListDialog(navController: NavController) {
    val insets = WindowInsets.systemBars.asPaddingValues()
    val selectorViewModel: FunctionSelectorViewModel = viewModel()
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
                    text = "Select Remote Folder",
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                    textAlign = TextAlign.Center
                )
                val repository = FirebaseRepository()
                var folders = mutableMapOf<String, String>()
                Log.d("FOLDERS", folders.keys.joinToString(", "))
                LaunchedEffect(Unit) {
                    folders = repository.loadAllFolders()
                }

                @Composable
                fun Option(pair: Pair<String, String>) {
                    Row(modifier = Modifier
                        .padding(10.dp)
                        .clickable(
                            onClick = {
                                selectorViewModel.loadRemoteFunctions(pair)
                                navController.navigate(Screen.FunctionSelector.route)
                            }
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.folder),
                            contentDescription = "Folder",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = pair.second,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(Modifier.weight(1f))
                    }
                }
                LazyColumn(Modifier.height(50.vh())) {
                    items(folders.keys.toTypedArray()) {
                        Option(Pair(it, folders[it]!!))
                    }
                }
                Spacer(Modifier.height(5.vw()))
            }
        }
    }
}


