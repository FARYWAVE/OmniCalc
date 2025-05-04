package com.example.omnicalc

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.omnicalc.ui.navigation.AppNavigation
import com.example.omnicalc.ui.screens.settings.SettingsViewModel
import com.example.omnicalc.ui.theme.FarywaveTheme

class MainActivity : ComponentActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FarywaveTheme(settingsViewModel = settingsViewModel) {
                AppNavigation()
            }
        }
    }
}
