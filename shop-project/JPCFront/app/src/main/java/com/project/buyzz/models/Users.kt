package com.project.buyzz.models

import com.google.gson.annotations.SerializedName

data class Users(
    val id: Int,
    val name: String,
    val login: String,
    @SerializedName("password_hash")val passwordHash: String
)
