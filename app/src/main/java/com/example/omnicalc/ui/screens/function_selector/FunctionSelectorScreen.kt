package com.example.omnicalc.ui.screens.function_selector

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
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.example.omnicalc.ui.components.ColorPicker



@Composable
fun FunctionSelectorScreen() {
    var expressionList by remember { mutableStateOf(listOf<ExpressionItem>()) }
    var focusedIndex by remember { mutableStateOf(-1) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Display Area
        Box(modifier = Modifier.fillMaxWidth().height(100.dp).border(1.dp, Color.Black)) {
            LazyRow(modifier = Modifier.align(Alignment.Center)) {
                itemsIndexed(expressionList) { index, item ->
                    ExpressionView(item, index, focusedIndex) { newValue ->
                        expressionList = expressionList.toMutableList().apply { set(index, newValue) }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons
        Column {
            Row {
                Button(onClick = { expressionList = expressionList + ExpressionItem.NumberItem("1") }) { Text("1") }
                Button(onClick = { expressionList = expressionList + ExpressionItem.NumberItem("2") }) { Text("2") }
                Button(onClick = { expressionList = expressionList + ExpressionItem.NumberItem("3") }) { Text("3") }
            }
            Row {
                Button(onClick = { expressionList = expressionList + ExpressionItem.OperationItem("+") }) { Text("+") }
                Button(onClick = { expressionList = expressionList + ExpressionItem.OperationItem("*") }) { Text("*") }
                Button(onClick = { expressionList = expressionList + ExpressionItem.FunctionItem("sin", listOf(ExpressionItem.NumberItem(""))) }) { Text("sin(x)") }
            }
            Row {
                Button(onClick = { expressionList = emptyList() }) { Text("Clear") }
            }
        }
    }
}

@Composable
fun ExpressionView(item: ExpressionItem, index: Int, focusedIndex: Int, onUpdate: (ExpressionItem) -> Unit) {
    when (item) {
        is ExpressionItem.NumberItem -> TextField(
            value = item.value,
            onValueChange = { onUpdate(item.copy(value = it)) },
            modifier = Modifier.width(50.dp)
        )
        is ExpressionItem.OperationItem -> Text(item.op, fontSize = 24.sp, modifier = Modifier.padding(8.dp))
        is ExpressionItem.FunctionItem -> Row {
            Text("${item.name}(")
            item.arguments.forEachIndexed { i, arg ->
                if (i > 0) Text(", ")
                ExpressionView(arg, index, focusedIndex, onUpdate)
            }
            Text(")")
        }
    }
}

sealed class ExpressionItem {
    data class NumberItem(val value: String) : ExpressionItem()
    data class OperationItem(val op: String) : ExpressionItem()
    data class FunctionItem(val name: String, val arguments: List<ExpressionItem>) : ExpressionItem()
}


@Preview
@Composable
fun ScreenPreview() {
    FunctionSelectorScreen()
}