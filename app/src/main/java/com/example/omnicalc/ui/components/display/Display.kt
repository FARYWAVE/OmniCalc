package com.example.omnicalc.ui.components.display


import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.omnicalc.ui.screens.MainViewModel
import com.example.omnicalc.ui.screens.settings.SettingsViewModel
import com.example.omnicalc.utils.VariableManager
import java.math.BigDecimal
import java.math.RoundingMode

fun Double.applyAccuracy(accuracy: Int): String {
    if (accuracy == 11) return this.toString()
    return try {
        val bd = BigDecimal(this)
            .setScale(accuracy, RoundingMode.HALF_UP)
            .stripTrailingZeros()
        bd.toPlainString()
    } catch (e: Exception) {
        this.toString()
    }
}

@Composable
fun Display() {
    val viewModel: MainViewModel = viewModel()
    Log.d("Variable Manager", "Variables found: ${VariableManager.variables.map { it.name }}")
    Column(Modifier.fillMaxWidth()) {
        LazyRow {
            item {
                ExpressionContainer(
                    modifier = Modifier.padding(10.dp),
                    container = viewModel.rootContainer,
                    fontSize = 22,
                    viewModel = viewModel
                )
            }
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            items(VariableManager.variables) { variable ->
                Log.d("Variabe", variable.name.toString())
                VariableCard(variable = variable, viewModel = variable)
            }
        }
    }
}


interface DisplayClickHandler {
    fun onDisplayClicked(hash: Int, after: Boolean = true)
    fun onSpecialClicked(hash: Int, start: Boolean = true)
}
