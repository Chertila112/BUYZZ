package com.project.buyzz.view

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.project.buyzz.view.CartScreen
import com.project.buyzz.viewModels.HomeViewModel
import com.project.buyzz.view.AuthScreen
import com.project.buyzz.view.HomeScreen
import com.project.buyzz.view.ProductDetailsScreen
import com.project.buyzz.view.OrderCreateScreen
import com.project.buyzz.view.ProfileScreen

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "auth"
    ) {
        composable("auth") {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                onProductClick = { product -> navController.navigate("product/${product.id}") },
                onCartClick = { navController.navigate("cart") },
                onProfileClick = { navController.navigate("profile") }
            )
        }

        composable("product/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
            if (productId != null) {
                ProductDetailsScreen(productId)
            }
        }

        composable("cart") {
            CartScreen(
                navController = navController,
                onCheckoutClick = {
                    navController.navigate("order_create")
                },
                onProfileClick = {
                    navController.navigate("profile") {
                        popUpTo("cart") { inclusive = true }
                    }
                }
            )
        }


        composable("order_create") {
            OrderCreateScreen(
                orderViewModel = viewModel(),
                onOrderCreated = {
                    navController.navigate("profile") {
                        popUpTo("cart") { inclusive = true }
                    }
                }
            )
        }

        composable("profile") {
            ProfileScreen()
        }
    }
}
