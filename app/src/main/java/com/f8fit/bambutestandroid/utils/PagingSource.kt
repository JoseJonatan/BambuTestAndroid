package com.f8fit.bambutestandroid.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.f8fit.bambutestandroid.data.dto.productDto.Product
import com.f8fit.bambutestandroid.data.remote.ProductApiService

class ProductPagingSource(
    private val api: ProductApiService
) : PagingSource<Int, Product>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val page = params.key ?: 0
            val limit = params.loadSize
            val response = api.getProducts(limit = limit, skip = page * limit)

            LoadResult.Page(
                data = response.products,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (response.products.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchor ->
            val anchorPage = state.closestPageToPosition(anchor)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
