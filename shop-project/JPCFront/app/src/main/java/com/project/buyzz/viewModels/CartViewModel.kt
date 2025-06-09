package com.project.buyzz.viewModels

import com.project.buyzz.Retrofit.RetrofitClient.apiService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.buyzz.Retrofit.RetrofitClient
import com.project.buyzz.models.CartItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class CartState {
    data object Loading : CartState()
    data class Success(val items: List<CartItems>) : CartState()
    data class Error(val message: String) : CartState()
}

class CartViewModel : ViewModel() {
    private val _cartState = MutableStateFlow<CartState>(CartState.Loading)
    val cartState: StateFlow<CartState> = _cartState

    init {
        loadCart()
    }

    fun loadCart() {
        viewModelScope.launch {
            _cartState.value = CartState.Loading
            try {
                val response = RetrofitClient.apiService.getCartItems()
                if (response.isSuccessful && response.body() != null) {
                    _cartState.value = CartState.Success(response.body()!!)
                } else {
                    _cartState.value = CartState.Error("Ошибка загрузки корзины")
                }
            } catch (e: Exception) {
                _cartState.value = CartState.Error("Ошибка: ${e.localizedMessage}")
            }
        }
    }
    fun updateItemQuantity(itemId: Int, delta: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.updateCartItemQuantity(itemId, mapOf("delta" to delta))
                if (response.isSuccessful) {
                    loadCart()
                }
            } catch (e: Exception) {
                _cartState.value = CartState.Error("Ошибка: ${e.localizedMessage}")
            }
        }
    }

    fun removeItem(itemId: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.deleteCartItem(itemId)
                if (response.isSuccessful) {
                    loadCart()
                }
            } catch (e: Exception) {
                _cartState.value = CartState.Error("Ошибка: ${e.localizedMessage}")
            }
        }
    }

}