package com.project.buyzz.models

import com.google.gson.annotations.SerializedName

data class OrderStatusHistory(
    val id:Int,
    @SerializedName("order_id")val  orderId: Int,
    val status: String
)