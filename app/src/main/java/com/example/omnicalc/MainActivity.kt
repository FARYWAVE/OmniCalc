package com.example.omnicalc

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.omnicalc.ui.navigation.AppNavigation
import com.example.omnicalc.ui.screens.settings.SettingsViewModel
import com.example.omnicalc.ui.theme.FarywaveTheme
import com.example.omnicalc.ui.theme.ThemeManager
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()

    companion object {
        private var instance: MainActivity? = null

        val context: Context
            get() = instance!!.applicationContext
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        instance = this
        super.onCreate(savedInstanceState)

        setContent {
            FarywaveTheme(settingsViewModel = settingsViewModel) {
                AppNavigation()
            }
        }
    }
}
