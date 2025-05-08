package com.example.omnicalc.utils

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.omnicalc.ui.components.KeyPressHandler
import com.example.omnicalc.ui.components.display.DisplayClickHandler
import com.example.omnicalc.ui.components.display.Function

class VariableManager {
    data class Variable(
        val name: Char,
        val expression: ExpressionContainer = ExpressionContainer(mutableStateListOf())
    ) : DisplayClickHandler, KeyPressHandler {
        override fun onDisplayClicked(hash: Int, after: Boolean) {
            removeCarets()
            removeCaretFromRoot()
            if (after) expression.moveCaretAfter(hash)
            else expression.moveCaretBefore(hash)
        }
        override fun onSpecialClicked(hash: Int, start: Boolean) {
            Log.d("Variable Clicked", "Empty, Hash: $hash")
            removeCarets()
            removeCaretFromRoot()
            expression.moveCaretIn(hash, start)
        }

        private var result = 0
        fun getValue(): Double? {
            return try {
                expression.solve()
            } catch (e: Exception) {
                null
            }
        }

        override fun onKeyPress(keyName: String) {
            val type = Function.fromFunctionName(keyName.substringBefore('/'))
            when (type) {
                Function.STEP_FORWARD -> expression.stepForward()
                Function.STEP_BACKWARDS -> expression.stepBackwards()
                Function.NUMBER -> expression.add(type, keyName.last())
                Function.VARIABLE -> {}
                Function.BACKSPACE -> expression.delete()
                Function.CLEAR -> {
                    if (expression.hasCaret()) {
                        expression.container.clear()
                        expression.container.add(Expression(Function.CARET))
                    }
                }
                else -> type?.let {
                    expression.add(it)
                }
            }
            Log.d("Variable Container State", expression.toString())

        }
    }
    companion object {
        private val usages = mutableMapOf<Char, Int>()
        var rootContainer: ExpressionContainer? = null
        private var _variables = mutableStateListOf<Variable>()
        var variables: MutableList<Variable>
            get() = _variables
            private set(_) {}

        fun removeCaretFromRoot() {
            rootContainer?.removeCaret()
        }

        fun setVars(variables: List<Variable>) {
            _variables.clear()
            _variables.addAll(variables.distinctBy { it.name })
        }

        fun addVar(char: Char) {
            if (char in usages.keys) usages[char] = usages[char]!! + 1
            else usages[char] = 1
            if (_variables.none { it.name == char }) {
                Log.d("Variable Manager", "Added: $char")
                _variables.add(Variable(char))
            }
        }
        fun removeVar(char: Char) {
            if (char in usages.keys) {
                if (usages[char]!! == 1) {
                    _variables.removeAll { it.name == char }
                    usages.remove(char)
                } else usages[char] = usages[char]!! - 1
            }
        }

        fun removeCarets() {
            _variables.forEach { it.expression.removeCaret() }
        }

        fun getValueByName(name: Char) = variables.first { it.name == name }.getValue()
    }
}