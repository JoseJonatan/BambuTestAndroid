package com.f8fit.bambutestandroid.presentation.cartModule.viewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.f8fit.bambutestandroid.data.dto.cartDto.CartItemEntity
import com.f8fit.bambutestandroid.data.repository.CartRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: CartRepository
) : ViewModel() {

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    val cartItems: StateFlow<List<CartItemEntity>> =
        repository.getCartItems().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addToCart(
        productId: Int,
        name: String,
        price: Double,
        quantity: Int,
        imageUrl: String
    ) {
        viewModelScope.launch {
            repository.addToCart(productId, name, price, quantity, imageUrl)
        }
    }


    fun updateQuantity(item: CartItemEntity, newQuantity: Int) {
        viewModelScope.launch {
            repository.updateQuantity(item, newQuantity)
        }
    }

    fun removeItem(item: CartItemEntity) {
        viewModelScope.launch {
            repository.removeFromCart(item)
        }
    }

    fun getTotal(): Double {
        return cartItems.value.sumOf { it.price * it.quantity }
    }
}

