package com.project.buyzz.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.project.buyzz.models.Products
import com.project.buyzz.viewModels.HomeViewModel
import com.project.buyzz.viewModels.ProductState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.buyzz.R // убедись, что есть R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onProductClick: (Products) -> Unit,
    onCartClick: () -> Unit,
    onProfileClick: () -> Unit  // добавлен параметр
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Buyzz") },
                navigationIcon = {
                    IconButton(onClick = onCartClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cart),
                            contentDescription = "Корзина",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_profile),
                            contentDescription = "Профиль",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFCE4775),
                    titleContentColor = Color.White
                )
            )
        },
        modifier = Modifier.background(Color(0xFFFFE0E6))
    ) { padding ->
        when (state) {
            is ProductState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFC0446F))
                }
            }

            is ProductState.Error -> {
                Text(
                    text = (state as ProductState.Error).message,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }

            is ProductState.Success -> {
                val products = (state as ProductState.Success).products
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(padding)
                ) {
                    items(products) { product ->
                        ProductCard(product, onClick = { onProductClick(product) })
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Products, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F4)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(Modifier.padding(16.dp)) {
            // Получаем ресурс изображения по ID продукта
            val imageResId = remember(product.id) {
                val resourceName = "product_${product.id}" // Формируем имя ресурса
                val resId = getResourceId(resourceName) // Получаем ID ресурса
                if (resId != 0) resId else R.drawable.placeholder_image // Fallback
            }

            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "Изображение товара",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFFE91E63)
                )
                Text(
                    text = "₽${product.price}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFFD81B60)
                )
            }
        }
    }
}
