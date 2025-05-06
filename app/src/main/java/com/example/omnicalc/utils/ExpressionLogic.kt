package com.example.omnicalc.utils

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.omnicalc.ui.components.display.Function
import com.example.omnicalc.ui.components.display.Function.*
import java.util.Stack
import kotlin.math.*


open class Expression(
    val type: Function,
) {
    private var parentContainer: ExpressionContainer? = null
    val numberOfInputs: Int = type.numberOfInputs
    val containers = Array(numberOfInputs) {
        ExpressionContainer(parentExpression = this, parentContainer = parentContainer)
    }
    val hash = getNewHash()

    companion object {
        private var lastHash = 0
        fun getNewHash(): Int {
            return lastHash++
        }

        val operators = listOf(
            Function.POWER,
            Function.FACTORIAL,
            Function.GREATER,
            Function.GREATER_EQUALS,
            Function.EQUALS,
            Function.LESS_EQUALS,
            Function.LESS,
            Function.PLUS,
            Function.MINUS,
            Function.MULTIPLY,
            Function.DIVIDE,
            Function.PERCENT,
            Function.REMAINDER
        )
    }

    fun setValues(containerId: Int, newContainer: ExpressionContainer) {
        containers[containerId] = newContainer
        newContainer.parentExpression = this
        newContainer.parentContainer = parentContainer
    }

    fun solve(): Double {
        val result = when (type) {
            Function.BRACKETS -> containers[0].solve()
            Function.ABSOLUTE -> containers[0].solve().absoluteValue
            Function.FRACTION -> containers[0].solve() / containers[1].solve()
            Function.ROOT -> containers[1].solve().pow(1 / containers[0].solve())
            Function.PI -> Math.PI
            Function.E -> Math.E
            Function.POWER -> containers[0].solve()
            Function.SIN -> sin(containers[0].solve())
            Function.COS -> cos(containers[0].solve())
            Function.TAN -> tan(containers[0].solve())
            Function.COT -> 1 / tan(containers[0].solve())
            Function.SEC -> 1 / cos(containers[0].solve())
            Function.CSC -> 1 / sin(containers[0].solve())
            Function.ARCSIN -> asin(containers[0].solve())
            Function.ARCCOS -> acos(containers[0].solve())
            Function.ARCTAN -> atan(containers[0].solve())
            Function.ARCCOT -> Math.PI / 2 - atan(containers[0].solve())
            Function.ARCSEC -> acos(1 / containers[0].solve())
            Function.ARCCSC -> asin(1 / containers[0].solve())
            Function.SINH -> sinh(containers[0].solve())
            Function.COSH -> cosh(containers[0].solve())
            Function.TANH -> tanh(containers[0].solve())
            Function.COTH -> 1 / tanh(containers[0].solve())
            Function.LOG -> log(containers[1].solve(), containers[0].solve())
            Function.EXP -> exp(containers[0].solve())
            else -> throw Exception("Incorrect Input")
        }
        return result
    }

    private fun factorial(n: Int): Double =
        if (n == 0 || n == 1) 1.0 else n * factorial(n - 1)

    fun setParentContainer(newParentContainer: ExpressionContainer?) {
        parentContainer = newParentContainer
        for (container in containers) {
            container.updateParentContainer(this, newParentContainer)
        }
    }

    fun getContainer(index: Int): ExpressionContainer {
        return containers[index]
    }

    fun delete() {
        for (container in containers) {
            container.delete()
        }
    }

    fun add(type: Function, value: Char? = null) {
        for (container in containers) {
            container.add(type, value)
        }
    }

    fun removeCaret() {
        containers.forEach {
            it.removeCaret()
        }
    }

    fun moveCaretAfter(hash: Int) {
        containers.forEach { it.moveCaretAfter(hash) }
    }

    fun moveCaretIn(hash: Int, start: Boolean = true) {
        containers.forEach {
            it.removeCaret()
            it.moveCaretIn(hash, start)
            if (it.hash == hash) {
                if (start) it.container.add(0, Expression(Function.CARET))
                else it.container.add(Expression(Function.CARET))
            }
        }
    }

    fun moveCaretBefore(hash: Int) {
        containers.forEach { it.moveCaretBefore(hash) }
    }

    fun stepForward() {
        for (container in containers) {
            if (container.stepForward()) break
        }
    }

    fun stepBackwards() {
        for (container in containers) {
            if (container.stepBackwards()) break
        }
    }

    override fun toString(): String {
        if (containers.size == 0) return type.functionName
        var result = "${type.functionName}($hash){"
        repeat(numberOfInputs) {
            result += "$it(${containers[it].hash}): ${containers[it]}"
        }
        //Log.d("Expression State", result)
        return result + "}"
    }
}

class NumericExpression(val value: Char) : Expression(Function.NUMBER) {
    override fun toString(): String {
        return value.toString()
    }
}

class ExpressionContainer(
    val container: SnapshotStateList<Expression> = mutableStateListOf(),
    var parentExpression: Expression? = null,
    var parentContainer: ExpressionContainer? = null,
) {
    val hash = Expression.getNewHash()
    val caret = Expression(CARET)

    init {
        for (item in container) {
            item.setParentContainer(this)
        }
    }


    fun solve(): Double {
        val values = Stack<Double>()
        val operators = Stack<Function>()

        var i = 0
        var prevWasValue = false

        while (i < container.size) {
            val expr = container[i]

            if (expr.type == Function.CARET) {
                i++
                continue
            }

            if (expr.type == Function.NUMBER) {
                if (prevWasValue) {
                    while (operators.isNotEmpty() && precedence(operators.peek()) >= precedence(
                            Function.MULTIPLY
                        )
                    ) {
                        applyOperator(values, operators.pop())
                    }
                    operators.push(Function.MULTIPLY)
                }

                val numberBuilder = StringBuilder()
                var hasDot = false

                while (i < container.size && container[i].type == Function.NUMBER) {
                    val numExpr = container[i] as NumericExpression
                    if (numExpr.value == '.') {
                        if (hasDot) {
                            throw IllegalArgumentException("Multiple decimal points in one number: $numberBuilder.")
                        }
                        hasDot = true
                    }
                    numberBuilder.append(numExpr.value)
                    i++
                }

                val num = numberBuilder.toString().toDoubleOrNull()
                    ?: throw IllegalArgumentException("Invalid number format: $numberBuilder")
                values.push(num)
                prevWasValue = true
                continue
            }

            if (Expression.operators.contains(expr.type)) {
                val op = expr.type

                // Factorial is unary postfix
                if (op == Function.FACTORIAL) {
                    val a = values.pop()
                    values.push(factorial(a))
                    i++
                    continue
                }

                while (operators.isNotEmpty() && precedence(operators.peek()) >= precedence(op)) {
                    applyOperator(values, operators.pop())
                }
                operators.push(op)
                prevWasValue = false
                i++
                continue
            }

            if (prevWasValue) {
                operators.push(Function.MULTIPLY)
            }

            val value = expr.solve()
            values.push(value)
            prevWasValue = true
            i++
        }

        while (operators.isNotEmpty()) {
            applyOperator(values, operators.pop())
        }


        val result = values.pop()
        return result
    }

    private fun factorial(n: Double): Double {
        require(n >= 0 && n == floor(n)) { "Factorial only defined for non-negative integers" }
        return (1..n.toInt()).fold(1.0) { acc, i -> acc * i }
    }

    private fun precedence(op: Function): Int = when (op) {
        Function.PLUS, Function.MINUS -> 1
        Function.MULTIPLY, Function.DIVIDE -> 2
        Function.PERCENT, Function.REMAINDER -> 3
        Function.GREATER, Function.LESS, Function.GREATER_EQUALS, Function.LESS_EQUALS, Function.EQUALS -> 0
        else -> -1
    }

    private fun applyOperator(values: Stack<Double>, op: Function) {
        val b = values.pop()
        val a = if (values.isNotEmpty()) values.pop() else 0.0
        values.push(
            when (op) {
                Function.PLUS -> a + b
                Function.MINUS -> a - b
                Function.MULTIPLY -> a * b
                Function.DIVIDE -> a / b
                Function.PERCENT -> a * b / 100
                Function.REMAINDER -> a % b
                Function.GREATER -> if (a > b) 1.0 else 0.0
                Function.GREATER_EQUALS -> if (a >= b) 1.0 else 0.0
                Function.LESS -> if (a < b) 1.0 else 0.0
                Function.LESS_EQUALS -> if (a <= b) 1.0 else 0.0
                Function.EQUALS -> if (a == b) 1.0 else 0.0
                else -> throw Exception("Unsupported operator: $op")
            }
        )
    }


    fun updateParentContainer(
        newParentExpression: Expression,
        newParentContainer: ExpressionContainer?
    ) {
        parentExpression = newParentExpression
        parentContainer = newParentContainer
        for (item in container) {
            item.setParentContainer(this)
        }
    }

    fun delete(expression: Expression) {
        container.remove(expression)
    }

    fun delete() {
        for (i in container.indices) {
            if (container[i].type == Function.CARET) {
                if (i > 0) {
                    container.removeAt(i - 1)
                    return
                } else {
                    parentContainer?.removeExpressionAndSetCaret(parentExpression!!)
                    return
                }
            } else {
                container[i].delete()
            }
        }
    }

    fun removeExpressionAndSetCaret(expression: Expression) {
        val index = container.indexOf(expression)
        if (index != -1) {
            container.removeAt(index)
            container.add(index, caret.apply { setParentContainer(this@ExpressionContainer) })
        }
    }

    fun add(type: Function, value: Char? = null) {
        val caretIndex = container.indexOfFirst { it.type == Function.CARET }
        if (caretIndex != -1) {
            val expression = when (type) {
                Function.ROOT_SQUARE -> {
                    val exp = Expression(ROOT)
                    exp.setValues(
                        0,
                        ExpressionContainer(mutableStateListOf(NumericExpression('2')))
                    )
                    exp
                }

                Function.ROOT_CUBIC -> {
                    val exp = Expression(ROOT)
                    exp.setValues(
                        0,
                        ExpressionContainer(mutableStateListOf(NumericExpression('3')))
                    )
                    exp
                }

                Function.POWER_SQUARE -> {
                    val exp = Expression(POWER)
                    exp.setValues(
                        0,
                        ExpressionContainer(mutableStateListOf(NumericExpression('2')))
                    )
                    exp
                }

                Function.POWER_CUBIC -> {
                    val exp = Expression(POWER)
                    exp.setValues(
                        0,
                        ExpressionContainer(mutableStateListOf(NumericExpression('3')))
                    )
                    exp
                }

                Function.NUMBER -> NumericExpression(value!!)
                Function.LN -> {
                    val exp = Expression(LOG)
                    exp.setValues(
                        0,
                        ExpressionContainer(mutableStateListOf(Expression(Function.E)))
                    )
                    exp
                }

                Function.LG -> {
                    val exp = Expression(LOG)
                    exp.setValues(
                        0,
                        ExpressionContainer(
                            mutableStateListOf(
                                NumericExpression('1'),
                                NumericExpression('0')
                            )
                        )
                    )
                    exp
                }

                Function.PI_DIV_2 -> {
                    val exp = Expression(FRACTION)
                    exp.setValues(
                        0,
                        ExpressionContainer(mutableStateListOf(Expression(Function.PI)))
                    )
                    exp.setValues(
                        1,
                        ExpressionContainer(mutableStateListOf(NumericExpression('2')))
                    )
                    exp
                }

                Function.PI_DIV_3 -> {
                    val exp = Expression(FRACTION)
                    exp.setValues(
                        0,
                        ExpressionContainer(mutableStateListOf(Expression(Function.PI)))
                    )
                    exp.setValues(
                        1,
                        ExpressionContainer(mutableStateListOf(NumericExpression('3')))
                    )
                    exp
                }

                else -> Expression(type)
            }
            expression.setParentContainer(this)
            container.add(caretIndex, expression)
            if (type.inputIndex != -1) {
                container.removeAll { it.type == Function.CARET }
                expression.setValues(
                    type.inputIndex,
                    ExpressionContainer(mutableStateListOf(Expression(CARET)))
                )
            }
        } else {
            container.forEach { it.add(type, value) }
        }
    }

    fun moveCaretAfter(hash: Int) {
        repeat(container.size) { i ->
            container[i].moveCaretAfter(hash)
            if (container[i].hash == hash) {
                container.add(i + 1, caret)
            }
        }
    }

    fun moveCaretIn(hash:Int, start: Boolean = true) {
        container.forEach {
            it.moveCaretIn(hash, start)
        }
    }

    fun removeCaret() {
        repeat(container.size) { i ->
            if (container[i].type == Function.CARET) {
                container.removeAt(i)
                return
            }
            container[i].removeCaret()
        }
    }

    fun moveCaretBefore(hash: Int) {
        repeat(container.size) { i ->
            container[i].moveCaretBefore(hash)
            if (container[i].hash == hash) {
                container.add(i, caret)
                return
            }
        }
    }

    fun stepForward(): Boolean {
        val caretIndex = container.indexOfFirst { it.type == Function.CARET }
        if (caretIndex == -1) {
            for (i in 0 until container.size) {
                container[i].stepForward()
            }
            return false
        } else {
            if (caretIndex == container.size - 1) {
                if (parentExpression == null) return true
                val index = parentExpression!!.containers.indexOf(this)
                if (index == parentExpression!!.containers.size - 1) {
                    container.removeAt(caretIndex)
                    parentContainer!!.moveCaretAfter(parentExpression!!.hash)
                } else {
                    //Log.d("Caret", "Has right direction")
                    container.removeAt(caretIndex)
                    parentExpression!!.containers[index + 1].container.add(0, caret)
                }
            } else {
                if (container[caretIndex + 1].numberOfInputs != 0)
                    container[caretIndex + 1].containers[0].container.add(0, caret)
                else
                    container.add(caretIndex + 2, caret)
                container.removeAt(caretIndex)
            }
        }
        return true
    }

    fun stepBackwards(): Boolean {
        val caretIndex = container.indexOfFirst { it.type == Function.CARET }
        if (caretIndex == -1) {
            for (i in 0 until container.size) {
                container[i].stepBackwards()
            }
            return false
        } else {
            if (caretIndex == 0) {
                if (parentExpression == null) return true
                val index = parentExpression!!.containers.indexOf(this)
                if (index == 0) {
                    container.removeAt(caretIndex)
                    parentContainer!!.moveCaretBefore(parentExpression!!.hash)
                } else {
                    Log.d("Caret", "faag")
                    container.removeAt(caretIndex)
                    parentExpression!!.containers[index - 1].container.add(caret)
                }
            } else {
                if (container[caretIndex - 1].numberOfInputs != 0) {
                    container[caretIndex - 1].containers.last().container.add(caret)
                    container.removeAt(caretIndex)
                } else {
                    container.add(caretIndex - 1, caret)
                    container.removeAt(caretIndex + 1)
                }

            }
        }
        return true
    }

    override fun toString(): String {
        var result = ""
        container.forEach {
            result += "$it "
        }
        //Log.d("Container State", result)
        return result
    }
}