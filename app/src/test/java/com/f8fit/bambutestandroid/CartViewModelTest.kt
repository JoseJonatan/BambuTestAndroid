package com.f8fit.bambutestandroid

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.f8fit.bambutestandroid.data.dto.cartDto.CartItemEntity
import com.f8fit.bambutestandroid.data.repository.CartRepository
import com.f8fit.bambutestandroid.presentation.cartModule.viewModel.CartViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class CartViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // LiveData / StateFlow sincrónico

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: CartRepository
    private lateinit var viewModel: CartViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        viewModel = CartViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addToCart llama a repository addToCart`() = runTest {
        val productId = 1
        val name = "Producto"
        val price = 10.0
        val quantity = 1
        val imageUrl = "url"

        coEvery { repository.addToCart(productId, name, price, quantity, imageUrl) } just Runs

        viewModel.addToCart(productId, name, price, quantity, imageUrl)

        coVerify { repository.addToCart(productId, name, price, quantity, imageUrl) }
    }

    @Test
    fun `updateQuantity llama a repository updateQuantity`() = runTest {
        val item = CartItemEntity(
            productId = 1,
            name = "Producto",
            price = 10.0,
            quantity = 1,
            imageUrl = "url"
        )
        val newQuantity = 3

        coEvery { repository.updateQuantity(item, newQuantity) } just Runs

        viewModel.updateQuantity(item, newQuantity)

        coVerify { repository.updateQuantity(item, newQuantity) }
    }

    @Test
    fun `removeItem llama a repository removeFromCart`() = runTest {
        val item = CartItemEntity(
            productId = 1,
            name = "Producto",
            price = 10.0,
            quantity = 1,
            imageUrl = "url"
        )

        coEvery { repository.removeFromCart(item) } just Runs

        viewModel.removeItem(item)

        coVerify { repository.removeFromCart(item) }
    }

    @Test
    fun `getTotal devuelve la suma correcta`() = runTest {
        val items = listOf(
            CartItemEntity(
                productId = 1,
                name = "Producto",
                price = 5.0,
                quantity = 2,
                imageUrl = "url"
            ),
            CartItemEntity(
                productId = 2,
                name = "Producto",
                price = 10.0,
                quantity = 1,
                imageUrl = "url"
            )
        )
        every { repository.getCartItems() } returns flowOf(items)

        val total = viewModel.getTotal()

        assertEquals(20.0, total) // 5*2 + 10*1
    }
}