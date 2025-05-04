package com.example.omnicalc.ui.screens


import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.omnicalc.ui.components.*
import com.example.omnicalc.ui.components.Function
import com.example.omnicalc.ui.components.display.Expression
import com.example.omnicalc.ui.components.display.ExpressionContainer

class MainViewModel : ViewModel(), KeyPressHandler {
    val rootContainer = ExpressionContainer(mutableStateListOf(Expression(Function.CARET)))
    override fun onKeyPress(keyName: String) {
        val type = Function.fromFunctionName(keyName.substringBefore('/'))
        Log.d("Key Handler", "Key called: $keyName, Detected: $type")
        when (type) {
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
    }
}