package com.example.omnicalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.omnicalc.ui.navigation.AppNavigation
import com.example.omnicalc.ui.theme.FarywaveTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FarywaveTheme {
                AppNavigation()
            }
        }
    }
}