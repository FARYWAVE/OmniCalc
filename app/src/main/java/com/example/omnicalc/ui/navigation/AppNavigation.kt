package com.example.omnicalc.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.omnicalc.ui.components.TopAppBar
import com.example.omnicalc.ui.screens.calc.CalcScreen
import com.example.omnicalc.ui.screens.convertor_selector.ConvertorSelectorScreen
import com.example.omnicalc.ui.screens.function_selector.FunctionSelectorScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        topBar = { TopAppBar(navController) },
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
                    ConvertorSelectorScreen()
                }
            }
        }
    )
}