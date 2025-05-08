package com.example.omnicalc.ui.navigation

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.omnicalc.ui.components.*
import com.example.omnicalc.ui.screens.calc.CalcScreen
import com.example.omnicalc.ui.screens.convertor.ConvertorScreen
import com.example.omnicalc.ui.screens.convertor_selector.ConvertorSelectorScreen
import com.example.omnicalc.ui.screens.function_selector.FunctionSelectorScreen
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.omnicalc.MainActivity
import com.example.omnicalc.ui.screens.settings.ColorPickerDialog
import com.example.omnicalc.ui.screens.settings.SettingsDrawer
import com.example.omnicalc.utils.vw
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.omnicalc.ui.screens.settings.SettingsViewModel
import com.example.omnicalc.ui.screens.settings.dataStore
import kotlinx.coroutines.flow.first


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    NavigationDrawer(drawerState = drawerState, scope = scope, navController = navController) {
        Scaffold(
            topBar = {
                TopAppBar(navController = navController,
                    onSettingsClick = {
                    scope.launch { drawerState.open() }
                    })
            },
            content = { paddingValues ->
                AppNavHost(navController, paddingValues)
            }
        )
    }
}


@Composable
private fun NavigationDrawer(
    drawerState: DrawerState,
    scope: CoroutineScope,
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
                        .width(80.vw()),
                ) {
                    SettingsDrawer(onClose = { scope.launch { drawerState.close() } }, navController)
                }
            },
            content = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    content()
                }
            }
        )
    }
}


@Composable
private fun AppNavHost(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = Screen.Calc.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(Screen.Calc.route) { CalcScreen() }
        composable(Screen.FunctionSelector.route) { FunctionSelectorScreen()}
        composable(Screen.ConvertorSelector.route) { ConvertorSelectorScreen(navController) }
        composable(
            route = Screen.Convertor.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            ConvertorScreen(backStackEntry.arguments?.getString("id") ?: "unknown")
        }
        dialog(Screen.ColorPickerDialog.route) {
            ColorPickerDialog(navController)
        }
    }
}
