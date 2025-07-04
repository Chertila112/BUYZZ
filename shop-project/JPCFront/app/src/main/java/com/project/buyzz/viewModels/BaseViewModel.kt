package com.project.buyzz.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<T> : ViewModel() {
    protected val _state = MutableStateFlow<State<T>>(State.Loading)
    val state: StateFlow<State<T>> get() = _state

    sealed class State<out T> {
        data object Loading : State<Nothing>()
        data class Success<out T>(val data: T) : State<T>()
        data class Error(val message: String) : State<Nothing>()
    }

}