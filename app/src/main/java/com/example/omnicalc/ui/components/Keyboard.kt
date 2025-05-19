package com.example.omnicalc.ui.components

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.omnicalc.R
import com.example.omnicalc.ui.screens.MainViewModel
import com.example.omnicalc.ui.screens.calc.CalcViewModel
import com.example.omnicalc.ui.screens.settings.SettingsViewModel
import com.example.omnicalc.utils.vw
import com.example.omnicalc.ui.components.display.Function


interface KeyPressHandler {
    fun onKeyPress(keyName: String)
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SecondaryFuncBar(mainViewModel: KeyPressHandler, keys: Array<Function>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.tertiary)
                .height(16.666f.vw())
                .aspectRatio(1f)
                .combinedClickable (
                    onClick = {mainViewModel.onKeyPress(Function.BACKSPACE.functionName)},
                    onLongClick = {mainViewModel.onKeyPress(Function.CLEAR.functionName)}
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.offset(x = (-2).dp),
                painter = painterResource(id = R.drawable.backspace),
                contentDescription = Function.BACKSPACE.functionName,
                tint = MaterialTheme.colorScheme.background
            )
        }

        keys.forEach { key ->
            when (key) {
                Function.ROOT -> {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.secondary)
                            .height(16.666f.vw())
                            .aspectRatio(1f)
                            .clickable {mainViewModel.onKeyPress(key.functionName)},
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "√",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 18.sp
                        )
                        Text(
                            modifier = Modifier.offset(x = (-3).dp),
                            text = "°",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 18.sp
                        )
                    }
                }
                Function.ROOT_SQUARE -> {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.secondary)
                            .height(16.666f.vw())
                            .aspectRatio(1f)
                            .clickable {mainViewModel.onKeyPress(key.functionName)},
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "√",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 18.sp
                        )
                        Text(
                            modifier = Modifier.offset(x = (-3).dp),
                            text = "²",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 18.sp
                        )
                    }
                }
                Function.ROOT_CUBIC -> {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.secondary)
                            .height(16.666f.vw())
                            .aspectRatio(1f)
                            .clickable {mainViewModel.onKeyPress(key.functionName)},
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "√",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 18.sp
                        )
                        Text(
                            modifier = Modifier.offset(x = (-3).dp),
                            text = "³",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 18.sp
                        )
                    }
                }
                else ->KeyButton(
                    mainViewModel,
                    key.displayText,
                    MaterialTheme.colorScheme.secondary,
                    MaterialTheme.colorScheme.primary,
                    key.icon,
                    functionName = key.functionName
                )
            }

        }
        Spacer(modifier = Modifier.weight(2f))
    }
}


@Composable
fun KeyButton(
    mainViewModel: KeyPressHandler,
    keyName: String,
    bgColor: Color,
    textColor: Color,
    iconId: Int? = null,
    textSize: TextUnit = 18.sp,
    functionName: String = keyName
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Button(
            onClick = { mainViewModel.onKeyPress(functionName) },
            modifier = Modifier
                .background(bgColor)
                .height(16.666f.vw())
                .aspectRatio(1f),
            shape = CutCornerShape(0.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = bgColor)
        ) {
            if (iconId != null) {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = keyName,
                    tint = textColor
                )
            } else {
                Text(keyName, fontSize = textSize, color = textColor)
            }
        }
    }
}


@Composable
fun CalcKeyboard(modifier: Modifier = Modifier) {
    Log.d("Keyboard init", "passed")
    val settingsViewModel: SettingsViewModel = viewModel()
    val mainViewModel: MainViewModel = viewModel()
    val pagerState = rememberPagerState(initialPage = 1, pageCount = { 3 })
    val keys = remember { mutableStateListOf<Function>() }
    val isLeftHanded by settingsViewModel.isLeftHanded.collectAsState(initial = false)

    Column (modifier){
        CompositionLocalProvider(LocalLayoutDirection provides if (isLeftHanded) LayoutDirection.Ltr else LayoutDirection.Rtl) {
            SecondaryFuncBar(mainViewModel, keys.toTypedArray())
            VerticalPager(state = pagerState, modifier = Modifier.aspectRatio(1.5f)) { page ->
                when (page) {
                    0 -> {
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                            Variables(mainViewModel)
                        }
                    }

                    1 -> SimplestOperations(mainViewModel, keys)
                    2 ->
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                            Functions(mainViewModel)
                        }
                }
            }
        }

    }
}


@Composable
fun Variables(mainViewModel: KeyPressHandler) {
    val _keys = ('a'..'z').toMutableList()
    _keys.addAll('A'..'Z')
    val keys = _keys.chunked(4)
    LazyRow(modifier = Modifier.fillMaxSize()) {
        items(keys.size) { rowInd ->
            val row = keys[rowInd]
            Column(modifier = Modifier.fillMaxHeight()) {
                row.forEach { key ->
                    KeyButton(
                        mainViewModel,
                        Function.VARIABLE.variable(key)[9].toString(),
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.tertiary,
                        functionName = Function.VARIABLE.variable(key)
                    )
                }
            }
        }
    }
}


@Composable
fun Functions(mainViewModel: KeyPressHandler) {
    val keys = Function.entries.toList().subList(32, Function.entries.size).chunked(4)

    LazyRow(modifier = Modifier.fillMaxSize()) {
        items(keys.size) { rowInd ->
            val row = keys[rowInd]

            Column(modifier = Modifier.fillMaxHeight()) {
                row.forEach { key ->
                    KeyButton(
                        mainViewModel = mainViewModel,
                        keyName = key.displayText,
                        bgColor = MaterialTheme.colorScheme.background,
                        textColor = MaterialTheme.colorScheme.onBackground,
                        iconId = key.icon,
                        functionName = key.functionName
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SimplestOperations(mainViewModel: KeyPressHandler, secondaryKeys: SnapshotStateList<Function>) {
    Row {
        val definiteKeys = listOf(
            Function.NUMBER.number(7),
            Function.NUMBER.number(8),
            Function.NUMBER.number(9),
            Function.DIVIDE.functionName,
            Function.NUMBER.number(4),
            Function.NUMBER.number(5),
            Function.NUMBER.number(6),
            Function.MULTIPLY.functionName,
            Function.NUMBER.number(1),
            Function.NUMBER.number(2),
            Function.NUMBER.number(3),
            Function.MINUS.functionName,
            Function.NUMBER.number(0),
            Function.DOT.functionName,
            Function.FRACTION.functionName,
            Function.PLUS.functionName
        )

        val indefiniteKeys = listOf(
            arrayOf(Function.PERCENT, Function.REMAINDER),
            arrayOf(Function.ROOT_SQUARE, Function.ROOT_CUBIC, Function.ROOT),
            arrayOf(Function.POWER_SQUARE, Function.POWER_CUBIC, Function.POWER),
            arrayOf(Function.PI, Function.PI_DIV_2, Function.PI_DIV_3),
            arrayOf(Function.EQUALS, Function.GREATER, Function.GREATER_EQUALS, Function.LESS_EQUALS, Function.LESS),
            arrayOf(Function.BRACKETS, Function.ABSOLUTE)
        )

        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Column(
                modifier = Modifier.weight(2f)
            ) {
                definiteKeys.chunked(4).forEach { row ->
                    Row {
                        row.forEach { key ->
                            val type =
                                if ("number" in key) Function.NUMBER else Function.fromFunctionName(
                                    key
                                )

                            if (type == Function.NUMBER) {
                                KeyButton(
                                    mainViewModel = mainViewModel,
                                    keyName = key.replace("number/", ""),
                                    bgColor = MaterialTheme.colorScheme.background,
                                    textColor = MaterialTheme.colorScheme.tertiary,
                                    functionName = key
                                )
                            } else {
                                KeyButton(
                                    mainViewModel = mainViewModel,
                                    keyName = type!!.displayText,
                                    bgColor = MaterialTheme.colorScheme.background,
                                    textColor = MaterialTheme.colorScheme.tertiary,
                                    functionName = type.functionName
                                )
                            }

                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            indefiniteKeys.chunked(2).forEach { rowKeys ->
                Row {
                    rowKeys.forEach { keys ->
                        val mainKey = keys[0]
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.background)
                                .height(16.666f.vw())
                                .padding(0.dp)
                                .aspectRatio(1f)
                                .combinedClickable(
                                    onClick = {
                                        secondaryKeys.clear()
                                        secondaryKeys.addAll(keys)
                                    },
                                    onDoubleClick = {
                                        mainViewModel.onKeyPress(mainKey.functionName)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (mainKey.icon != null) {
                                Icon(
                                    painter = painterResource(id = mainKey.icon),
                                    contentDescription = mainKey.functionName,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            } else {
                                Text(mainKey.displayText, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Row {
                    for (key in arrayOf(Function.STEP_BACKWARDS, Function.STEP_FORWARD)) {
                        KeyButton(
                            mainViewModel,
                            key.displayText,
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.tertiary,
                            key.icon,
                            functionName = key.functionName
                        )
                    }
                }
            }
        }
    }
}

val vm = CalcViewModel()

@Preview
@Composable
fun KbrdPreview() {
    SimplestOperations(vm, remember { mutableStateListOf() })
}