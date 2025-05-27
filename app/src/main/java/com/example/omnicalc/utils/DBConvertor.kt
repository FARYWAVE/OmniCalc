package com.example.omnicalc.utils

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DBConvertor {

    companion object {
        private val json = Json { prettyPrint = false }
        @TypeConverter
        fun fromExpressionContainer(container: ExpressionContainer) : String {
            return json.encodeToString<SerializableContainer>(container.toSerializable())
        }
        @TypeConverter
        fun toExpressionContainer(data: String) : ExpressionContainer {
            return json.decodeFromString<SerializableContainer>(data).toExpressionContainer()
        }
    }

    @TypeConverter
    fun fromExpressionContainer(container: ExpressionContainer) : String {
        return json.encodeToString<SerializableContainer>(container.toSerializable())
    }

    @TypeConverter
    fun toExpressionContainer(data: String) : ExpressionContainer {
        return json.decodeFromString<SerializableContainer>(data).toExpressionContainer()
    }


    @TypeConverter
    fun fromExpression(expression: Expression) : String {
        return json.encodeToString<SerializableExpression>(expression.toSerializable())
    }

    @TypeConverter
    fun toExpression(data: String) : Expression {
        return json.decodeFromString<SerializableExpression>(data).toExpression()
    }


    @TypeConverter
    fun fromList(functionIds: List<Int>): String {
        return functionIds.joinToString(",")
    }

    @TypeConverter
    fun toList(data: String) : List<Int> {
        return data.split(",").map { it.toInt() }
    }
}