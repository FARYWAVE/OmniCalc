package com.example.omnicalc.ui.components

import androidx.appcompat.app.ActionBar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.omnicalc.R
import com.example.omnicalc.ui.navigation.Screen
import com.example.omnicalc.ui.screens.function.FunctionViewModel
import com.example.omnicalc.ui.screens.function_selector.FunctionSelectorViewModel
import com.example.omnicalc.utils.vw


interface ActionBarHandler {
    val displayText: MutableState<String>
    enum class Key(val icon: Int) {
        MOVE(R.drawable.move),
        COPY(R.drawable.copy),
        SAVE(R.drawable.check),
        ADD(R.drawable.add),
        DELETE(R.drawable.cross);


        companion object {
            fun keyByName(name:String) = Key.entries.first { it.name == name }
        }
    }

    fun onKeyPressed(key: Key, confirmed: Boolean = true)
}

@Composable
fun ActionBar(
    viewModel: ActionBarHandler,
    keys: Array<ActionBarHandler.Key> = ActionBarHandler.Key.entries.toTypedArray(),
    root: Boolean,
    navController: NavController
) {
    val focusManager = LocalFocusManager.current
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.tertiary)
            .height(16.666f.vw())
            .clickable(
                onClick = { focusManager.clearFocus() }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val underlineColor = if (viewModel.displayText.value.isEmpty()) MaterialTheme.colorScheme.background
        else Color.Transparent
        if (root) Spacer(Modifier.weight(1f))
        else TextField(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 3.dp, vertical = 2.dp),
            value = viewModel.displayText.value,
            onValueChange = { viewModel.displayText.value = it },
            placeholder = { Text(viewModel.displayText.value) },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.background,
                unfocusedTextColor = MaterialTheme.colorScheme.background,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedLabelColor = MaterialTheme.colorScheme.background,
                unfocusedLabelColor = MaterialTheme.colorScheme.background,
                focusedIndicatorColor = underlineColor,
                unfocusedIndicatorColor = underlineColor
            )
        )
        keys.forEach {
            Key(it, viewModel, navController)
        }
        Spacer(Modifier.width(5.dp))
    }


}


@Composable
private fun Key(key: ActionBarHandler.Key, viewModel: ActionBarHandler, navController: NavController) {
    val focusManager = LocalFocusManager.current
    IconButton(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(0.dp),
        onClick = {
            if (viewModel is FunctionViewModel) {
                when (key) {
                    ActionBarHandler.Key.DELETE, ActionBarHandler.Key.COPY -> {
                        navController.navigate(Screen.ConfirmActionDialog.withData("0", key, viewModel.displayText.value))
                    }
                    else -> {viewModel.onKeyPressed(key)}
                }
            } else if (viewModel is FunctionSelectorViewModel) {
                when (key) {
                    ActionBarHandler.Key.ADD -> navController.navigate(Screen.NewItemDialog.route)
                    ActionBarHandler.Key.DELETE, ActionBarHandler.Key.COPY -> {
                        navController.navigate(Screen.ConfirmActionDialog.withData("1", key, viewModel.displayText.value))
                    }
                    else -> {viewModel.onKeyPressed(key)}
                }
            }
        }
    ) {
        Icon(
            painter = painterResource(key.icon),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.background
        )
    }
}


@Preview
@Composable
fun Preview() {
    //ActionBar("Example", vm)
}