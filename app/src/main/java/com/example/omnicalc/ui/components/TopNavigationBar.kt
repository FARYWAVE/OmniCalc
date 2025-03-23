package com.example.omnicalc.ui.components

import android.content.res.Resources.Theme
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.omnicalc.R
import com.example.omnicalc.ui.navigation.Screen
import com.example.omnicalc.ui.theme.FarywaveTheme
import com.example.omnicalc.ui.theme.FarywaveTypo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""
    TopAppBar(
        title = { Text("") },
        navigationIcon = {
            IconButton(onClick = {
            }) {
                Icon(painter = painterResource(id = R.drawable.overlay), contentDescription = "Overlay", tint = MaterialTheme.colorScheme.onSecondary)
            }
        },
        actions = {
            IconButton(onClick = {
                navController.navigate(Screen.Calc.route)

            }) {
                Icon(painter = painterResource(id = R.drawable.calc), contentDescription = "Calc",
                    tint = if (currentRoute == Screen.Calc.route) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondary)
            }
            IconButton(onClick = {
                navController.navigate(Screen.FunctionSelector.route)
            }) {
                Icon(painter = painterResource(id = R.drawable.function), contentDescription = "Function Selector",
                    tint = if (currentRoute == Screen.FunctionSelector.route) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondary)
            }
            IconButton(onClick = {
                navController.navigate(Screen.ConvertorSelector.route)
            }) {
                Icon(painter = painterResource(id = R.drawable.convert), contentDescription = "Convertor Selector",
                    tint = if (currentRoute == Screen.ConvertorSelector.route) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondary)
            }
            IconButton(onClick = {
            }) {
                Icon(painter = painterResource(id = R.drawable.settings), contentDescription = "Settings", tint = MaterialTheme.colorScheme.onSecondary)
            }
        }
    )
}

@Preview
@Composable
fun TopAppBarPreview() {
    TopAppBar(navController = rememberNavController())
}