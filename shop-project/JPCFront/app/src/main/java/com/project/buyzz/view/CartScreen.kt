package com.project.buyzz.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.project.buyzz.R
import com.project.buyzz.models.CartItems
import com.project.buyzz.viewModels.CartState
import com.project.buyzz.viewModels.CartViewModel
import com.project.buyzz.viewModels.OrderState
import com.project.buyzz.viewModels.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onCheckoutClick: () -> Unit,
    onProfileClick: () -> Unit,
    navController: NavController,
    cartViewModel: CartViewModel = viewModel(),
    orderViewModel: OrderViewModel = viewModel()
) {
    val cartState by cartViewModel.cartState.collectAsState()
    val orderState by orderViewModel.orderState.collectAsState()

    LaunchedEffect(orderState) {
        if (orderState is OrderState.Success) {
            onProfileClick()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Корзина") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFCE4775))
            )
        },
        bottomBar = {
            if (cartState is CartState.Success) {
                val total = (cartState as CartState.Success).items.sumOf { it.productPrice * it.quantity }
                Surface(
                    tonalElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Итого: $total руб.", style = MaterialTheme.typography.titleMedium)
                        Button(onClick = onCheckoutClick) {
                            Text("Оформить заказ")
                        }
                    }
                }
            }
        }
    ) { padding ->
        when (cartState) {
            is CartState.Loading -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFC0446F))
            }
            is CartState.Success -> {
                val items = (cartState as CartState.Success).items
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items) { item ->
                        CartItemCard(
                            item = item,
                            onIncrease = { cartViewModel.updateItemQuantity(item.id, +1) },
                            onDecrease = { cartViewModel.updateItemQuantity(item.id, -1) },
                            onRemove = { cartViewModel.removeItem(item.id) }
                        )
                    }
                }
            }
            is CartState.Error -> Text(
                text = (cartState as CartState.Error).message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun OrderDialog(total: Double, onCancel: () -> Unit, onConfirm: (String) -> Unit) {
    var address by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Оформление заказа") },
        text = {
            Column {
                Text("Сумма заказа: $total руб.")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    placeholder = { Text("Введите адрес доставки") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(enabled = address.isNotBlank(), onClick = { onConfirm(address) }) {
                Text("Подтвердить")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Отмена")
            }
        }
    )
}

@Composable
fun CartItemCard(
    item: CartItems,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    val imageResId = remember(item.productId) {
        val resourceName = "product_${item.productId}"
        val resId = getResourceId(resourceName)
        if (resId != 0) resId else R.drawable.placeholder_image
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F4)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "Изображение товара",
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(item.productName, style = MaterialTheme.typography.titleMedium)
                Text("Цена: ${item.productPrice} руб.", style = MaterialTheme.typography.bodyMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onDecrease) {
                        Text("-")
                    }
                    Text(item.quantity.toString())
                    IconButton(onClick = onIncrease) {
                        Text("+")
                    }
                }
            }

            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Удалить")
            }
        }
    }
}

fun getResourceId(resourceName: String): Int {
    return try {
        val resIdField = R.drawable::class.java.getDeclaredField(resourceName)
        resIdField.getInt(null)
    } catch (e: Exception) {
        0
    }
}
