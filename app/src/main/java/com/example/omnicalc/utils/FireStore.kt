package com.example.omnicalc.utils

import com.example.omnicalc.MainActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class FirestoreFunction(
    val id: Int = 0,
    val expression: String = "",
    val name: String = "",
)

data class FirestoreFunctionFolder(
    val id: Int = 0,
    val name: String = "",
    val parentFolderId: Int = 0
)


class FirebaseRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val repository by lazy {
        val db = FunctionDB.getDatabase(MainActivity.context)
        DatabaseRepository(db.functionDao(), db.functionFolderDao())
    }

    suspend fun loadAllFolders(): MutableMap<String, String> {
        val foldersSnapshot = firestore.collection("folders").get().await()
        val result = mutableMapOf<String, String>()

        for (doc in foldersSnapshot.documents) {
            val name = doc.getString("name") ?: continue
            result[doc.id] = name
        }

        return result
    }

    suspend fun loadFunctionsFrom(id: String, name: String) {
        val folder = firestore.collection("folders").document(id).get().await()
        val functionIds = folder.get("functions") as? List<String> ?: emptyList()
        val folderId = repository.addFolder(
            FunctionFolder(
                id = 0,
                name = name,
                parentFolderId = 0
            )
        )

        for (id in functionIds) {
            val funcDoc = firestore.collection("functions").document(id).get().await()
            val funcName = funcDoc.getString("name") ?: continue
            val exprSerialized = funcDoc.getString("expression") ?: continue
            repository.addFunction(
                Function(
                    id = 0,
                    name = funcName,
                    expression = DBConvertor.toExpressionContainer(exprSerialized),
                    parentFolderId = folderId.toInt()
                )
            )
        }
    }
}