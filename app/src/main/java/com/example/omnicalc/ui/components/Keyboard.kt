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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omnicalc.R
import com.example.omnicalc.ui.screens.calc.CalcViewModel
import com.example.omnicalc.utils.vw


interface KeyPressHandler {
    fun onKeyPress(keyName: String)
}


enum class Key(val keyName: String, val keyIcon: String) {
    BRACKETS("brackets", "( )"), ABSOLUTE("absolute", "| |"),
    GREATER("greater", ">"), GREATER_EQUALS("greater_equals", "≥"),
    EQUALS("equals", "="), LESS_EQUALS("less_equals", "≤"),
    LESS("less", "<"), FRACTION("fraction", "½"),
    FRACTION_IMPROPER("fraction_improper", "⅔"),
    ROOT_SQUARE("root_square", "√"), ROOT_CUBIC("root_cubic", "∛"),
    ROOT("root", "°√"), DEGREE_SQUARE("degree_square", "x²"),
    DEGREE_CUBIC("degree_cubic", "x³"), DEGREE("degree", "x°"),
    PI("pi", "π"), PI_DIV_2("pi_div_2", "π/2"), PI_DIV_3("pi_div_3", "π/3"),
    PERCENT("percent", "%"), REMAINDER("remainder", "%"),
    PLUS("plus", "+"), MINUS("minus", "-"), MULTIPLY("multiply", "×"),
    DIVIDE("divide", "÷"), DOT("dot", "."),
    NUMBER("number/{n}", "n"), VARIABLE("variable/{ch}", "ch"),
    DOUBLE_O("double_o", "00"), BACKSPACE("backspace", "←"),
    CLEAR("clear", "AC");

    fun number(num: Int) = keyName.replace("{n}", num.toString())
    fun variable(char: Char) = keyName.replace("{ch}", char.toString())
}


sealed class IconType {
    data class Icon(val id: Int) : IconType()
    data class Text(val text: String) : IconType()
}


enum class FunKey(val keyName: String, val iconType: IconType = IconType.Text(keyName)) {
    SIN("sin"), COS("cos"), TAN("tan"), COT("cot"), SEC("sec"), CSC("csc"),
    ARCSIN("arcsin"), ARCCOS("arccos"), ARCTAN("arctan"), ARCCOT("arccot"),
    ARCSEC("arcsec"), ARCCSC("arccsc"), SINH("sinh"), COSH("cosh"), TANH("tanh"),
    COTH("coth"), LN("ln"), LOG("log"), SQRT("\u221a"), EXP("exp"), FACTORIAL("!"),
    LIMIT("lim"), DERIVATIVE("d/dx"), INTEGRAL("\u222b"), E("e")
}


@Composable
fun SecondaryFuncBar(parentViewModel: KeyPressHandler, keys: Array<Key>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        keys.forEach { key ->
            KeyButton(
                parentViewModel,
                key.keyIcon,
                MaterialTheme.colorScheme.secondary,
                MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.weight(2f))
        KeyButton(
            parentViewModel,
            Key.CLEAR.keyIcon,
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.background
        )
        KeyButton(
            parentViewModel,
            Key.BACKSPACE.keyIcon,
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.background,
            R.drawable.backspace
        )
    }
}


@Composable
fun KeyButton(
    parentViewModel: KeyPressHandler,
    keyName: String,
    bgColor: Color,
    textColor: Color,
    iconId: Int? = null,
    textSize: TextUnit = 18.sp
) {
    Button(
        onClick = { parentViewModel.onKeyPress(keyName) },
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


@Composable
fun CalcKeyboard(parentViewModel: KeyPressHandler) {
    val pagerState = rememberPagerState(initialPage = 1, pageCount = { 3 })
    val keys = remember { mutableStateListOf<Key>() }
    Column {
        SecondaryFuncBar(parentViewModel, keys.toTypedArray())
        VerticalPager(state = pagerState, modifier = Modifier.aspectRatio(1.5f)) { page ->
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
    val keys = ('a'..'z').chunked(4)
    LazyRow(modifier = Modifier.fillMaxSize()) {
        items(keys.size) { rowInd ->
            val row = keys[rowInd]
            Column(modifier = Modifier.fillMaxHeight()) {
                row.forEach { key ->
                    KeyButton(
                        parentViewModel,
                        Key.VARIABLE.variable(key)[9].toString(),
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
}


@Composable
fun Functions(parentViewModel: KeyPressHandler) {
    val keys = FunKey.entries.toList().chunked(4)

    LazyRow(modifier = Modifier.fillMaxSize()) {
        items(keys.size) { rowInd ->
            val row = keys[rowInd]

            Column(modifier = Modifier.fillMaxHeight()) {
                row.forEach { key ->
                    when (val iconType = key.iconType) {
                        is IconType.Text -> KeyButton(
                            parentViewModel = parentViewModel,
                            keyName = key.keyName,
                            bgColor = MaterialTheme.colorScheme.background,
                            textColor = MaterialTheme.colorScheme.onBackground,
                            iconId = null
                        )
                        is IconType.Icon -> IconButton(
                            onClick = { parentViewModel.onKeyPress(key.keyName) },
                        ) {
                            Icon(
                                painter = painterResource(id = iconType.id),
                                contentDescription = key.keyName,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
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
        val definiteKeys = listOf(
            Key.NUMBER.number(7), Key.NUMBER.number(8), Key.NUMBER.number(9), Key.DIVIDE.keyIcon,
            Key.NUMBER.number(4), Key.NUMBER.number(5), Key.NUMBER.number(6), Key.MULTIPLY.keyIcon,
            Key.NUMBER.number(1), Key.NUMBER.number(2), Key.NUMBER.number(3), Key.MINUS.keyIcon,
            Key.NUMBER.number(0), Key.DOT.keyIcon, Key.DOUBLE_O.keyIcon, Key.PLUS.keyIcon
        )

        val indefiniteKeys = listOf(
            arrayOf(Key.FRACTION, Key.FRACTION_IMPROPER),
            arrayOf(Key.GREATER, Key.GREATER_EQUALS, Key.LESS_EQUALS, Key.LESS),
            arrayOf(Key.ROOT_SQUARE, Key.ROOT_CUBIC, Key.ROOT),
            arrayOf(Key.DEGREE_SQUARE, Key.DEGREE_CUBIC, Key.DEGREE),
            arrayOf(Key.PI, Key.PI_DIV_2, Key.PI_DIV_3),
            arrayOf(Key.PERCENT, Key.REMAINDER),
        )

        Column(
            modifier = Modifier.weight(2f)
        ) {
            definiteKeys.chunked(4).forEach { row ->
                Row {
                    row.forEach { key ->
                        KeyButton(
                            parentViewModel = parentViewModel,
                            keyName = key.replace("number/", ""),
                            bgColor = MaterialTheme.colorScheme.background,
                            textColor = MaterialTheme.colorScheme.tertiary,
                        )
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
                                        parentViewModel.onKeyPress(mainKey.keyName)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                mainKey.keyIcon,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }

            Row {
                val keys = arrayOf(Key.BRACKETS, Key.ABSOLUTE)
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
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .height(16.666f.vw())
                        .padding(0.dp)
                        .aspectRatio(1f)
                        .combinedClickable(
                            onClick = {
                                parentViewModel.onKeyPress(Key.EQUALS.keyName)
                            },
                            onLongClick = {}
                        ),
                    contentAlignment = Alignment.Center
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