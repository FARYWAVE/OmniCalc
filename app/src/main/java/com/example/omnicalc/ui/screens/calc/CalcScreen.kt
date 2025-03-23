package com.example.omnicalc.ui.screens.calc


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.omnicalc.ui.components.TopAppBar
import com.example.omnicalc.ui.theme.*
import com.example.omnicalc.utils.SquareButton
import com.example.omnicalc.ui.components.*

@Composable
fun CalcScreen() {
    val viewModel: CalcViewModel = viewModel()
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (display, keyboard) = createRefs()
        val guideline = createGuidelineFromTop(1f)
        ConstraintLayout(
            modifier = Modifier.fillMaxSize().constrainAs(display) {
                top.linkTo(parent.top)
                bottom.linkTo(guideline)
            }
        ) {

        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(keyboard) {
                    top.linkTo(guideline)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            CalcKeyboard(viewModel)
        }
    }
}

@Preview
@Composable
fun ScreenPreview() {
    CalcScreen()
}