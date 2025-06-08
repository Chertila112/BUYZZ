package com.project.buyzz.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.buyzz.models.Products
import com.project.buyzz.ui.theme.MyApplicationTheme
import com.project.buyzz.viewModels.HomeViewModel
import com.project.buyzz.viewModels.ProductState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buyzz — Продукты") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFE91E63),
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
                    CircularProgressIndicator(color = Color(0xFFE91E63))
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
                        ProductCard(product)
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Products) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F4)),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(product.name, style = MaterialTheme.typography.titleMedium, color = Color(0xFFE91E63))
            Spacer(modifier = Modifier.height(4.dp))
            Text(product.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "₽ ${product.price}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFD81B60)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    MyApplicationTheme {
        HomeScreen()
    }
}