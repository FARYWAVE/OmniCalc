package com.example.omnicalc.ui.components.display

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.omnicalc.utils.Expression
import com.example.omnicalc.utils.ExpressionContainer
import com.example.omnicalc.utils.NumericExpression
import kotlinx.coroutines.delay


@Composable
fun ExpressionContainer(
    modifier: Modifier = Modifier,
    container: ExpressionContainer,
    fontSize: Int,
    viewModel: DisplayClickHandler
) {
    Row(modifier.wrapContentHeight(), verticalAlignment = Alignment.CenterVertically) {
        if (container.container.size == 0) {
            Text(
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures {
                        viewModel.onSpecialClicked(container.hash)
                    }
                },
                text = "⛶",
                color = MaterialTheme.colorScheme.primary,
                fontSize = fontSize.sp
            )
        } else for (expression in container.container) {
            if (expression is NumericExpression) {
                Operator(expression, expression.value.toString(), fontSize, viewModel)
            } else expression.type.Compose(expression, fontSize, viewModel)
        }
    }
}

@Composable
fun SimpleExpression(expression: Expression, fontSize: Int, viewModel: DisplayClickHandler) {
    val containerHeight = remember { mutableIntStateOf(0) }
    val bracketWidth = (fontSize / 5).coerceAtLeast(2)
    val strokeWidth = (fontSize / 8).coerceAtLeast(2)
    val color = MaterialTheme.colorScheme.tertiary

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures { offset ->
                    val isRightSide = offset.x > size.width * 2 / 3
                    if (isRightSide) viewModel.onSpecialClicked(expression.containers[0].hash)
                    else viewModel.onDisplayClicked(expression.hash, false)
                }
            },
            text = expression.type.displayText,
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = fontSize.sp
        )
        Canvas(
            modifier = Modifier
                .width(bracketWidth.dp)
                .height(with(LocalDensity.current) { containerHeight.intValue.toDp() })
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        viewModel.onSpecialClicked(expression.containers[0].hash)
                    }
                }
        ) {
            val path = Path().apply {
                moveTo(size.width, 0f)
                quadraticTo(0f, size.height / 2, size.width, size.height)
            }
            drawPath(
                path = path,
                color = color,
                style = Stroke(width = strokeWidth.dp.toPx())
            )

        }
        Spacer(Modifier.width(strokeWidth.dp))
        Box(
            modifier = Modifier
                .onGloballyPositioned {
                    containerHeight.intValue = it.size.height * 8 / 10
                }
        ) {
            ExpressionContainer(
                container = expression.getContainer(0),
                fontSize = fontSize * 9 / 10,
                viewModel = viewModel
            )
        }
        Spacer(Modifier.width(strokeWidth.dp))
        Canvas(
            modifier = Modifier
                .width(bracketWidth.dp)
                .height(with(LocalDensity.current) { containerHeight.intValue.toDp() })
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val isRightSide = offset.x > size.width / 2
                        if (!isRightSide) viewModel.onSpecialClicked(
                            expression.containers[0].hash,
                            false
                        )
                        else viewModel.onDisplayClicked(expression.hash)
                    }
                }
        ) {
            val path = Path().apply {
                moveTo(0f, 0f)
                quadraticTo(size.width, size.height / 2, 0f, size.height)
            }
            drawPath(
                path = path,
                color = color,
                style = Stroke(width = strokeWidth.dp.toPx())
            )

        }
    }
}

@Composable
fun Operator(expression: Expression, value: String, fontSize: Int, viewModel: DisplayClickHandler) {
    Text(
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures { offset ->
                val isRightSide = offset.x > size.width / 2
                viewModel.onDisplayClicked(expression.hash, isRightSide)
            }
        },
        text = value,
        fontSize = fontSize.sp,
        color = MaterialTheme.colorScheme.tertiary
    )
}

@Composable
fun Brackets(expression: Expression, fontSize: Int, viewModel: DisplayClickHandler) {
    val containerHeight = remember { mutableIntStateOf(0) }
    val bracketWidth = (fontSize / 5).coerceAtLeast(2)
    val strokeWidth = (fontSize / 8).coerceAtLeast(2)
    val color = MaterialTheme.colorScheme.tertiary

    Row(verticalAlignment = Alignment.CenterVertically) {
        Canvas(
            modifier = Modifier
                .width(bracketWidth.dp)
                .height(with(LocalDensity.current) { containerHeight.intValue.toDp() })
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val isRightSide = offset.x > size.width / 2
                        if (isRightSide) viewModel.onSpecialClicked(expression.containers[0].hash)
                        else viewModel.onDisplayClicked(expression.hash, false)
                    }
                }
        ) {
            val path = Path().apply {
                moveTo(size.width, 0f)
                quadraticTo(0f, size.height / 2, size.width, size.height)
            }
            if (expression.type == Function.BRACKETS) {
                drawPath(
                    path = path,
                    color = color,
                    style = Stroke(width = strokeWidth.dp.toPx())
                )
            } else {
                drawLine(
                    color = color,
                    start = Offset(x = size.width / 2, y = 0f),
                    end = Offset(x = size.width / 2, y = size.height),
                    strokeWidth = strokeWidth.dp.toPx()
                )
            }

        }
        Spacer(Modifier.width(strokeWidth.dp))
        Box(
            modifier = Modifier
                .onGloballyPositioned {
                    containerHeight.intValue = it.size.height * 8 / 10
                }
        ) {
            ExpressionContainer(
                container = expression.getContainer(0),
                fontSize = fontSize,
                viewModel = viewModel
            )
        }
        Spacer(Modifier.width(strokeWidth.dp))
        Canvas(
            modifier = Modifier
                .width(bracketWidth.dp)
                .height(with(LocalDensity.current) { containerHeight.intValue.toDp() })
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val isRightSide = offset.x > size.width / 2
                        if (!isRightSide) viewModel.onSpecialClicked(
                            expression.containers[0].hash,
                            false
                        )
                        else viewModel.onDisplayClicked(expression.hash)
                    }
                }
        ) {
            val path = Path().apply {
                moveTo(0f, 0f)
                if (expression.type == Function.BRACKETS)
                    quadraticTo(size.width, size.height / 2, 0f, size.height)
            }
            if (expression.type == Function.BRACKETS) {
                drawPath(
                    path = path,
                    color = color,
                    style = Stroke(width = strokeWidth.dp.toPx())
                )
            } else {
                drawLine(
                    color = color,
                    start = Offset(x = size.width / 2, y = 0f),
                    end = Offset(x = size.width / 2, y = size.height),
                    strokeWidth = strokeWidth.dp.toPx()
                )
            }
        }
    }
}


@Composable
fun Fraction(expression: Expression, fontSize: Int, viewModel: DisplayClickHandler) {
    ConstraintLayout {
        val (numeratorRef, divider1Ref, divider2Ref, denominatorRef) = createRefs()

        ExpressionContainer(
            container = expression.getContainer(0),
            modifier = Modifier
                .constrainAs(numeratorRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            fontSize = fontSize * 9 / 10,
            viewModel = viewModel
        )

        HorizontalDivider(
            thickness = (fontSize / 10).dp,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier
                .constrainAs(divider1Ref) {
                    top.linkTo(numeratorRef.bottom)
                    start.linkTo(numeratorRef.start)
                    end.linkTo(numeratorRef.end)
                    width = androidx.constraintlayout.compose.Dimension.fillToConstraints
                }
        )

        HorizontalDivider(
            thickness = (fontSize / 10).dp,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier
                .constrainAs(divider2Ref) {
                    top.linkTo(numeratorRef.bottom)
                    start.linkTo(denominatorRef.start)
                    end.linkTo(denominatorRef.end)
                    width = androidx.constraintlayout.compose.Dimension.fillToConstraints
                }
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val isRightSide = offset.x > size.width / 2
                        viewModel.onDisplayClicked(expression.hash, isRightSide)
                    }
                }
        )

        ExpressionContainer(
            container = expression.getContainer(1),
            modifier = Modifier
                .constrainAs(denominatorRef) {
                    top.linkTo(divider1Ref.bottom)
                    start.linkTo(numeratorRef.start)
                    end.linkTo(numeratorRef.end)
                },
            fontSize = fontSize * 9 / 10,
            viewModel = viewModel
        )
    }
}


@Composable
fun Power(expression: Expression, fontSize: Int, viewModel: DisplayClickHandler) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(0.dp),
        contentAlignment = Alignment.TopStart
    ) {
        ExpressionContainer(
            container = expression.getContainer(0),
            fontSize = fontSize * 6 / 10,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-1).dp, y = (-fontSize / 5).dp),
            viewModel = viewModel
        )
    }
}


@Composable
fun Root(expression: Expression, fontSize: Int, viewModel: DisplayClickHandler) {
    val rootLegOffsetX = 5.dp
    val strokeWidth = (fontSize / 10).coerceAtLeast(2)
    val backgroundColor = MaterialTheme.colorScheme.background
    val color = MaterialTheme.colorScheme.tertiary

    SubcomposeLayout { constraints ->

        val indexPlaceable = subcompose("index") {
            ExpressionContainer(
                container = expression.getContainer(0),
                fontSize = fontSize * 6 / 10,
                viewModel = viewModel
            )
        }.first().measure(constraints)

        val bodyPlaceable = subcompose("body") {
            ExpressionContainer(
                container = expression.getContainer(1),
                fontSize = fontSize * 8 / 10,
                viewModel = viewModel
            )
        }.first().measure(constraints)

        val legOffsetX = rootLegOffsetX.roundToPx()
        val canvasPadding = 5
        val spacingBetweenIndexAndRoot = 2.dp.roundToPx()

        val contentWidth = bodyPlaceable.width + legOffsetX
        val totalWidth = contentWidth + indexPlaceable.width + spacingBetweenIndexAndRoot
        val totalHeight = maxOf(
            indexPlaceable.height + spacingBetweenIndexAndRoot,
            bodyPlaceable.height + fontSize
        )

        layout(totalWidth, totalHeight) {
            val canvasPlaceable = subcompose("canvas") {
                Canvas(
                    modifier = Modifier
                        .size(totalWidth.toDp(), totalHeight.toDp())
                        .pointerInput(Unit) {
                            detectTapGestures { offset ->
                                val isRightSide = offset.x > size.width / 2
                                viewModel.onDisplayClicked(expression.hash, isRightSide)
                            }
                        }
                ) {
                    val xCoordinates = arrayOf(
                        indexPlaceable.width.toFloat(),
                        indexPlaceable.width + (rootLegOffsetX / 2).toPx(),
                        indexPlaceable.width + rootLegOffsetX.toPx(),
                        size.width
                    )
                    val yCoordinates = arrayOf(
                        size.height - fontSize * 2,
                        size.height - fontSize,
                        strokeWidth.toFloat() + fontSize / 5,
                        strokeWidth.toFloat() + fontSize / 5
                    )

                    repeat(xCoordinates.size - 1) {
                        drawLine(
                            color = backgroundColor,
                            strokeWidth = strokeWidth.dp.toPx() + 4,
                            start = Offset(xCoordinates[it], yCoordinates[it]),
                            end = Offset(xCoordinates[it + 1], yCoordinates[it + 1])
                        )
                    }

                    repeat(xCoordinates.size - 1) {
                        drawLine(
                            color = color,
                            strokeWidth = strokeWidth.dp.toPx(),
                            start = Offset(xCoordinates[it], yCoordinates[it]),
                            end = Offset(xCoordinates[it + 1], yCoordinates[it + 1])
                        )
                    }
                }
            }.first().measure(Constraints.fixed(totalWidth, totalHeight))

            canvasPlaceable.place(0, 0)
            bodyPlaceable.place(
                x = indexPlaceable.width + legOffsetX + canvasPadding,
                y = totalHeight - bodyPlaceable.height - 15
            )
            indexPlaceable.place(0, 0)
        }
    }
}

@Composable
fun Logarithm(expression: Expression, fontSize: Int, viewModel: DisplayClickHandler) {
    val containerHeight = remember { mutableIntStateOf(0) }
    val bracketWidth = (fontSize / 5).coerceAtLeast(2)
    val strokeWidth = (fontSize / 8).coerceAtLeast(2)
    val color = MaterialTheme.colorScheme.tertiary

    Row(verticalAlignment = Alignment.Bottom) {
        Text(
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures { offset ->
                    val isRightSide = offset.x > size.width / 2
                    if (isRightSide) viewModel.onSpecialClicked(expression.containers[0].hash)
                    else viewModel.onDisplayClicked(expression.hash, false)
                }
            },
            text = "log",
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = fontSize.sp
        )
        Box(
            modifier = Modifier
                .wrapContentSize()
                .padding(0.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            ExpressionContainer(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(x = (-1).dp, y = (fontSize / 5).dp),
                container = expression.getContainer(0),
                fontSize = fontSize * 6 / 10,
                viewModel = viewModel
            )
        }
        Canvas(
            modifier = Modifier
                .width(bracketWidth.dp)
                .height(with(LocalDensity.current) { containerHeight.intValue.toDp() })
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val isRightSide = offset.x > size.width / 2
                        if (isRightSide) viewModel.onSpecialClicked(expression.containers[1].hash)
                        else viewModel.onSpecialClicked(expression.containers[0].hash, false)
                    }
                }
        ) {
            val path = Path().apply {
                moveTo(size.width, 0f)
                quadraticTo(0f, size.height / 2, size.width, size.height)
            }
            drawPath(
                path = path,
                color = color,
                style = Stroke(width = strokeWidth.dp.toPx())
            )

        }
        Spacer(Modifier.width(strokeWidth.dp))
        Box(
            modifier = Modifier
                .onGloballyPositioned {
                    containerHeight.intValue = it.size.height * 8 / 10
                }
                .offset(y = 3.dp)
        ) {
            ExpressionContainer(
                container = expression.getContainer(1   ),
                fontSize = fontSize * 9 / 10,
                viewModel = viewModel
            )
        }
        Spacer(Modifier.width(strokeWidth.dp))
        Canvas(
            modifier = Modifier
                .width(bracketWidth.dp)
                .height(with(LocalDensity.current) { containerHeight.intValue.toDp() })
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val isRightSide = offset.x > size.width / 2
                        if (!isRightSide) viewModel.onSpecialClicked(
                            expression.containers[1].hash,
                            false
                        )
                        else viewModel.onDisplayClicked(expression.hash)
                    }
                }
        ) {
            val path = Path().apply {
                moveTo(0f, 0f)
                quadraticTo(size.width, size.height / 2, 0f, size.height)
            }
            drawPath(
                path = path,
                color = color,
                style = Stroke(width = strokeWidth.dp.toPx())
            )

        }
    }
}


@Composable
fun Caret(expression: Expression, fontSize: Int, viewModel: DisplayClickHandler) {
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(500) // blink interval
            visible = !visible
        }
    }

    Text(
        text = "|",
        fontSize = fontSize.sp,
        color = if (visible) MaterialTheme.colorScheme.primary else Color.Transparent
    )
}