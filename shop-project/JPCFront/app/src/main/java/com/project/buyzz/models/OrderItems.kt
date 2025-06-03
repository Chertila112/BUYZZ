package com.project.buyzz.models

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class OrderItems(
    val id:Int,
    @SerializedName("order_id")val orderId: Int,
    @SerializedName("product_id")val productId: Int,
    val quantity: Int,
    val price: Double
)
