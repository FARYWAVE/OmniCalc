package com.example.omnicalc.utils

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@TypeConverters(DBConvertor::class)
@Database(entities = [Function::class, FunctionFolder::class], version = 2)
abstract class FunctionDB : RoomDatabase() {
    abstract fun functionDao(): FunctionDao
    abstract fun functionFolderDao(): FunctionFolderDao

    companion object {
        @Volatile
        private var INSTANCE: FunctionDB? = null

        fun getDatabase(context: Context): FunctionDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FunctionDB::class.java,
                    "function_database"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                getDatabase(context).functionFolderDao().insert(
                                    FunctionFolder(
                                        id = 0,
                                        name = "Root",
                                        parentFolderId = -1
                                    )
                                )

                            }
                        }
                    })
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}

@Serializable
data class SerializableExpression(
    val type: String,
    val containers: List<SerializableContainer>,
    val value: Char
) {
    fun toExpression(): Expression {
        val type = com.example.omnicalc.ui.components.display.Function.fromFunctionName(type)!!
        val expr = when (type) {
            com.example.omnicalc.ui.components.display.Function.NUMBER -> NumericExpression(value)
            com.example.omnicalc.ui.components.display.Function.VARIABLE -> VariableExpression(value)
            else -> Expression(type)
        }
        containers.forEachIndexed { i, c ->
            expr.setValues(i, c.toExpressionContainer())
        }
        return expr
    }
}

@Serializable
data class SerializableContainer(
    val expressions: List<SerializableExpression>
) {
    fun toExpressionContainer(): ExpressionContainer {
        val container = ExpressionContainer()
        expressions.forEach { serExpr ->
            val child = serExpr.toExpression()
            container.container.add(child)
            child.setParentContainer(container)
        }
        return container
    }
}

