package com.example.omnicalc.ui.screens.settings

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.omnicalc.ui.theme.ThemeManager
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val Application.dataStore by preferencesDataStore(name = "settings")

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore = application.dataStore

    private val THEME_KEY = booleanPreferencesKey("dark_mode")
    private val ORIENTATION_KEY = booleanPreferencesKey("left_handed")
    private val PRIMARY_COLOR_KEY = intPreferencesKey("primary_color")

    val isDarkMode: Flow<Boolean> = dataStore.data.map { it[THEME_KEY] ?: false }
    val isLeftHanded: Flow<Boolean> = dataStore.data.map { it[ORIENTATION_KEY] ?: false }
    val primaryColor: Flow<Color> = dataStore.data
        .map { prefs -> Color(prefs[PRIMARY_COLOR_KEY] ?: Color(0xFF6200EE).toArgb()) } // Default color

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit { it[THEME_KEY] = enabled }
        }
    }

    fun toggleLeftHanded(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit { it[ORIENTATION_KEY] = enabled }
            ThemeManager.applyTheme(enabled)
        }
    }

    fun onPrimaryColorChanged(color: Color) {
        viewModelScope.launch {
            dataStore.edit { it[PRIMARY_COLOR_KEY] = color.toArgb() }
        }
        ThemeManager.setAppColor(color)
    }
}
