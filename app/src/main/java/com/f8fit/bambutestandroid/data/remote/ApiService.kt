package com.f8fit.bambutestandroid.data.remote

import com.f8fit.bambutestandroid.data.dto.productDto.Product
import com.f8fit.bambutestandroid.data.dto.productDto.ProductsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int = 10,
        @Query("skip") skip: Int = 0
    ): ProductsResponse

    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") query: String,
        @Query("limit") limit: Int = 10,
        @Query("skip") skip: Int = 0
    ): ProductsResponse

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Product
}