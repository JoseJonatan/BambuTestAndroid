package com.f8fit.bambutestandroid.presentation.productsModule.viewModel

import com.f8fit.bambutestandroid.data.dto.productDto.Product

sealed class ProductUiState {
    object Loading : ProductUiState()
    data class Success(val products: List<Product>) : ProductUiState()
    data class Error(val message: String) : ProductUiState()
}