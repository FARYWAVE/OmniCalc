package com.example.omnicalc.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.NavHostController
import com.example.omnicalc.ui.components.dialogs.ColorPickerDialog
import com.example.omnicalc.ui.components.dialogs.ConfirmActionDialog
import com.example.omnicalc.ui.components.dialogs.NewItemDialog
import com.example.omnicalc.ui.components.dialogs.RemoteFolderListDialog
import com.example.omnicalc.ui.screens.function.FunctionScreen
import com.example.omnicalc.ui.screens.settings.SettingsDrawer
import com.example.omnicalc.utils.vw
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


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
                val focusManager = LocalFocusManager.current
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Box(Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {focusManager.clearFocus()}
                    )) {
                        content()
                    }

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
        navController.currentBackStackEntry?.savedStateHandle?.set("confirm_result", false)
        composable(Screen.Calc.route) { CalcScreen() }
        composable(Screen.FunctionSelector.route) { FunctionSelectorScreen(navController)}
        composable(Screen.ConvertorSelector.route) { ConvertorSelectorScreen(navController) }
        composable(
            route = Screen.Convertor.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            ConvertorScreen(backStackEntry.arguments?.getString("id") ?: "unknown")
        }
        composable(
            route = Screen.Function.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            FunctionScreen(backStackEntry.arguments?.getString("id") ?: "unknown", navController)
        }
        dialog(Screen.ColorPickerDialog.route) {
            ColorPickerDialog(navController)
        }
        dialog(route = Screen.NewItemDialog.route) {
            NewItemDialog(navController)
        }
        dialog(route = Screen.ConfirmActionDialog.route) { backStackEntry ->
            fun byID(id:String) : String {
                return backStackEntry.arguments?.getString(id) ?: "unknown"
            }
            ConfirmActionDialog(byID("vmID"), navController, byID("key"), byID("item"))
        }
        dialog(Screen.RemoteFolderListDialog.route) {
            RemoteFolderListDialog(navController)
        }
    }
}
