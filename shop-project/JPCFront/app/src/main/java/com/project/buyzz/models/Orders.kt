package com.project.buyzz.models

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class Orders(
    val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("delivery_address") val deliveryAddress: String,
    val status: String,
    @SerializedName("created_at") val createdAt: String
){
@RequiresApi(Build.VERSION_CODES.O)
fun getFormattedCreatedAt(): String {
    return try {
        val instant = java.time.Instant.parse(createdAt)
        val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(java.time.ZoneId.systemDefault())
        formatter.format(instant)
    } catch (e: Exception) {
        createdAt
    }
}
}