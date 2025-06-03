package com.project.buyzz.models

import com.google.gson.annotations.SerializedName

data class Cart(
    val id: Int,
    @SerializedName("user_id")val userId: Int
)