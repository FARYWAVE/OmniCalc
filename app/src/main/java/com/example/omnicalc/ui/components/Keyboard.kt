package com.example.omnicalc.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omnicalc.R
import com.example.omnicalc.ui.screens.calc.CalcViewModel
import com.example.omnicalc.utils.vw


interface KeyPressHandler {
    fun onKeyPress(keyName: String)
}

enum class Key(val keyName: String, val keyIcon: String) {
    BRACKETS("brackets", "( )"),
    ABSOLUTE("absolute", "| |"),
    GREATER("greater", ">"),
    GREATER_EQUALS("greater_equals", "≥"),
    EQUALS("equals", "="),
    LESS_EQUALS("less_equals", "≤"),
    LESS("less", "<"),
    FRACTION("fraction", "½"),
    FRACTION_IMPROPER("fraction_improper", "⅔"),
    ROOT_SQUARE("root_square", "√"),
    ROOT_CUBIC("root_cubic", "∛"),
    ROOT("root", "°√"),
    DEGREE_SQUARE("degree_square", "x²"),
    DEGREE_CUBIC("degree_cubic", "x³"),
    DEGREE("degree", "x°"),
    PI("pi", "π"),
    PI_DIV_2("pi_div_2", "π/2"),
    PI_DIV_3("pi_div_3", "π/3"),
    PERCENT("percent", "%"),
    REMAINDER("remainder", "%"),
    PLUS("plus", "+"),
    MINUS("minus", "-"),
    MULTIPLY("multiply", "×"),
    DIVIDE("divide", "÷"),
    DOT("dot", "."),
    NUMBER("number/{n}", "n"),
    VARIABLE("variable/{ch}", "ch"),
    DOUBLE_O("double_o", "00"),
    BACKSPACE("backspace", "<-"),
    CLEAR("clear", "AC");

    // Method to format for numbers
    fun number(num: Int): String {
        return keyName.replace("{n}", num.toString())
    }

    // Method to format for variables
    fun variable(char: Char): String {
        return keyName.replace("{ch}", char.toString())
    }
}

sealed class IconType {
    data class Icon(val id: Int) : IconType()
    data class Text(val text: String) : IconType()
}

enum class FunKey(val keyName: String, val iconType: IconType = IconType.Text(keyName)) {
    SIN("sin"),
    COS("cos"),
    TAN("tan"),
    COT("cot"),
    SEC("sec"),
    CSC("csc"),

    ARCSIN("arcsin"),
    ARCCOS("arccos"),
    ARCTAN("arctan"),
    ARCCOT("arccot"),
    ARCSEC("arcsec"),
    ARCCSC("arccsc"),

    SINH("sinh"),
    COSH("cosh"),
    TANH("tanh"),
    COTH("coth"),

    LN("ln"),
    LOG("log"),
    SQRT("√"),
    EXP("exp"),

    FACTORIAL("!"),
    LIMIT("lim"),
    DERIVATIVE("d/dx"),
    INTEGRAL("∫"),

    E("e")
}


@Composable
fun SecondaryFuncBar(parentViewModel: KeyPressHandler, keys: Array<Key>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        for (key: Key in keys) {
            Button(
                onClick = { parentViewModel.onKeyPress(key.keyName) },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondary)
                    .height(16.666f.vw())
                    .padding(0.dp)
                    .aspectRatio(1f),
                shape = CutCornerShape(0.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    key.keyIcon,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        Spacer(modifier = Modifier.weight(2f))
        Button(
            onClick = { parentViewModel.onKeyPress(Key.CLEAR.keyName) },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.tertiary)
                .height(16.666f.vw())
                .padding(0.dp)
                .aspectRatio(1f),
            shape = CutCornerShape(0.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.background
            )
        ) {
            Text(
                Key.CLEAR.keyIcon,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.background
            )
        }
        Button(
            onClick = { parentViewModel.onKeyPress(Key.CLEAR.keyName) },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.tertiary)
                .height(16.666f.vw())
                .padding(0.dp)
                .aspectRatio(1f),
            shape = CutCornerShape(0.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.background
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.backspace),
                contentDescription = "Backspace",
                tint = MaterialTheme.colorScheme.background
            )
        }
    }

}


@Composable
fun CalcKeyboard(parentViewModel: KeyPressHandler) {
    val pagerState = rememberPagerState(initialPage = 1, pageCount = { 3 })
    val keys = remember { mutableStateListOf<Key>() }
    Column {
        SecondaryFuncBar(parentViewModel, keys.toTypedArray())
        VerticalPager(
            state = pagerState,
            modifier = Modifier.aspectRatio(1.5f)
        ) { page ->
            when (page) {
                0 -> Variables(parentViewModel)
                1 -> SimplestOperations(parentViewModel, keys)
                2 -> Functions(parentViewModel)
            }
        }
    }
}


@Composable
fun Variables(parentViewModel: KeyPressHandler) {
    val keys = ('a'..'z').toList()
    LazyRow(modifier = Modifier.fillMaxSize()) {
        items((keys.size + 3) / 4) { colInd ->
            Column(modifier = Modifier.fillMaxHeight()) {
                repeat(4) { rowInd ->
                    val index = colInd * 4 + rowInd
                    if (index < keys.size) {
                        val key = keys[index]
                        Button(
                            onClick = { parentViewModel.onKeyPress(key.toString()) },
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.background)
                                .fillMaxWidth()
                                .height(16.666f.vw())
                                .padding(0.dp)
                                .aspectRatio(1f),

                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.tertiary
                            )// Remove extra padding
                        ) {
                            Text(
                                key.toString(),
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                }
            }
        }
    }

}


@Composable
fun Functions(parentViewModel: KeyPressHandler) {
    val keys = FunKey.entries.toTypedArray()

    LazyRow(modifier = Modifier.fillMaxSize()) {
        items((keys.size + 3) / 4) { colInd ->
            Column(modifier = Modifier.fillMaxHeight()) {
                repeat(4) { rowInd ->
                    val index = colInd * 4 + rowInd
                    if (index < keys.size) {
                        val key = keys[index]

                        if (key.iconType is IconType.Text) {
                            Button(
                                onClick = { parentViewModel.onKeyPress(key.keyName) },
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.background)
                                    .fillMaxWidth()
                                    .height(16.666f.vw())
                                    .padding(0.dp)
                                    .aspectRatio(1f),
                                shape = CutCornerShape(0.dp),
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.background,
                                    contentColor = MaterialTheme.colorScheme.tertiary
                                )
                            ) {
                                Text(
                                    key.iconType.text,
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            }
                        } else if (key.iconType is IconType.Icon) {
                            Button(
                                onClick = { parentViewModel.onKeyPress(key.keyName) },
                                shape = CutCornerShape(0.dp),
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.background)
                                    .fillMaxWidth()
                                    .height(16.vw())
                                    .padding(0.dp)
                                    .aspectRatio(1f),
                            ) {
                                Icon(
                                    painter = painterResource(id = key.iconType.id),
                                    contentDescription = key.keyName,
                                    tint = MaterialTheme.colorScheme.tertiary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SimplestOperations(parentViewModel: KeyPressHandler, secondaryKeys: SnapshotStateList<Key>) {
    Row {
        val definiteKeys = arrayOf(
            Key.NUMBER.number(7), Key.NUMBER.number(8), Key.NUMBER.number(9), Key.DIVIDE.keyIcon,
            Key.NUMBER.number(4), Key.NUMBER.number(5), Key.NUMBER.number(6), Key.MULTIPLY.keyIcon,
            Key.NUMBER.number(1), Key.NUMBER.number(2), Key.NUMBER.number(3), Key.MINUS.keyIcon,
            Key.NUMBER.number(0), Key.DOT.keyIcon, Key.DOUBLE_O.keyIcon, Key.PLUS.keyIcon
        )
        val indefiniteKeys = arrayOf(
            arrayOf(Key.FRACTION, Key.FRACTION_IMPROPER),
            arrayOf(Key.GREATER, Key.GREATER_EQUALS, Key.LESS_EQUALS, Key.LESS),
            arrayOf(Key.ROOT_SQUARE, Key.ROOT_CUBIC, Key.ROOT),
            arrayOf(Key.DEGREE_SQUARE, Key.DEGREE_CUBIC, Key.DEGREE),
            arrayOf(Key.PI, Key.PI_DIV_2, Key.PI_DIV_3),
            arrayOf(Key.PERCENT, Key.REMAINDER),
            arrayOf(Key.BRACKETS, Key.ABSOLUTE),
        )
        Column(
            modifier = Modifier
                .weight(2f)
        ) {
            repeat(4) { rowIndex ->
                Row {
                    repeat(4) { colIndex ->
                        val key = definiteKeys[rowIndex * 4 + colIndex]
                        Button(
                            onClick = { parentViewModel.onKeyPress(key) },
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.background)
                                .height(16.666f.vw())
                                .padding(0.dp)
                                .aspectRatio(1f),
                            shape = CutCornerShape(0.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.tertiary
                            )
                        ) {
                            Text(
                                key.removePrefix("number/"),
                                color = MaterialTheme.colorScheme.tertiary,
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            repeat(3) { colIndex ->
                Row {
                    repeat(2) { rowIndex ->
                        val keys = indefiniteKeys[colIndex * 2 + rowIndex]
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
                                        parentViewModel.onKeyPress(mainKey.keyName)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                mainKey.keyIcon,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 20.sp
                            )
                        }
                    }

                }


            }
            Row {
                val keys = indefiniteKeys.last()
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
                                parentViewModel.onKeyPress(mainKey.keyName)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        mainKey.keyIcon,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 20.sp
                    )
                }

                Button(
                    onClick = { parentViewModel.onKeyPress(Key.EQUALS.keyName) },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .height(16.666f.vw())
                        .padding(0.dp)
                        .aspectRatio(1f),

                    contentPadding = PaddingValues(0.dp),
                    shape = CutCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.tertiary,
                    )
                ) {
                    Text(
                        Key.EQUALS.keyIcon,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

val vm = CalcViewModel()

@Preview
@Composable
fun KbrdPreview() {
    Functions(vm)
}