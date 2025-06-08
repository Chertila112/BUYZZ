package com.project.buyzz.viewModels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.core.content.edit

sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data class Success(val token: String, val userName: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(login: String, password: String) = authRequest(
        endpoint = "login",
        body = mapOf("login" to login, "password" to password)
    )

    fun register(name: String, login: String, password: String) = authRequest(
        endpoint = "register",
        body = mapOf("name" to name, "login" to login, "password" to password)
    )

    private fun authRequest(endpoint: String, body: Map<String, String>) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val response = when (endpoint) {
                    "login" -> RetrofitClient.apiService.login(body)
                    "register" -> RetrofitClient.apiService.register(body)
                    else -> return@launch
                }

                if (response.isSuccessful && response.body() != null) {
                    val res = response.body()!!
                    val token = res["token"] as? String ?: ""
                    val user = res["user"] as? Map<*, *>
                    val name = user?.get("name") as? String ?: ""
                    val userId = (user?.get("id") as? Double)?.toInt() ?: -1

                    val shared = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                    shared.edit() {
                        putString("token", token)
                            .putInt("user_id", userId)
                    }
                    RetrofitClient.setToken(token)

                    _authState.value = AuthState.Success(token, name)
                } else {
                    _authState.value = AuthState.Error("Ошибка авторизации или регистрации")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Ошибка: ${e.localizedMessage}")
            }
        }
    }

    fun reset() {
        _authState.value = AuthState.Idle
    }
}