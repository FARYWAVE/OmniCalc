package com.example.omnicalc.ui.screens.convertor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.omnicalc.ui.components.KeyPressHandler
import com.example.omnicalc.utils.Length
import com.example.omnicalc.utils.Measurement
import com.example.omnicalc.utils.MeasurementUnit

class ConvertorViewModel : ViewModel(), KeyPressHandler {
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
    override fun onKeyPress(keyName: String) {

    }
}