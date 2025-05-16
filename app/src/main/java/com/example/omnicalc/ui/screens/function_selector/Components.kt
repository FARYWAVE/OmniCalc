package com.example.omnicalc.ui.screens.function_selector

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.omnicalc.R
import com.example.omnicalc.ui.components.display.ExpressionContainer
import com.example.omnicalc.ui.navigation.Screen
import com.example.omnicalc.ui.screens.MainViewModel
import com.example.omnicalc.utils.Expression
import com.example.omnicalc.utils.ExpressionContainer
import com.example.omnicalc.utils.Function
import com.example.omnicalc.utils.FunctionFolder
import com.example.omnicalc.utils.VariableManager

@Composable
fun FunctionCard(
    modifier: Modifier = Modifier,
    function: Function,
    navController: NavController
) {
    Box(Modifier
        .padding(10.dp)
        .background(MaterialTheme.colorScheme.secondary)
        .clickable(onClick = {
            function.expression.container.add(Expression(com.example.omnicalc.ui.components.display.Function.CARET))
            MainViewModel.rootContainer.container.clear()
            MainViewModel.rootContainer.container.addAll(function.expression.container)
            VariableManager.variables.clear()
            MainViewModel.rootContainer.getVariables().forEach {
                VariableManager.addVar(it)
            }

            navController.navigate(Screen.Function.withId(function.id.toString()))
            function.expression.container.removeAt(function.expression.container.size - 1)
        })) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.function),
                contentDescription = "Function",
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = function.name,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.tertiary,
                textAlign = TextAlign.Start
            )
            Spacer(Modifier.weight(1f))
            var variables = "Variables: "
            function.expression.getVariables().forEach {
                variables += "${it}, "
            }
            Text(
                text = variables.removeSuffix(", "),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
fun FunctionFolderCard(
    modifier: Modifier = Modifier,
    folder: FunctionFolder,
    viewModel: FunctionSelectorViewModel
) {
    Box(Modifier
        .padding(10.dp)
        .background(MaterialTheme.colorScheme.secondary)
        .clickable(
            onClick = {
                viewModel.openFolder(folder)
            }
        )
    ) {
        Row(modifier = modifier
            .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.folder),
                contentDescription = "Folder",
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = folder.name + "          ",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.tertiary,
                textAlign = TextAlign.Start
            )
            Spacer(Modifier.weight(1f))
        }
    }
}