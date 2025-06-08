package com.project.buyzz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.project.buyzz.view.BuyzzTheme
import com.project.buyzz.view.MainNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BuyzzTheme {
                MainNavigation()
            }
        }
    }
}
