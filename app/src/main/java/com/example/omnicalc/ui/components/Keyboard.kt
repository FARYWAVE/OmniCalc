package com.example.omnicalc.ui.components

import android.util.Log
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
import com.example.omnicalc.ui.components.display.Caret
import com.example.omnicalc.ui.components.display.Expression
import com.example.omnicalc.ui.components.display.NumericExpression
import com.example.omnicalc.ui.components.display.Operator
import com.example.omnicalc.ui.components.display.SimpleExpression
import com.example.omnicalc.ui.screens.MainViewModel
import com.example.omnicalc.ui.screens.calc.CalcViewModel
import com.example.omnicalc.ui.screens.settings.SettingsViewModel
import com.example.omnicalc.utils.vw
import kotlinx.coroutines.flow.last


interface KeyPressHandler {
    fun onKeyPress(keyName: String)
}


enum class Function(
    var functionName: String,
    val displayText: String,
    val icon: Int? = null,
    val numberOfInputs: Int = 0,
    val inputIndex: Int = 0
) {
    // Cursor & Brackets
    CARET("caret", "|", null, inputIndex = -1) {
        @Composable
        override fun Compose(expression: Expression) {
            Caret(expression)
        }
    },
    BRACKETS("brackets", "( )", null, 1),
    ABSOLUTE("absolute", "| |", null, 1),

    // Comparison
    GREATER("greater", ">", null, inputIndex = -1) {
        @Composable
        override fun Compose(expression: Expression) {
            Operator(expression, displayText)
        }
    },
    GREATER_EQUALS("greater_equals", "≥", null, inputIndex = -1) {
        @Composable
        override fun Compose(expression: Expression) {
            Operator(expression, displayText)
        }
    },
    EQUALS("equals", "=", null, inputIndex = -1) {
        @Composable
        override fun Compose(expression: Expression) {
            Operator(expression, displayText)
        }
    },
    LESS_EQUALS("less_equals", "≤", null, inputIndex = -1) {
        @Composable
        override fun Compose(expression: Expression) {
            Operator(expression, displayText)
        }
    },
    LESS("less", "<", null, inputIndex = -1) {
        @Composable
        override fun Compose(expression: Expression) {
            Operator(expression, displayText)
        }
    },

    // Fractions & Roots
    FRACTION("fraction", "½", null, 2),
    ROOT_SQUARE("root_square", "√", null, inputIndex = 1),
    ROOT_CUBIC("root_cubic", "∛", null, inputIndex = 1),
    ROOT("root", "°√", null, 2, 0),

    // Powers
    POWER_SQUARE("power_square", "x²", null, inputIndex = -1),
    POWER_CUBIC("power_cubic", "x³", null, inputIndex = -1),
    POWER("power", "x°", null, 1, 1),

    // Constants
    PI("pi", "π", null, inputIndex = -1) {
        @Composable
        override fun Compose(expression: Expression) {
            Operator(expression, displayText)
        }
    },
    PI_DIV_2("pi_div_2", "π/2", null, inputIndex = -1),
    PI_DIV_3("pi_div_3", "π/3", null, inputIndex = -1),
    PERCENT("percent", "%", null, inputIndex = -1),
    REMAINDER("remainder", "%", null, inputIndex = -1),

    // Operators
    PLUS("plus", "+", null, inputIndex = -1) {
        @Composable
        override fun Compose(expression: Expression) {
            Operator(expression, displayText)
        }
    },
    MINUS("minus", "-", null, inputIndex = -1) {
        @Composable
        override fun Compose(expression: Expression) {
            Operator(expression, displayText)
        }
    },
    MULTIPLY("multiply", "×", null, inputIndex = -1) {
        @Composable
        override fun Compose(expression: Expression) {
            Operator(expression, displayText)
        }
    },
    DIVIDE("divide", "÷", null, inputIndex = -1) {
        @Composable
        override fun Compose(expression: Expression) {
            Operator(expression, displayText)
        }
    },
    DOT("dot", ".", null) {
        @Composable
        override fun Compose(expression: Expression) {
            Operator(expression, displayText)
        }
    },

    // Input
    NUMBER("number", "n", null, 0, -1),
    VARIABLE("variable/{ch}", "ch", null, inputIndex = -1),
    DOUBLE_O("double_o", "00", null),

    // Controls
    BACKSPACE("backspace", "", 0),
    CLEAR("clear", "AC", null),

    // Trigonometric Functions
    SIN("sin", "sin", null, 1),
    COS("cos", "cos", null, 1),
    TAN("tan", "tan", null, 1),
    COT("cot", "cot", null, 1),
    SEC("sec", "sec", null, 1),
    CSC("csc", "csc", null, 1),

    // Arc Trig Functions
    ARCSIN("arcsin", "arcsin", null, 1),
    ARCCOS("arccos", "arccos", null, 1),
    ARCTAN("arctan", "arctan", null, 1),
    ARCCOT("arccot", "arccot", null, 1),
    ARCSEC("arcsec", "arcsec", null, 1),
    ARCCSC("arccsc", "arccsc", null, 1),

    // Hyperbolic Functions
    SINH("sinh", "sinh", null, 1),
    COSH("cosh", "cosh", null, 1),
    TANH("tanh", "tanh", null, 1),
    COTH("coth", "coth", null, 1),

    // Logarithmic & Exponential
    LN("ln", "ln", null, inputIndex = 1),
    LOG("log", "log", null, 2),
    LG("lg", "lg", null, inputIndex = 1),
    EXP("exp", "exp", null, 1, -1),

    // Miscellaneous
    FACTORIAL("factorial", "!", null, inputIndex = -1),
    E("e", "e", null, inputIndex = -1) {
        @Composable
        override fun Compose(expression: Expression) {
            Operator(expression, displayText)
        }
    };

    @Composable
    open fun Compose(expression: Expression) {
        SimpleExpression(expression)
    }

    fun number(num: Int) : String{
        return "number/$num"
    }
    fun variable(char: Char) = functionName.replace("{ch}", char.toString())

    companion object {
        fun fromFunctionName(name: String): Function? {
            return entries.firstOrNull { it.functionName == name }
        }
    }
}



@Composable
fun SecondaryFuncBar(mainViewModel: KeyPressHandler, keys: Array<Function>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        KeyButton(
            mainViewModel,
            Function.CLEAR.displayText,
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.background,
            functionName = Function.CLEAR.functionName
        )
        KeyButton(
            mainViewModel,
            "",
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.background,
            R.drawable.backspace,
            functionName = Function.BACKSPACE.functionName
        )

        keys.forEach { key ->
            KeyButton(
                mainViewModel,
                key.displayText,
                MaterialTheme.colorScheme.secondary,
                MaterialTheme.colorScheme.primary,
                key.icon,
                functionName = key.functionName
            )
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
fun CalcKeyboard() {
    Log.d("Keyboard init", "passed")
    val settingsViewModel: SettingsViewModel = viewModel()
    val mainViewModel: MainViewModel = viewModel()
    val pagerState = rememberPagerState(initialPage = 1, pageCount = { 3 })
    val keys = remember { mutableStateListOf<Function>() }
    val isLeftHanded by settingsViewModel.isLeftHanded.collectAsState(initial = false)

    Column {
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
    val keys = ('a'..'z').chunked(4)
    LazyRow(modifier = Modifier.fillMaxSize()) {
        items(keys.size) { rowInd ->
            val row = keys[rowInd]
            Column(modifier = Modifier.fillMaxHeight()) {
                row.forEach { key ->
                    KeyButton(
                        mainViewModel,
                        Function.VARIABLE.variable(key)[9].toString(),
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
}


@Composable
fun Functions(mainViewModel: KeyPressHandler) {
    val keys = Function.entries.toList().subList(30, Function.entries.size).chunked(4)

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
            Function.DIVIDE.displayText,
            Function.NUMBER.number(4),
            Function.NUMBER.number(5),
            Function.NUMBER.number(6),
            Function.MULTIPLY.displayText,
            Function.NUMBER.number(1),
            Function.NUMBER.number(2),
            Function.NUMBER.number(3),
            Function.MINUS.displayText,
            Function.NUMBER.number(0),
            Function.DOT.displayText,
            Function.DOUBLE_O.displayText,
            Function.PLUS.displayText
        )

        val indefiniteKeys = listOf(
            arrayOf(Function.FRACTION),
            arrayOf(Function.GREATER, Function.GREATER_EQUALS, Function.LESS_EQUALS, Function.LESS),
            arrayOf(Function.ROOT_SQUARE, Function.ROOT_CUBIC, Function.ROOT),
            arrayOf(Function.POWER_SQUARE, Function.POWER_CUBIC, Function.POWER),
            arrayOf(Function.PI, Function.PI_DIV_2, Function.PI_DIV_3),
            arrayOf(Function.PERCENT, Function.REMAINDER),
        )

        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Column(
                modifier = Modifier.weight(2f)
            ) {
                definiteKeys.chunked(4).forEach { row ->
                    Row {
                        row.forEach { key ->
                            KeyButton(
                                mainViewModel = mainViewModel,
                                keyName = key.replace("number/", ""),
                                bgColor = MaterialTheme.colorScheme.background,
                                textColor = MaterialTheme.colorScheme.tertiary,
                                functionName = key
                            )
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

            Row {
                val keys = arrayOf(Function.BRACKETS, Function.ABSOLUTE)
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
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .height(16.666f.vw())
                        .padding(0.dp)
                        .aspectRatio(1f)
                        .combinedClickable(
                            onClick = {
                                mainViewModel.onKeyPress(Function.EQUALS.functionName)
                            },
                            onLongClick = {}
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        Function.EQUALS.displayText,
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