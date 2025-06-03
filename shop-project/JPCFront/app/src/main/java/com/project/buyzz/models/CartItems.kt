package com.project.buyzz.models

import com.google.gson.annotations.SerializedName

data class CartItems(
    val id:Int,
    @SerializedName("cart_id")val cartId: Int,
    @SerializedName("product_id")val productId: Int,
    val quantity: Int
)