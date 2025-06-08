package com.project.buyzz.API

import com.project.buyzz.models.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    // Users
    @GET("/api/users")
    suspend fun getUsers(): Response<List<Users>>

    @POST("/auth/login")
    suspend fun login(
        @Body request: Map<String, String>
    ): Response<Map<String, Any>>

    @POST("/auth/register")
    suspend fun register(
        @Body request: Map<String, String>
    ): Response<Map<String, Any>>

    @GET("/api/users/me") // ✅ get data from JWT
    suspend fun getCurrentUser(): Response<Users>

    // Products
    @GET("/api/products")
    suspend fun getProducts(): Response<List<Products>>

    @POST("/api/products")
    suspend fun createProduct(@Body product: Products): Response<String>

    // Cart
    @GET("/api/cart")
    suspend fun getCart(): Response<Cart>

    @GET("/api/cart/items")
    suspend fun getCartItems(): Response<List<CartItems>>

    @POST("/api/cart/items")
    suspend fun addToCart(
        @Body item: Map<String, Int>
    ): Response<Unit>

    // Orders
    @GET("/api/orders")
    suspend fun getUserOrders(): Response<List<Orders>>

    @GET("/api/orders/{orderId}/items")
    suspend fun getOrderItems(
        @Path("orderId") orderId: Int
    ): Response<List<OrderItems>>

    @GET("/api/orders/{orderId}/history")
    suspend fun getOrderHistory(
        @Path("orderId") orderId: Int
    ): Response<List<OrderStatusHistory>>

    @POST("/api/orders")
    suspend fun createOrder(
        @Body order: Orders
    ): Response<Orders>
}