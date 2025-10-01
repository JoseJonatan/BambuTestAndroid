package com.f8fit.bambutestandroid.presentation.productsModule.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.f8fit.bambutestandroid.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val uiState: StateFlow<ProductUiState> = _uiState

    fun loadProduct(id: Int) {
        viewModelScope.launch {
            _uiState.value = ProductUiState.Loading
            try {
                val product = repository.fetchProductById(id)
                _uiState.value = ProductUiState.Success(listOf(product))
            } catch (e: Exception) {
                _uiState.value = ProductUiState.Error(e.localizedMessage ?: "Error al cargar producto")
            }
        }
    }
}
