package com.project.buyzz.view

import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

@Composable
fun BuyzzTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFFE91E63),
            background = Color(0xFFFFE0E6),
            onPrimary = Color.White,
            onBackground = Color.Black
        ),
        typography = Typography(),
        content = content
    )
}