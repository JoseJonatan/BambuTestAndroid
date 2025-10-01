package com.f8fit.bambutestandroid.data.repository

import com.f8fit.bambutestandroid.data.dao.CartDao
import com.f8fit.bambutestandroid.data.dto.cartDto.CartItemEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val cartDao: CartDao
) {

    fun getCartItems(): Flow<List<CartItemEntity>> = cartDao.getAllCartItems()

    suspend fun addToCart(
        productId: Int,
        name: String,
        price: Double,
        quantity: Int,
        imageUrl: String
    ) {
        val existing = cartDao.getItemById(productId)
        if (existing != null) {
            val updated = existing.copy(quantity = existing.quantity + quantity)
            cartDao.update(updated)
        } else {
            cartDao.insert(
                CartItemEntity(
                    productId = productId,
                    name = name,
                    price = price,
                    quantity = quantity,
                    imageUrl = imageUrl   // ✅ aquí agregamos la imagen
                )
            )
        }
    }


    suspend fun updateQuantity(item: CartItemEntity, newQuantity: Int) {
        if (newQuantity > 0) {
            cartDao.update(item.copy(quantity = newQuantity))
        } else {
            cartDao.delete(item)
        }
    }

    suspend fun removeFromCart(item: CartItemEntity) {
        cartDao.delete(item)
    }

    suspend fun updateItem(item: CartItemEntity) = cartDao.updateItem(item)

    suspend fun deleteItem(item: CartItemEntity) = cartDao.deleteItem(item)

    suspend fun clearCart() = cartDao.clearCart()
}
