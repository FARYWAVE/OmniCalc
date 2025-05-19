package com.example.omnicalc.utils

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class DatabaseRepository(
    private val functionDao: FunctionDao,
    private val folderDao: FunctionFolderDao
) {
    fun getFolderContents(folderId: Int): Flow<List<FunctionFolderItem>> = combine(
        functionDao.getFunctionsIn(folderId),
        folderDao.getFoldersIn(folderId)
    ) { functions, folders ->
        val items = mutableListOf<FunctionFolderItem>()
        items.addAll(folders)
        items.addAll(functions)
        items
    }

    suspend fun getFolderById(folderId: Int): FunctionFolder? {
        return folderDao.getFolderById(folderId)
    }

    suspend fun getFunctionById(functionId: Int): Function? {
        return functionDao.getFunctionById(functionId)
    }

    fun getAllFolders() : Flow<List<FunctionFolder>> = folderDao.getAll()

    fun getAllFunctions() : Flow<List<Function>> = functionDao.getAll()

    suspend fun addFolder(folder: FunctionFolder) {
        folderDao.insert(folder)
    }

    suspend fun addFunction(function: Function) {
        functionDao.insert(function)
    }

    suspend fun delete(item: FunctionFolderItem) {
        if (item is Function) functionDao.delete(item)
        else folderDao.delete(item as FunctionFolder)
    }

}