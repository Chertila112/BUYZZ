package com.project.buyzz.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.buyzz.models.Orders
import com.project.buyzz.viewModels.OrderState
import com.project.buyzz.viewModels.OrderViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    orderViewModel: OrderViewModel = viewModel()
) {
    val orderState by orderViewModel.orderState.collectAsState()

    LaunchedEffect(Unit) {
        orderViewModel.getUserOrders()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Личный кабинет") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFCE4775),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        modifier = Modifier.background(Color(0xFFFFE0E6))
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (orderState) {
                is OrderState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFFE91E63)
                    )
                }
                is OrderState.Error -> {
                    Text(
                        text = (orderState as OrderState.Error).message,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is OrderState.Success -> {
                    val orders = (orderState as OrderState.Success).orders
                    if (orders.isEmpty()) {
                        Text(
                            "У вас пока нет заказов", 
                            modifier = Modifier.align(Alignment.Center),
                            color = Color(0xFFE91E63)
                        )
                    } else {
                        OrdersList(orders)
                    }
                }
                else -> { }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrdersList(orders: List<Orders>) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(orders) { order ->
            OrderItem(order)
            HorizontalDivider(color = Color(0xFFE0E0E0))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderItem(order: Orders) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F4))
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                text = "Заказ №${order.id}", 
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFE91E63)
            )
            Text(
                text = "Адрес доставки: ${order.deliveryAddress}", 
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Статус: ${order.status}", 
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Дата: ${formatIsoDateString(order.createdAt)}", 
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatIsoDateString(isoDate: String): String {
    return try {
        val instant = Instant.parse(isoDate)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
            .withZone(ZoneId.systemDefault())
        formatter.format(instant)
    } catch (e: Exception) {
        isoDate
    }
}
