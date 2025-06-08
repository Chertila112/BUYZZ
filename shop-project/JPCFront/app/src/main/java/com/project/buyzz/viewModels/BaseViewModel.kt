import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<T> : ViewModel() {
    protected val _state = MutableStateFlow<State<T>>(State.Loading)
    val state: StateFlow<State<T>> get() = _state

    sealed class State<out T> {
        object Loading : State<Nothing>()
        data class Success<out T>(val data: T) : State<T>()
        data class Error(val message: String) : State<Nothing>()
    }

}