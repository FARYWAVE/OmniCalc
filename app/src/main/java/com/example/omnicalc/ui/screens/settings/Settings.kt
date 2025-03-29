package com.example.omnicalc.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.omnicalc.utils.vw


@Composable
fun SettingsDrawer(onClose: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(70.vw())
            .padding(16.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Dark Mode")
            Switch(checked = false, onCheckedChange = {})
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onClose) {
            Text("Close")
        }
    }
}