import androidx.lifecycle.viewModelScope
import com.project.buyzz.API.ApiService
import com.project.buyzz.models.Products
import kotlinx.coroutines.launch

class ProductViewModel(private val api: ApiService) : BaseViewModel<List<Products>>() {
    fun loadProducts() {
        viewModelScope.launch {
            _state.value = State.Loading
            try {
                val response = api.getProducts()
                if (response.isSuccessful) {
                    _state.value = State.Success(response.body()!!)
                } else {
                    _state.value = State.Error("Failed to load products")
                }
            } catch (e: Exception) {
                _state.value = State.Error(e.message ?: "Network error")
            }
        }
    }
}