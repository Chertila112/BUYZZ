package com.project.buyzz.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.buyzz.ui.theme.MyApplicationTheme
import com.project.buyzz.viewModels.AuthState
import com.project.buyzz.viewModels.AuthViewModel

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = viewModel(),
    onAuthSuccess: (String) -> Unit
) {
    var isLogin by remember { mutableStateOf(true) }
    var name by remember { mutableStateOf("") }
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val state by viewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFE0E6))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isLogin) "Вход" else "Регистрация",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFFE91E63)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!isLogin) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Имя") },
                modifier = Modifier.fillMaxWidth(),
                colors = pinkTextFieldColors()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth(),
            colors = pinkTextFieldColors()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            colors = pinkTextFieldColors()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isLogin) viewModel.login(login, password)
                else viewModel.register(name, login, password)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Продолжить", color = Color.White)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = {
            isLogin = !isLogin
            viewModel.reset()
        }) {
            Text(
                text = if (isLogin) "Нет аккаунта? Зарегистрируйтесь" else "Уже есть аккаунт? Войти",
                color = Color(0xFFAD1457)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (state) {
            is AuthState.Loading -> CircularProgressIndicator(color = Color(0xFFE91E63))
            is AuthState.Error -> {
                Text(
                    text = (state as AuthState.Error).message,
                    color = Color.Red
                )
            }
            is AuthState.Success -> {
                LaunchedEffect(Unit) {
                    onAuthSuccess((state as AuthState.Success).token)
                    viewModel.reset()
                }
            }
            else -> {}
        }
    }
}

@Composable
fun pinkTextFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = Color(0xFFE91E63),
    focusedLabelColor = Color(0xFFE91E63),
    cursorColor = Color(0xFFE91E63)
)