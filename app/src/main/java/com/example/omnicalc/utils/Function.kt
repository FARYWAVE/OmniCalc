package com.example.omnicalc.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import com.example.omnicalc.ui.components.display.DisplayClickHandler
import com.example.omnicalc.ui.screens.function_selector.FunctionCard
import com.example.omnicalc.ui.screens.function_selector.FunctionFolderCard
import com.example.omnicalc.ui.screens.function_selector.FunctionSelectorViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable


@Entity(tableName = "functions")
data class Function(
    @PrimaryKey (autoGenerate = true) val id: Int = 0,
    val expression: ExpressionContainer,
    var name: String,
    override var parentFolderId: Int
) : DisplayClickHandler, FunctionFolderItem {
    override fun onDisplayClicked(hash: Int, after: Boolean) {}

    override fun onSpecialClicked(hash: Int, start: Boolean) {}

    @Composable
    override fun Compose(modifier: Modifier, viewModel: FunctionSelectorViewModel, navController: NavController) {
        FunctionCard(modifier, this, navController)
    }
}

@Entity(tableName = "function_folders")
data class FunctionFolder(
    @PrimaryKey (autoGenerate = true) val id: Int = 0,
    var name: String,
    override var parentFolderId: Int
) : FunctionFolderItem {
    @Composable
    override fun Compose(modifier: Modifier, viewModel: FunctionSelectorViewModel, navController: NavController) {
        FunctionFolderCard(modifier, this, viewModel)
    }
}

@Dao
interface FunctionDao {
    @Query("SELECT * FROM functions")
    fun getAllFunctions(): Flow<List<Function>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(function: Function) : Long

    @Delete
    suspend fun delete(function: Function) : Int

    @Query("SELECT * FROM functions")
    fun getAll() : Flow<List<Function>>

    @Query("SELECT * FROM functions WHERE id = :id LIMIT 1")
    suspend fun getFunctionById(id: Int): Function?

    @Query("SELECT * FROM functions WHERE parentFolderId = :id")
    fun getFunctionsIn(id: Int) : Flow<List<Function>>

}

@Dao
interface FunctionFolderDao {
    @Query("SELECT * FROM function_folders")
    fun getAllFolders(): Flow<List<FunctionFolder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(folder: FunctionFolder): Long

    @Delete
    suspend fun delete(folder: FunctionFolder)

    @Query("SELECT * FROM function_folders")
    fun getAll() : Flow<List<FunctionFolder>>

    @Query("SELECT * FROM function_folders WHERE id = :id LIMIT 1")
    suspend fun getFolderById(id: Int): FunctionFolder?

    @Query("SELECT * FROM function_folders WHERE parentFolderId = :id")
    fun getFoldersIn(id: Int) : Flow<List<FunctionFolder>>
}

interface FunctionFolderItem {
    var parentFolderId: Int
    @Composable
    fun Compose(modifier: Modifier = Modifier, viewModel: FunctionSelectorViewModel, navController: NavController)
}