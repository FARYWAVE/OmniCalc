package com.example.omnicalc.ui.navigation

import androidx.compose.foundation.layout.padding
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
import com.example.omnicalc.ui.screens.settings.SettingsDrawer
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    SettingsDrawer(onClose = { scope.launch { drawerState.close() } })
                }
            },
            content = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Scaffold(
                        topBar = {
                            TopAppBar(navController, onSettingsClick = {
                                scope.launch { drawerState.open() }
                            })
                        },
                        content = { paddingValues ->
                            NavHost(
                                navController = navController,
                                startDestination = Screen.Calc.route,
                                modifier = Modifier.padding(paddingValues)
                            ) {
                                composable(Screen.Calc.route) {
                                    CalcScreen()
                                }
                                composable(Screen.FunctionSelector.route) {
                                    FunctionSelectorScreen()
                                }
                                composable(Screen.ConvertorSelector.route) {
                                    ConvertorSelectorScreen(navController)
                                }
                                composable(
                                    route = Screen.Convertor.route,
                                    arguments = listOf(navArgument("id") {
                                        type = NavType.StringType
                                    })
                                ) { backStackEntry ->
                                    val id = backStackEntry.arguments?.getString("id") ?: "unknown"
                                    ConvertorScreen(id)
                                }
                            }
                        }
                    )
                }
            }
        )
    }

}
