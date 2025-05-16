package com.example.omnicalc.ui.components.display

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.example.omnicalc.R
import com.example.omnicalc.utils.Expression
import java.security.spec.ECFieldFp

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
        override fun Compose(expression: Expression, fontSize: Int, viewModel: DisplayClickHandler) {
            Caret(expression, fontSize, viewModel)
        }
    },
    BRACKETS("brackets", "( )", null, 1) {
        @Composable
        override fun Compose(expression: Expression, fontSize: Int, viewModel: DisplayClickHandler) {
            Brackets(expression,  fontSize, viewModel)
        }
    },
    ABSOLUTE("absolute", "| |", null, 1) {
        @Composable
        override fun Compose(expression: Expression, fontSize: Int, viewModel: DisplayClickHandler) {
            Brackets(expression,  fontSize, viewModel)
        }
    },

    // Comparison
    GREATER("greater", ">", null, inputIndex = -1) {
        @Composable
        override fun Compose(expression: Expression, fontSize: Int, viewModel: DisplayClickHandler) {
            Operator(expression, displayText, fontSize, viewModel)
        }
    },
    GREATER_EQUALS("greater_equals", "≥", null, inputIndex = -1) {
        @Composable
        override fun Compose(expression: Expression, fontSize: Int, viewModel: DisplayClickHandler) {
            Operator(expression, displayText, fontSize, viewModel)
        }
    },
    EQUALS("equals", "=", null, inputIndex = -1) {
        @Composable
        override fun Compose(expression: Expression, fontSize: Int, viewModel: DisplayClickHandler) {
            Operator(expression, displayText, fontSize, viewModel)
        }
    },

    STEP_BACKWARDS("step_backwards", "", R.drawable.arrow_back),
    STEP_FORWARD("step_forward", "", R.drawable.arrow_forward),

    LESS_EQUALS("less_equals", "≤", null, inputIndex = -1) {
        @Composable
        override fun Compose(expression: Expression, fontSize: Int, viewModel: DisplayClickHandler) {
            Operator(expression, displayText, fontSize, viewModel)
        }
    },
    LESS("less", "<", null, inputIndex = -1) {
        @Composable
        override fun Compose(expression: Expression, fontSize: Int, viewModel: DisplayClickHandler) {
            Operator(expression, displayText, fontSize, viewModel)
        }
    },

    // Fractions & Roots
    FRACTION("fraction", "½", null, 2) {
        @Composable
        override fun Compose(expression: Expression, fontSize: Int, viewModel: DisplayClickHandler) {
            Fraction(expression, fontSize, viewModel)
        }
    },
    ROOT_SQUARE("root_square", "√", null, inputIndex = 1),
    ROOT_CUBIC("root_cubic", "∛", null, inputIndex = 1),
    ROOT("root", "°√", null, 2, 0) {
        @Composable
        override fun Compose(expression: Expression, fontSize: Int, viewModel: DisplayClickHandler) {
            Root(expression, fontSize, viewModel)
        }
    },

    // Powers
    POWER_SQUARE("power_square", "x²", null, inputIndex = -1),
    POWER_CUBIC("power_cubic", "x³", null, inputIndex = -1),
    POWER("power", "x°", null, 1, 0) {
        @Composable
        override fun Compose(expression: Expression, fontSize: Int, viewModel: DisplayClickHandler) {
            Power(expression, fontSize, viewModel)
        }
    },

    // Constants
    PI("pi", "π", null, inputIndex = -1) {
        @Composable
        override fun Compose(expression: Expression, fontSize: Int, viewModel: DisplayClickHandler) {
            Operator(expression, displayText, fontSize, viewModel)
        }
    },
    PI_DIV_2("pi_div_2", "π/2", null, inputIndex = -1),
    PI_DIV_3("pi_div_3", "π/3", null, inputIndex = -1),
    PERCENT("percent", "%", null, inputIndex = -1),
    REMAINDER("remainder", " rem ", null, inputIndex = -1),

    // Operators
    PLUS("plus", "+", null, inputIndex = -1) {
        @Composable
        override fun Compose(expression: Expression, fontSize: Int, viewModel: DisplayClickHandler) {
            Operator(expression, displayText, fontSize, viewModel)
        }
    },
    MINUS("minus", "-", null, inputIndex = -1) {
        @Composable
        override fun Compose(expression: Expression, fontSize: Int, viewModel: DisplayClickHandler) {
            Operator(expression, displayText, fontSize, viewModel)
        }
    },
    MULTIPLY("multiply", "×", null, inputIndex = -1) {
        @Composable
        override fun Compose(expression: Expression, fontSize: Int, viewModel: DisplayClickHandler) {
            Operator(expression, displayText, fontSize, viewModel)
        }
    },
    DIVIDE("divide", "÷", null, inputIndex = -1) {
        @Composable
        override fun Compose(expression: Expression, fontSize: Int, viewModel: DisplayClickHandler) {
            Operator(expression, displayText, fontSize, viewModel)
        }
    },
    DOT("dot", ".", null, inputIndex = -1) {
        @Composable
        override fun Compose(expression: Expression, fontSize: Int, viewModel: DisplayClickHandler) {
            Operator(expression, displayText, fontSize, viewModel)
        }
    },

    // Input
    NUMBER("number", "n", null, 0, -1),
    VARIABLE("variable", "ch", null, inputIndex = -1),
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

    DEGREE("degree", "°", null, 0, inputIndex = -1) {
        @Composable
        override fun Compose(expression: Expression, fontSize: Int, viewModel: DisplayClickHandler) {
            Operator(expression, displayText, fontSize, viewModel)
        }
    },
    RAD("radian", "rad", null, 1),

    // Logarithmic & Exponential
    LN("ln", "ln", null, inputIndex = 1),
    LOG("log", "log", null, 2) {
        @Composable
        override fun Compose(expression: Expression, fontSize: Int, viewModel: DisplayClickHandler) {
            Logarithm(expression, fontSize, viewModel)
        }
    },
    LG("lg", "lg", null, inputIndex = 1),
    EXP("exp", "exp", null, 1, -1),

    // Miscellaneous
    FACTORIAL("factorial", "!", null, inputIndex = -1),
    E("e", "e", null, inputIndex = -1) {
        @Composable
        override fun Compose(expression: Expression, fontSize: Int, viewModel: DisplayClickHandler) {
            Operator(expression, displayText, fontSize, viewModel)
        }
    };

    @Composable
    open fun Compose(expression: Expression, fontSize: Int, viewModel: DisplayClickHandler) {
        if (this.numberOfInputs == 1) SimpleExpression(expression, fontSize, viewModel)
        else Operator(expression, this.displayText, fontSize, viewModel)
    }

    fun number(num: Int): String {
        return "number/$num"
    }

    fun variable(char: Char): String {
        return "variable/$char"
    }

    companion object {
        fun fromFunctionName(name: String): Function? {
            Log.d("Function Parser", "Recieved: $name, Found: ${entries.firstOrNull { it.functionName == name }}")
            return entries.firstOrNull { it.functionName == name }
        }
    }
}
