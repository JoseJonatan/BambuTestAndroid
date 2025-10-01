package com.f8fit.bambutestandroid.data.dto.cartDto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val name: String,
    val price: Double,
    val quantity: Int,
    val imageUrl: String
)
