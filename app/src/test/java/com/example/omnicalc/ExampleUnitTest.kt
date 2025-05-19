package com.example.omnicalc

import com.example.omnicalc.utils.ExpressionContainer
import org.junit.Test
import com.example.omnicalc.ui.components.display.Function

import org.junit.Assert.*

class ExpressionLogicTest {
    private fun newRoot(): ExpressionContainer {
        val root = ExpressionContainer()
        // Place caret to start building
        root.container.add(root.caret)
        return root
    }

    @Test
    fun testSimpleAdditionAndMultiplicationPrecedence() {
        val root = newRoot()
        // Expression: 2 + 3 * 4 = 14
        root.add(Function.NUMBER, '2')
        root.add(Function.PLUS)
        root.add(Function.NUMBER, '3')
        root.add(Function.MULTIPLY)
        root.add(Function.NUMBER, '4')

        val result = root.solve()
        assertEquals(14.0, result, 1e-9)
    }

    @Test
    fun testDecimalNumbers() {
        val root = newRoot()
        // Expression: 1.5 + 2.25 = 3.75
        root.add(Function.NUMBER, '1')
        root.add(Function.DOT)
        root.add(Function.NUMBER, '5')
        root.add(Function.PLUS)
        root.add(Function.NUMBER, '2')
        root.add(Function.DOT)
        root.add(Function.NUMBER, '2')
        root.add(Function.NUMBER, '5')

        val result = root.solve()
        assertEquals(3.75, result, 1e-9)
    }

    @Test
    fun testPowerAndSquare() {
        val root = newRoot()
        // Expression: 3^2 + 2^3 = 9 + 8 = 17
        root.add(Function.NUMBER, '3')
        root.add(Function.POWER_SQUARE)
        root.add(Function.PLUS)
        root.add(Function.NUMBER, '2')
        root.add(Function.POWER_CUBIC)

        val result = root.solve()
        assertEquals(17.0, result, 1e-9)
    }

    @Test
    fun testSquareRootAndCubeRoot() {
        val root = newRoot()
        // Expression: sqrt(16) + cbrt(27) = 4 + 3 = 7
        root.add(Function.ROOT_SQUARE)
        root.add(Function.NUMBER, '1')
        root.add(Function.NUMBER, '6')
        root.add(Function.PLUS)
        root.add(Function.ROOT_CUBIC)
        root.add(Function.NUMBER, '2')
        root.add(Function.NUMBER, '7')

        val result = root.solve()
        assertEquals(7.0, result, 1e-9)
    }

    @Test
    fun testTrigonometricAndConstants() {
        val root = newRoot()
        // Expression: PI * sin(PI/2) = PI * 1 = PI
        root.add(Function.PI)
        root.add(Function.MULTIPLY)
        root.add(Function.SIN)
        // default LN or inner container? insert PI/2
        root.add(Function.PI_DIV_2)

        val result = root.solve()
        assertEquals(Math.PI, result, 1e-9)
    }

    @Test
    fun testLogarithms() {
        val root = newRoot()
        // Expression: log_10(1000) = 3
        root.add(Function.LG)
        root.add(Function.NUMBER, '1')
        root.add(Function.NUMBER, '0')
        root.add(Function.NUMBER, '0')
        root.add(Function.NUMBER, '0')

        val result = root.solve()
        assertEquals(3.0, result, 1e-9)
    }

    @Test
    fun testFractionFunction() {
        val root = newRoot()
        // Expression: Fraction 1/4 = 0.25
        root.add(Function.FRACTION)
        // numerator default is FIRST container, caret moves inside
        root.add(Function.NUMBER, '1')
        root.stepForward()
        root.add(Function.NUMBER, '4')

        val result = root.solve()
        assertEquals(0.25, result, 1e-9)
    }

    @Test
    fun testNestedExpressions() {
        val root = newRoot()
        // Expression: (2 + 3) * 4 = 20
        root.add(Function.BRACKETS)
        root.add(Function.NUMBER, '2')
        root.add(Function.PLUS)
        root.add(Function.NUMBER, '3')
        root.stepForward()
        root.add(Function.MULTIPLY)
        root.add(Function.NUMBER, '4')

        val result = root.solve()
        assertEquals(20.0, result, 1e-9)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInvalidDecimalThrows() {
        val root = newRoot()
        // Expression: 1..2 should throw
        root.add(Function.NUMBER, '1')
        root.add(Function.DOT)
        root.add(Function.NUMBER, '.')
        root.solve()
    }
}
