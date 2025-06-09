package com.project.buyzz.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.buyzz.viewModels.OrderState
import com.project.buyzz.viewModels.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderCreateScreen(
    orderViewModel: OrderViewModel = viewModel(),
    onOrderCreated: () -> Unit = {}
) {
    var address by remember { mutableStateOf(TextFieldValue("")) }
    val orderState by orderViewModel.orderState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Оформление заказа") })
        }
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
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (address.text.isNotBlank()) {
                        orderViewModel.createOrder(address.text)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Оформить заказ")
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (orderState) {
                is OrderState.Loading -> CircularProgressIndicator()
                is OrderState.Error -> Text(
                    text = (orderState as OrderState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
                is OrderState.Created -> {
                    Text("Заказ успешно оформлен!")
                    LaunchedEffect(orderState) {
                        onOrderCreated()
                    }
                }
                else -> {}
            }
        }
    }
}
