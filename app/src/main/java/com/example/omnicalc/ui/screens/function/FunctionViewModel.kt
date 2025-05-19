package com.example.omnicalc.ui.screens.function

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.omnicalc.MainActivity
import com.example.omnicalc.ui.components.ActionBarHandler
import com.example.omnicalc.ui.screens.MainViewModel
import com.example.omnicalc.utils.DatabaseRepository
import com.example.omnicalc.utils.Expression
import com.example.omnicalc.utils.Function
import com.example.omnicalc.utils.FunctionDB
import com.example.omnicalc.utils.FunctionFolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class FunctionViewModel : ViewModel(), ActionBarHandler {

    private val repository by lazy {
        val db = FunctionDB.getDatabase(MainActivity.context)
        DatabaseRepository(db.functionDao(), db.functionFolderDao())
    }

    override val displayText: MutableState<String> = mutableStateOf("Error1")
    companion object {
        private val _currentFunction = MutableStateFlow<Function?>(null)
        val currentFunction: StateFlow<Function?> = _currentFunction
    }

    init {
        MainViewModel.rootContainer.stepBackwards()
        MainViewModel.rootContainer.stepForward()
        viewModelScope.launch(Dispatchers.IO + NonCancellable) {
            currentFunction.collect { function ->
                displayText.value = function?.name ?: "Error2"
            }
        }

    }

    fun addFunction(function: Function) {
        viewModelScope.launch(Dispatchers.IO + NonCancellable) {
            repository.addFunction(function)
        }
    }

    fun setFunction(id: Int) {
        viewModelScope.launch(Dispatchers.IO + NonCancellable) {
            _currentFunction.value = repository.getFunctionById(id)
        }
    }

    fun duplicateFunction() {
        _currentFunction.value?.let {function ->
            viewModelScope.launch(Dispatchers.IO + NonCancellable) {
                repository.addFunction(function.copy(id = 0, name = "Copy of " + function.name))
            }
        }
    }

    fun deleteFunction(function: Function) {
        viewModelScope.launch(Dispatchers.IO + NonCancellable) {
            repository.delete(function)
        }
    }

    override fun onKeyPressed(key: ActionBarHandler.Key, confirmed: Boolean) {
        if (currentFunction.value == null) return
        if (confirmed) when (key) {
            ActionBarHandler.Key.SAVE -> {
                MainViewModel.rootContainer.removeCaret()
                addFunction(currentFunction.value!!.copy(name = displayText.value, expression = MainViewModel.rootContainer))
                MainViewModel.rootContainer.container.add(Expression(com.example.omnicalc.ui.components.display.Function.CARET))
            }
            ActionBarHandler.Key.DELETE -> {
                deleteFunction(currentFunction.value!!)
            }
            ActionBarHandler.Key.COPY -> {
                duplicateFunction()
            }
            else -> {}
        }
    }
}