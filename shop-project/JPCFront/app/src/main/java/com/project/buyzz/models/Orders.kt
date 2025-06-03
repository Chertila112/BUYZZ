package com.project.buyzz.models

import com.google.gson.annotations.SerializedName
import java.security.Timestamp

data class Orders(
    val id: Int,
    @SerializedName("user_id")val userId: Int,
    @SerializedName("delivery_address")val deliveryAddress:String,
    val status: String,
    @SerializedName("created_at")val createdAt: Timestamp


)