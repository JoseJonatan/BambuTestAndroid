package com.f8fit.bambutestandroid.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.f8fit.bambutestandroid.data.dto.productDto.Product
import com.f8fit.bambutestandroid.data.dto.productDto.ProductsResponse
import com.f8fit.bambutestandroid.data.remote.ProductApiService
import com.f8fit.bambutestandroid.utils.ProductPagingSource
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val api: ProductApiService
) {
    suspend fun fetchProducts(): ProductsResponse = api.getProducts()
    suspend fun fetchProductById(id: Int): Product = api.getProductById(id)

    suspend fun getProducts(): List<Product> {
        return api.getProducts().products
    }

    suspend fun searchProducts(query: String): List<Product> {
        return api.searchProducts(query).products
    }

    suspend fun getProductById(id: Int): Product {
        return api.getProductById(id)
    }

    fun getProductsPager(): Pager<Int, Product> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { ProductPagingSource(api) }
        )
    }
}