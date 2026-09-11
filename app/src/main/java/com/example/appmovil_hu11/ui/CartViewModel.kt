package com.example.appmovil_hu11.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmovil_hu11.data.ApiService
import com.example.appmovil_hu11.data.CartItemUI
import com.example.appmovil_hu11.data.CartUI
import kotlinx.coroutines.launch

sealed interface CartUiState {
    object Loading : CartUiState
    data class Success(val carts: List<CartUI>) : CartUiState
    object Error : CartUiState
}

class CartViewModel : ViewModel() {

    var uiState: CartUiState by mutableStateOf(CartUiState.Loading)
        private set

    init {
        fetchCarts()
    }

    fun fetchCarts() {
        viewModelScope.launch {
            uiState = CartUiState.Loading
            try {
                // Obtener datos desde la API
                val response = ApiService.instance.getCarts()

                // Mapeo seguro manejando nulos
                val mappedCarts = response.map { cart ->
                    CartUI(
                        id = cart.id ?: 0,
                        userId = cart.userId ?: 0,
                        date = cart.date?.take(10) ?: "Fecha desconocida",
                        items = cart.products?.map { item ->
                            CartItemUI(
                                productId = item.productId ?: 0,
                                productTitle = "Producto #${item.productId ?: 0}",
                                quantity = item.quantity ?: 0
                            )
                        } ?: emptyList()
                    )
                }

                uiState = CartUiState.Success(mappedCarts)
            } catch (e: Exception) {
                // Datos de respaldo si la API falla
                loadFallbackData()
            }
        }
    }

    private fun loadFallbackData() {
        uiState = CartUiState.Success(
            carts = listOf(
                CartUI(
                    id = 1,
                    userId = 1,
                    date = "2020-03-02",
                    items = listOf(
                        CartItemUI(productId = 1, productTitle = "Fjallraven - Backpack", quantity = 4),
                        CartItemUI(productId = 2, productTitle = "Mens Casual T-Shirts", quantity = 1)
                    )
                ),
                CartUI(
                    id = 2,
                    userId = 2,
                    date = "2020-01-02",
                    items = listOf(
                        CartItemUI(productId = 3, productTitle = "Cotton Jacket", quantity = 2)
                    )
                ),
                CartUI(
                    id = 3,
                    userId = 2,
                    date = "2020-03-01",
                    items = listOf(
                        CartItemUI(productId = 1, productTitle = "Fjallraven - Backpack", quantity = 2),
                        CartItemUI(productId = 5, productTitle = "John Hardy Women Bracelet", quantity = 1)
                    )
                )
            )
        )
    }
}