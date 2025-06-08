package com.project.buyzz.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.buyzz.models.Products
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ProductState {
    data object Loading : ProductState()
    data class Success(val products: List<Products>) : ProductState()
    data class Error(val message: String) : ProductState()
}

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow<ProductState>(ProductState.Loading)
    val state: StateFlow<ProductState> = _state

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getProducts()
                if (response.isSuccessful && response.body() != null) {
                    _state.value = ProductState.Success(response.body()!!)
                } else {
                    _state.value = ProductState.Error("Не удалось загрузить продукты")
                }
            } catch (e: Exception) {
                _state.value = ProductState.Error("Ошибка: ${e.localizedMessage}")
            }
        }
    }
}
