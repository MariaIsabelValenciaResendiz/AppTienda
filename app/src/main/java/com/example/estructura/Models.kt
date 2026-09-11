package com.example.historicocarritos

// Modelos para la API /carts
data class CartProductResponse(
    val productId: Int,
    val quantity: Int
)

data class CartResponse(
    val id: Int,
    val userId: Int,
    val date: String,
    val products: List<CartProductResponse>
)

// Modelo para la API /products
data class ProductResponse(
    val id: Int,
    val title: String,
    val price: Double
)

// Modelos procesados para la Vista (UI)
data class CartProductUI(
    val id: Int,
    val title: String,
    val quantity: Int
)

data class CartUI(
    val id: Int,
    val userId: Int,
    val date: String,
    val items: List<CartProductUI>,
    val isExpanded: Boolean = false
)