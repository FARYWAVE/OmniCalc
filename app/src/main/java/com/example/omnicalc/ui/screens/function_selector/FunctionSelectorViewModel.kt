package com.example.omnicalc.ui.screens.function_selector

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omnicalc.MainActivity
import com.example.omnicalc.ui.components.ActionBarHandler
import com.example.omnicalc.ui.screens.MainViewModel
import com.example.omnicalc.utils.DatabaseRepository
import com.example.omnicalc.utils.Expression
import com.example.omnicalc.utils.Function
import com.example.omnicalc.utils.FunctionDB
import com.example.omnicalc.utils.FunctionFolder
import com.example.omnicalc.utils.FunctionFolderItem
import io.ktor.http.ContentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FunctionSelectorViewModel : ViewModel(), ActionBarHandler {

    private val repository by lazy {
        val db = FunctionDB.getDatabase(MainActivity.context)
        DatabaseRepository(db.functionDao(), db.functionFolderDao())
    }
    companion object {
        private val _currentFolder = MutableStateFlow<FunctionFolder?>(null)
        val currentFolder: StateFlow<FunctionFolder?> = _currentFolder
    }

    override val displayText: MutableState<String> = mutableStateOf(currentFolder.value?.name?: "Error1")

    init {
        viewModelScope.launch{
            viewModelScope.launch(Dispatchers.IO + NonCancellable) {
                if (currentFolder.value == null) openFolder(repository.getFolderById(1)!!)
                currentFolder.collect { folder ->
                    displayText.value = folder?.name ?: "Error2"
                }
            }

        }
    }

    val folderContents: StateFlow<List<FunctionFolderItem>> =
        _currentFolder
            .filterNotNull()
            .flatMapLatest { folder ->
                repository.getFolderContents(folder.id)
            }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    fun openFolder(folder: FunctionFolder) {
        _currentFolder.value = folder
        Log.d("CURRENT FOLDER", _currentFolder.value?.name?: " null")
    }

    fun goUp() {
        _currentFolder.value?.parentFolderId?.let { parentId ->
            viewModelScope.launch(Dispatchers.IO + NonCancellable) {
                val parent = repository.getFolderById(parentId)
                _currentFolder.value = parent
            }
        }
    }

    fun renameFolder(newName: String) {
        _currentFolder.value?.let { folder ->
            viewModelScope.launch(Dispatchers.IO + NonCancellable) {
                repository.addFolder(folder.copy(name = newName))
            }
        }
    }

    fun createNewFolder(name: String) {
        _currentFolder.value?.let { parent ->
            viewModelScope.launch(Dispatchers.IO + NonCancellable) {
                Log.d("NEW FOLDER CREATION", "${parent.id} ${parent.name}")
                repository.addFolder(FunctionFolder(id = 0, name = name, parentFolderId = parent.id))
            }
        }
    }

    fun duplicateFolder() {
        _currentFolder.value?.let {
            viewModelScope.launch(Dispatchers.IO + NonCancellable) {
                repository.addFolder(_currentFolder.value!!.copy(id = 0, name = "Copy of " + currentFolder.value?.name!!))
            }
        }
    }

    fun deleteCurrentFolder() {
        _currentFolder.value?.let { folder ->
            viewModelScope.launch(Dispatchers.IO + NonCancellable) {
                repository.delete(folder)
                folderContents.value.forEach {
                    repository.delete(it)
                }
                goUp()
            }
        }
    }

    fun addFunction(name: String) {
        _currentFolder.value?.let {folder ->
            viewModelScope.launch(Dispatchers.IO + NonCancellable) {
                MainViewModel.rootContainer.removeCaret()
                repository.addFunction(
                        Function(id = 0,
                            expression = MainViewModel.rootContainer.apply {
                                removeCaret()
                            },
                            name = name,
                            parentFolderId = folder.id
                        ))
                MainViewModel.rootContainer.container.add(Expression(com.example.omnicalc.ui.components.display.Function.CARET))
            }
        }

    }


    override fun onKeyPressed(key: ActionBarHandler.Key, confirmed: Boolean) {
        if (confirmed) when (key) {
            ActionBarHandler.Key.MOVE -> {goUp()}
            ActionBarHandler.Key.COPY -> {duplicateFolder()}
            ActionBarHandler.Key.SAVE -> {renameFolder(displayText.value)}
            ActionBarHandler.Key.ADD -> {}
            ActionBarHandler.Key.DELETE -> {deleteCurrentFolder()}
        }
    }
}