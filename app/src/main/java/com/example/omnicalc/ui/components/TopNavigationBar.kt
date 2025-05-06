package com.example.omnicalc.ui.components

import android.app.Activity
import androidx.annotation.DrawableRes
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.omnicalc.R
import com.example.omnicalc.ui.navigation.Screen



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(navController: NavController, onSettingsClick: () -> Unit) {
    val activity = LocalContext.current as? Activity
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        title = { Text("") },
        navigationIcon = {
            IconButton(
                onClick = {
                    
                }
            ) {
                Icon(painter = painterResource(id = R.drawable.overlay), contentDescription = "Overlay", tint = MaterialTheme.colorScheme.tertiary)
            }
        },
        actions = {
            TopBarIconButton(
                navController = navController,
                destination = Screen.Calc.route,
                iconRes = R.drawable.calc,
                description = "Calc",
                currentRoute = currentRoute
            )
            TopBarIconButton(
                navController = navController,
                destination = Screen.FunctionSelector.route,
                iconRes = R.drawable.function,
                description = "Function Selector",
                currentRoute = currentRoute
            )
            TopBarIconButton(
                navController = navController,
                destination = Screen.ConvertorSelector.route,
                iconRes = R.drawable.convert,
                description = "Convertor Selector",
                currentRoute = currentRoute,
                matchPartial = true
            )
            IconButton(onClick = { onSettingsClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.settings),
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    )
}

@Composable
private fun TopBarIconButton(
    navController: NavController,
    destination: String,
    @DrawableRes iconRes: Int,
    description: String,
    currentRoute: String,
    matchPartial: Boolean = false
) {
    val isSelected = if (matchPartial) {
        currentRoute.contains("convertor")
    } else {
        currentRoute == destination
    }

    IconButton(onClick = { navController.navigate(destination) }) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = description,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
        )
    }
}
