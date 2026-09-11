package com.example.historicocarritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class CartUiState {
    object Loading : CartUiState()
    data class Success(val carts: List<CartUI>) : CartUiState()
    data class Error(val message: String) : CartUiState()
}

class CartHistoryViewModel : ViewModel() {

    private val apiService = ApiService.create()

    private val _uiState = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = CartUiState.Loading
            try {
                // Peticiones concurrentes
                val cartsDeferred = async { apiService.getCarts() }
                val productsDeferred = async { apiService.getProducts() }

                val cartsRaw = cartsDeferred.await()
                val productsRaw = productsDeferred.await()

                // Mapeo O(1) de productos por ID
                val productsMap = productsRaw.associateBy { it.id }

                // Cruce de datos (productId -> Título del Producto)
                val cartsUiList = cartsRaw.map { cart ->
                    CartUI(
                        id = cart.id,
                        userId = cart.userId,
                        date = if (cart.date.length >= 10) cart.date.take(10) else cart.date,
                        items = cart.products.map { p ->
                            CartProductUI(
                                id = p.productId,
                                title = productsMap[p.productId]?.title ?: "Producto sin nombre",
                                quantity = p.quantity
                            )
                        }
                    )
                }

                _uiState.value = CartUiState.Success(cartsUiList)
            } catch (e: Exception) {
                _uiState.value = CartUiState.Error(e.localizedMessage ?: "Error de conexión")
            }
        }
    }

    fun toggleExpand(cartId: Int) {
        val currentState = _uiState.value
        if (currentState is CartUiState.Success) {
            val updatedList = currentState.carts.map { cart ->
                if (cart.id == cartId) cart.copy(isExpanded = !cart.isExpanded)
                else cart
            }
            _uiState.value = CartUiState.Success(updatedList)
        }
    }
}