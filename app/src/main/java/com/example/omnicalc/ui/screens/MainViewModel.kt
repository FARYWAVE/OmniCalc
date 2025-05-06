package com.example.omnicalc.ui.screens


import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.omnicalc.ui.components.*
import com.example.omnicalc.ui.components.display.DisplayClickHandler
import com.example.omnicalc.ui.components.display.Function
import com.example.omnicalc.utils.Expression
import com.example.omnicalc.utils.ExpressionContainer

class MainViewModel : ViewModel(), KeyPressHandler, DisplayClickHandler {
    val rootContainer = ExpressionContainer(mutableStateListOf(Expression(Function.CARET)))
    private var caretMovingInProgress = false
    override fun onKeyPress(keyName: String) {
        val type = Function.fromFunctionName(keyName.substringBefore('/'))
        Log.d("Key Handler", "Key called: $keyName, Detected: $type")
        when (type) {
            Function.STEP_FORWARD -> rootContainer.stepForward()
            Function.STEP_BACKWARDS -> rootContainer.stepBackwards()
            Function.NUMBER -> rootContainer.add(type, keyName.last())
            Function.BACKSPACE -> rootContainer.delete()
            Function.CLEAR -> {
                rootContainer.container.clear()
                rootContainer.container.add(Expression(Function.CARET))
            }
            else -> type?.let {
                rootContainer.add(it)
            }
        }
        Log.d("Root Container State", rootContainer.toString())
    }

    override fun onDisplayClicked(hash: Int, after: Boolean) {
        if (!caretMovingInProgress) {
            Log.d("CaretManagement", "Moving to $hash, after: $after")
            caretMovingInProgress = true
            rootContainer.removeCaret()
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
            rootContainer.moveCaretIn(hash, start)
            caretMovingInProgress = false
            Log.d("Root Container State", rootContainer.toString())
        }
    }
}