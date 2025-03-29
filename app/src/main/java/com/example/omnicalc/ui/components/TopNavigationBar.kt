package com.example.omnicalc.ui.components

import androidx.compose.ui.res.painterResource
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.omnicalc.R
import com.example.omnicalc.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(navController: NavController, onSettingsClick: () -> Unit) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        title = { Text("") },
        navigationIcon = {
            IconButton(onClick = {
            }) {
                Icon(painter = painterResource(id = R.drawable.overlay), contentDescription = "Overlay", tint = MaterialTheme.colorScheme.tertiary)
            }
        },
        actions = {
            IconButton(onClick = {
                navController.navigate(Screen.Calc.route)

            }) {
                Icon(painter = painterResource(id = R.drawable.calc), contentDescription = "Calc",
                    tint = if (currentRoute == Screen.Calc.route) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary)
            }
            IconButton(onClick = {
                navController.navigate(Screen.FunctionSelector.route)
            }) {
                Icon(painter = painterResource(id = R.drawable.function), contentDescription = "Function Selector",
                    tint = if (currentRoute == Screen.FunctionSelector.route) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary)
            }
            IconButton(onClick = {
                navController.navigate(Screen.ConvertorSelector.route)
            }) {
                Icon(painter = painterResource(id = R.drawable.convert), contentDescription = "Convertor Selector",
                    tint = if ((currentRoute == Screen.ConvertorSelector.route) || (currentRoute.contains("convertor"))) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary)
            }
            IconButton(onClick = {
                onSettingsClick()
            }) {
                Icon(painter = painterResource(id = R.drawable.settings), contentDescription = "Settings", tint = MaterialTheme.colorScheme.tertiary)
            }
        }
    )
}
