package com.project.buyzz.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.buyzz.models.CartItems
import com.project.buyzz.models.Products
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductDetailsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<ProductDetailsState>(ProductDetailsState.Loading)
    val uiState: StateFlow<ProductDetailsState> = _uiState

    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getProducts()
                val product = response.body()?.find { it.id == productId }
                if (product != null) {
                    _uiState.value = ProductDetailsState.Success(product)
                } else {
                    _uiState.value = ProductDetailsState.Error("Товар не найден")
                }
            } catch (e: Exception) {
                _uiState.value = ProductDetailsState.Error("Ошибка: ${e.message}")
            }
        }
    }

    fun addToCart(productId: Int) {
        viewModelScope.launch {
            try {
                val cartItem = CartItems(id = 0, cartId = 1, productId = productId, quantity = 1)
                RetrofitClient.apiService.addToCart(cartItem)
                (_uiState.value as? ProductDetailsState.Success)?.let {
                    _uiState.value = it.copy(addedToCart = true)
                }
            } catch (e: Exception) {
            }
        }
    }
}

sealed class ProductDetailsState {
    object Loading : ProductDetailsState()
    data class Error(val message: String) : ProductDetailsState()
    data class Success(val product: Products, val addedToCart: Boolean = false) : ProductDetailsState()
}