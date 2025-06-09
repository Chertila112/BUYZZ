package com.project.buyzz.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.buyzz.Retrofit.RetrofitClient.apiService
import com.project.buyzz.models.CartItems
import com.project.buyzz.models.Orders
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class OrderState {
    object Idle : OrderState()
    object Loading : OrderState()
    data class Created(val order: Orders) : OrderState()
    data class Success(val orders: List<Orders>) : OrderState()
    data class Error(val message: String) : OrderState()
}

class OrderViewModel : ViewModel() {

    private val _orderState = MutableStateFlow<OrderState>(OrderState.Idle)
    val orderState: StateFlow<OrderState> = _orderState

    fun createOrder(deliveryAddress: String) {
        viewModelScope.launch {
            _orderState.value = OrderState.Loading
            try {
                val newOrder = mapOf("delivery_address" to deliveryAddress)
                val response = apiService.createOrder(newOrder)
                if (response.isSuccessful && response.body() != null) {
                    _orderState.value = OrderState.Created(response.body()!!)
                    clearCart()
                } else {
                    _orderState.value = OrderState.Error("Не удалось оформить заказ")
                }
            } catch (e: Exception) {
                _orderState.value = OrderState.Error("Ошибка: ${e.localizedMessage}")
            }
        }
    }

    private suspend fun clearCart() {
        try {
            val response = apiService.getCartItems()
            if (response.isSuccessful && response.body() != null) {
                val cartItems: List<CartItems> = response.body()!!
                cartItems.forEach { item ->
                    apiService.deleteCartItem(item.id)
                }
            }
        } catch (e: Exception) {
        }
    }

    fun getUserOrders() {
        viewModelScope.launch {
            _orderState.value = OrderState.Loading
            try {
                val response = apiService.getUserOrders()
                if (response.isSuccessful && response.body() != null) {
                    _orderState.value = OrderState.Success(response.body()!!)
                } else {
                    _orderState.value = OrderState.Error("Не удалось загрузить заказы")
                }
            } catch (e: Exception) {
                _orderState.value = OrderState.Error("Ошибка: ${e.localizedMessage}")
            }
        }
    }
}
