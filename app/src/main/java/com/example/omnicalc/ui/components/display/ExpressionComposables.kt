package com.example.omnicalc.ui.components.display

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp
import com.example.omnicalc.ui.components.Function
import kotlinx.coroutines.delay


@Composable
fun ExpressionContainer(container: ExpressionContainer) {
    Row {
        for (expression in container.container) {
            if (expression is NumericExpression) {
                Operator(expression, expression.value.toString())
            } else expression.type.Compose(expression)
        }
    }
}

@Composable
fun SimpleExpression(expression: Expression) {
    Row {
        Text(
            text = expression.type.displayText + "(",
            fontSize = expression.fontSize.sp,
            color = MaterialTheme.colorScheme.tertiary
        )
        ExpressionContainer(expression.getContainer(0))
        Text(
            text = ")",
            fontSize = expression.fontSize.sp,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
fun Operator(expression: Expression, value: String) {
    Text(
        text = value,
        fontSize = expression.fontSize.sp,
        color = MaterialTheme.colorScheme.tertiary
    )
}

@Composable
fun Caret(expression: Expression) {
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(500) // blink interval
            visible = !visible
        }
    }

    Text(
        text = if (visible) "|" else " ",
        fontSize = expression.fontSize.sp
    )
}