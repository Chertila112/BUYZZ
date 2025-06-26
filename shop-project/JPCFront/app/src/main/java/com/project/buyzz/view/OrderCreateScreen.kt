package com.project.buyzz.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.buyzz.viewModels.OrderState
import com.project.buyzz.viewModels.OrderViewModel
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderCreateScreen(
    navController: NavController,
    orderViewModel: OrderViewModel = viewModel(),
    onOrderCreated: () -> Unit = {}
) {
    var address by remember { mutableStateOf(TextFieldValue("")) }
    val orderState by orderViewModel.orderState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Оформление заказа") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Адрес доставки") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFFFF1F4),
                    unfocusedContainerColor = Color(0xFFFFF1F4),
                    focusedLabelColor = Color(0xFFE91E63),
                    cursorColor = Color(0xFFE91E63)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (address.text.isNotBlank()) {
                        orderViewModel.createOrder(address.text)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
                enabled = address.text.isNotBlank()
            ) {
                Text("Оформить заказ", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (orderState) {
                is OrderState.Loading -> CircularProgressIndicator(color = Color(0xFFE91E63))
                is OrderState.Error -> Text(
                    text = (orderState as OrderState.Error).message,
                    color = Color.Red
                )
                is OrderState.Created -> {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E8)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Заказ успешно оформлен!",
                            color = Color(0xFF4CAF50),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    LaunchedEffect(orderState) {
                        onOrderCreated()
                    }
                }
                else -> {}
            }
        }
    }
}
