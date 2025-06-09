package com.project.buyzz.models

import com.google.gson.annotations.SerializedName

data class CartItems(
    @SerializedName("id") val id: Int,
    @SerializedName("cart_id") val cartId: Int,
    @SerializedName("product_id") val productId: Int,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("name") val productName: String,
    @SerializedName("price") val productPrice: Double,
    @SerializedName("description") val productDescription: String
)