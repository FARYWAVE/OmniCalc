package com.example.omnicalc.ui.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import kotlinx.coroutines.delay
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.foundation.gestures.detectTapGestures
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModel
import com.example.omnicalc.R
import com.example.omnicalc.ui.screens.calc.*
import com.example.omnicalc.ui.theme.GrayDark
import com.example.omnicalc.ui.theme.ThemeManager
import com.example.omnicalc.utils.SquareButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.launch as launch1


interface KeyPressHandler {
    fun onKeyPress(keyName: String)
}


enum class Section {
    SIMPLEST_OPERATIONS,
    FUNCTIONS,
    VARIABLES
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
    DEGREE_SQUARE("degree_square", "²"),
    DEGREE_CUBIC("degree_cubic", "³"),
    DEGREE("degree", "°"),
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
    BACKSPACE("backspace", "<-");

    // Method to format for numbers
    fun number(num: Int): String {
        return keyName.replace("{n}", num.toString())
    }

    // Method to format for variables
    fun variable(char: Char): String {
        return keyName.replace("{ch}", char.toString())
    }
}


@Composable
fun CalcKeyboard(parentViewModel: KeyPressHandler) {
    Column(
        modifier = Modifier.fillMaxSize().drawBehind {
            // Draw a line at the top
            drawLine(
                color = GrayDark,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = 7f
            )
        }
    ) {
        CalcKeyboardNavigationBar()
        SimplestOperations(parentViewModel)
    }

}



@Composable
fun CalcKeyboardNavigationBar() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        val currentSection = remember { mutableStateOf(Section.SIMPLEST_OPERATIONS) }
        val (simplestOperations, functions) = createRefs()

        Box(
            modifier = Modifier
                .background(if (currentSection.value == Section.SIMPLEST_OPERATIONS) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background)
                .clickable {
                    currentSection.value = Section.SIMPLEST_OPERATIONS
                }
                .constrainAs(simplestOperations) {
                    start.linkTo(parent.start)
                    end.linkTo(functions.start)
                }
                .padding(4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.simplestoperations),
                contentDescription = "Simplest Operations",
                tint = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.size(28.dp)
            )
        }

        Box(
            modifier = Modifier
                .background(if (currentSection.value == Section.FUNCTIONS) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background)
                .clickable {
                    currentSection.value = Section.FUNCTIONS
                }
                .constrainAs(functions) {
                    start.linkTo(simplestOperations.end)
                    end.linkTo(parent.end)
                }
                .padding(4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.functions),
                contentDescription = "Functions",
                tint = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun ConverterKeyboardNavigationBar() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        val currentSection = remember { mutableStateOf(Section.SIMPLEST_OPERATIONS) }
        val (simplestOperations, functions, variables) = createRefs()

        Box(
            modifier = Modifier
                .background(if (currentSection.value == Section.SIMPLEST_OPERATIONS) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background)
                .clickable {
                    currentSection.value = Section.SIMPLEST_OPERATIONS
                }
                .constrainAs(simplestOperations) {
                    start.linkTo(parent.start)
                    end.linkTo(functions.start)
                }
                .padding(4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.simplestoperations),
                contentDescription = "Simplest Operations",
                tint = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.size(28.dp)
            )
        }

        Box(
            modifier = Modifier
                .background(if (currentSection.value == Section.FUNCTIONS) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background)
                .clickable {
                    currentSection.value = Section.FUNCTIONS
                }
                .constrainAs(functions) {
                    start.linkTo(simplestOperations.end)
                    end.linkTo(variables.start)
                }
                .padding(4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.functions),
                contentDescription = "Functions",
                tint = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.size(28.dp)
            )
        }

        Box(
            modifier = Modifier
                .background(if (currentSection.value == Section.VARIABLES) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background)
                .clickable {
                    currentSection.value = Section.VARIABLES
                }
                .constrainAs(variables) {
                    start.linkTo(functions.end)
                    end.linkTo(parent.end)
                }
                .padding(4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.variables),
                contentDescription = "Variables",
                tint = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun SimplestOperations(parentViewModel: KeyPressHandler) {
    Row(modifier = Modifier.fillMaxSize()) {
        val definiteKeys = arrayOf(
            Key.NUMBER.number(7), Key.NUMBER.number(8), Key.NUMBER.number(9), Key.DIVIDE.keyIcon,
            Key.NUMBER.number(4), Key.NUMBER.number(5), Key.NUMBER.number(6), Key.MULTIPLY.keyIcon,
            Key.NUMBER.number(1), Key.NUMBER.number(2), Key.NUMBER.number(3), Key.MINUS.keyIcon,
            Key.NUMBER.number(0), Key.DOT.keyIcon, Key.EQUALS.keyIcon, Key.PLUS.keyIcon
        )
        val indefiniteKeys = arrayOf(
            arrayOf(Key.FRACTION_IMPROPER, Key.FRACTION),
            arrayOf(Key.GREATER, Key.GREATER_EQUALS, Key.LESS_EQUALS, Key.LESS),
            arrayOf(Key.ROOT_SQUARE, Key.ROOT_CUBIC, Key.ROOT),
            arrayOf(Key.DEGREE_SQUARE, Key.DEGREE_CUBIC, Key.DEGREE),
            arrayOf(Key.PI, Key.PI_DIV_2, Key.PI_DIV_3),
            arrayOf(Key.PERCENT, Key.REMAINDER),
            arrayOf(Key.BRACKETS, Key.ABSOLUTE),
        )
        Column(modifier = Modifier.fillMaxHeight().weight(2f)) {
            repeat(4) { rowIndex ->
                Row {
                    repeat(4) { colIndex ->
                        val key = definiteKeys[rowIndex * 4 + colIndex]
                        SquareButton(
                            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                            onClick = { parentViewModel.onKeyPress(key) },
                            modifier = Modifier
                                .weight(1f) // Distributes buttons equally in Row
                                .aspectRatio(1f) // Makes width = height
                                .padding(0.dp)
                        ) {
                            Text(
                                key.removePrefix("number/"),
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
        }
        Column(modifier = Modifier.fillMaxHeight().weight(1f)) {
            repeat(3) { colIndex ->
                Row {
                    repeat(2) { rowIndex ->
                        val keys = indefiniteKeys[colIndex * 2 + rowIndex]
                        val mainKey = keys[0]
                        var showPopup by remember { mutableStateOf(false) }
                        var selectedSymbol by remember { mutableStateOf(mainKey) }
                        var pressJob by remember { mutableStateOf<Job?>(null) }
                        SquareButton(
                            backgroundColor = MaterialTheme.colorScheme.background,
                            onClick = { },
                            modifier = Modifier.weight(1f) // Distributes buttons equally in Row
                                .aspectRatio(1f) // Makes width = height
                                .padding(0.dp)
                                .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        pressJob?.cancel() // Cancel any previous long-press detection
                                        pressJob =
                                            CoroutineScope(Dispatchers.Main).launch1 {
                                                delay(300) // Long press threshold
                                                showPopup = true
                                            }
                                        tryAwaitRelease() // Wait for user to release press
                                        pressJob?.cancel()
                                    }
                                )
                            }
                        ) {
                            Text(selectedSymbol.keyIcon,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 18.sp,

                            )
                        }

                        // Popup with alternate symbols
                        if (showPopup) {
                            DropdownMenu(
                                expanded = true,
                                onDismissRequest = { showPopup = false }
                            ) {
                                keys.forEach { symbol ->
                                    DropdownMenuItem(
                                        text = { Text(symbol.keyIcon) },
                                        onClick = {
                                            selectedSymbol = symbol
                                            showPopup = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                }


            }
            Row {
                val keys = indefiniteKeys.last()
                val mainKey = keys[0]
                var showPopup by remember { mutableStateOf(false) }
                var selectedSymbol by remember { mutableStateOf(mainKey) }
                var pressJob by remember { mutableStateOf<Job?>(null) }
                SquareButton(
                    backgroundColor = MaterialTheme.colorScheme.background,
                    onClick = { },
                    modifier = Modifier.weight(1f) // Distributes buttons equally in Row
                        .aspectRatio(1f) // Makes width = height
                        .padding(0.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    pressJob?.cancel() // Cancel any previous long-press detection
                                    pressJob =
                                        CoroutineScope(Dispatchers.Main).launch1 {
                                            delay(300) // Long press threshold
                                            showPopup = true
                                        }
                                    tryAwaitRelease() // Wait for user to release press
                                    pressJob?.cancel()
                                }
                            )
                        }
                ) {
                    Text(selectedSymbol.keyIcon,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Visible,
                        modifier = Modifier.padding(0.dp)
                        )
                }

                // Popup with alternate symbols
                if (showPopup) {
                    DropdownMenu(
                        expanded = true,
                        onDismissRequest = { showPopup = false }
                    ) {
                        keys.forEach { symbol ->
                            DropdownMenuItem(
                                text = { Text(symbol.keyIcon, maxLines = 1,
                                    modifier = Modifier.padding(0.dp),
                                    overflow = TextOverflow.Clip,) },
                                onClick = {
                                    selectedSymbol = symbol
                                    showPopup = false
                                }
                            )
                        }
                    }
                }
                IconButton(
                    onClick = { parentViewModel.onKeyPress(Key.BACKSPACE.keyName) },
                    modifier = Modifier
                        .weight(1f) // Distributes buttons equally in Row
                        .aspectRatio(1f) // Makes width = height
                        .padding(0.dp)
                        .background(MaterialTheme.colorScheme.onSecondary)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.backspace),
                        contentDescription = "Backspace",
                        tint = MaterialTheme.colorScheme.background,

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
    CalcKeyboard(vm)
}