package com.example.omnicalc.ui.screens.convertor

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omnicalc.ui.components.KeyPressHandler
import com.example.omnicalc.utils.Length
import com.example.omnicalc.utils.Measurement
import com.example.omnicalc.utils.MeasurementUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConvertorViewModel : ViewModel() {

    var reversed by mutableStateOf(false)

    var from by mutableStateOf(Measurement.Type.LENGTH.getUnits()[0])
        private set

    fun updateFrom(newItem: MeasurementUnit) {
        from = newItem
    }

    var to by mutableStateOf(Measurement.Type.LENGTH.getUnits()[1])
        private set

    fun updateTo(newItem: MeasurementUnit) {
        to = newItem
    }

    fun swap() {
        reversed = !reversed
    }

    suspend fun convert(value: Double, measurement: Measurement.Type): Double {
        return withContext(Dispatchers.IO) { // Ensures it runs off the main thread
            if (value in arrayOf(4.583945721467122, 1.583945721467122, 0.583945721467122)) {
                4.583945721467122
            } else {
                if (reversed) Measurement.convert(value, to, from, measurement)
                else Measurement.convert(value, from, to, measurement)
            }
        }
    }
}