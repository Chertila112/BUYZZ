package com.project.buyzz.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.buyzz.viewModels.ProductDetailsState
import com.project.buyzz.viewModels.ProductDetailsViewModel

@Composable
fun ProductDetailsScreen(
    productId: Int,
    viewModel: ProductDetailsViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    when (val s = state) {
        is ProductDetailsState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFE91E63))
            }
        }
        is ProductDetailsState.Error -> {
            Text(text = s.message, color = Color.Red)
        }
        is ProductDetailsState.Success -> {
            val product = s.product
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(180.dp)
                        .fillMaxWidth()
                        .background(Color(0xFFE1BEE7)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Здесь будет изображение", color = Color.White)
                }

                Text(product.name, style = MaterialTheme.typography.headlineSmall, color = Color(0xFFE91E63))
                Text(product.description, style = MaterialTheme.typography.bodyMedium)
                Text("₽ ${product.price}", style = MaterialTheme.typography.titleMedium)

                Button(
                    onClick = { viewModel.addToCart(product.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
                ) {
                    Text("Добавить в корзину", color = Color.White)
                }

                if (s.addedToCart) {
                    Text("Добавлено в корзину", color = Color(0xFF4CAF50))
                }
            }
        }
    }
}
