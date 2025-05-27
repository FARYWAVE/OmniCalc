package com.example.omnicalc.ui.screens.function_selector

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.omnicalc.ui.components.ActionBar
import com.example.omnicalc.ui.components.ActionBarHandler
import com.example.omnicalc.ui.components.ColorPicker
import com.example.omnicalc.utils.DBConvertor
import com.example.omnicalc.utils.DatabaseRepository
import com.example.omnicalc.utils.FunctionFolder


@Composable
fun FunctionSelectorScreen(navController: NavController) {
    val selectorViewModel: FunctionSelectorViewModel = viewModel()
    val currentFolder by FunctionSelectorViewModel.currentFolder.collectAsState()
    val folderContents by selectorViewModel.folderContents.collectAsState()
    Log.d("FOLDER", currentFolder?.name?: "null")
    val isRoot = currentFolder?.name.orEmpty() == "Root"
    Column (Modifier.fillMaxSize()){
        ActionBar(
            viewModel = selectorViewModel,
            root = isRoot,
            keys = if (isRoot) arrayOf(ActionBarHandler.Key.ADD)
            else arrayOf(
                ActionBarHandler.Key.MOVE,
                ActionBarHandler.Key.SAVE,
                ActionBarHandler.Key.ADD,
                ActionBarHandler.Key.DELETE
            ),
            navController = navController
        )
        Spacer(Modifier.height(10.dp))
        LazyColumn(Modifier.fillMaxSize()) {
            items(folderContents) { item ->
                item.Compose(viewModel = selectorViewModel, navController = navController)
                if (item is com.example.omnicalc.utils.Function) Log.i("Function", DBConvertor.fromExpressionContainer(item.expression))
            }
        }
    }
}