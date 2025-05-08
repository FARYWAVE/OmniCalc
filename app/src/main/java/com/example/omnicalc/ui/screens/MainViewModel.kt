package com.example.omnicalc.ui.screens


import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.example.omnicalc.ui.components.*
import com.example.omnicalc.ui.components.display.DisplayClickHandler
import com.example.omnicalc.ui.components.display.Function
import com.example.omnicalc.utils.Expression
import com.example.omnicalc.utils.ExpressionContainer
import com.example.omnicalc.utils.VariableManager

class MainViewModel : ViewModel(), KeyPressHandler, DisplayClickHandler {
    val rootContainer = ExpressionContainer(mutableStateListOf(Expression(Function.CARET)))
    var result: MutableState<Double> = mutableDoubleStateOf(0.0)
    private var caretMovingInProgress = false
    override fun onKeyPress(keyName: String) {
        if (VariableManager.rootContainer == null) VariableManager.rootContainer = rootContainer
        VariableManager.variables.forEach { it.onKeyPress(keyName) }
        val type = Function.fromFunctionName(keyName.substringBefore('/'))
        Log.d("Key Handler", "Key called: $keyName, Detected: $type")
        when (type) {
            Function.STEP_FORWARD -> rootContainer.stepForward()
            Function.STEP_BACKWARDS -> rootContainer.stepBackwards()
            Function.NUMBER, Function.VARIABLE -> {
                rootContainer.add(type, keyName.last())
                if (type == Function.VARIABLE) VariableManager.addVar(keyName.last())
            }
            Function.BACKSPACE -> rootContainer.delete()
            Function.CLEAR -> {
                if (rootContainer.hasCaret()) {
                    VariableManager.setVars(listOf())
                    rootContainer.container.clear()
                    rootContainer.container.add(Expression(Function.CARET))
                }
            }

            else -> type?.let {
                rootContainer.add(it)
            }
        }
        Log.d("Root Container State", rootContainer.toString())
        try {
            if ((rootContainer.container.size == 1) and (rootContainer.container[0].type == Function.CARET))
                result.value = 0.0
            else result.value = rootContainer.solve()
        } catch (e: Exception) {
            Log.d("Calculation", "Error: ${e.cause}\n${e.message}")
            result.value = 4.583945721467122
        }
    }

    override fun onDisplayClicked(hash: Int, after: Boolean) {
        if (!caretMovingInProgress) {
            Log.d("CaretManagement", "Moving to $hash, after: $after")
            caretMovingInProgress = true
            rootContainer.removeCaret()
            VariableManager.removeCarets()
            if (after) rootContainer.moveCaretAfter(hash)
            else rootContainer.moveCaretBefore(hash)
            caretMovingInProgress = false
            Log.d("Root Container State", rootContainer.toString())
        }
    }

    override fun onSpecialClicked(hash: Int, start: Boolean) {
        if (!caretMovingInProgress) {
            Log.d("CaretManagement", "Moving to $hash, start: $start")
            caretMovingInProgress = true
            rootContainer.removeCaret()
            VariableManager.removeCarets()
            rootContainer.moveCaretIn(hash, start)
            caretMovingInProgress = false
            Log.d("Root Container State", rootContainer.toString())
        }
    }
}