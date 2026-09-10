package com.example.appmovil_hu11.data

// ==========================================
// MODELOS PARA LA INTERFAZ DE USUARIO (UI)
// ==========================================

data class UserUI(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String
) {
    val initials: String
        get() = name.trim().split("\\s+".toRegex())
            .take(2)
            .mapNotNull { it.firstOrNull()?.uppercaseChar() }
            .joinToString("")
}

data class CartUI(
    val id: Int,
    val userId: Int,
    val date: String,
    val items: List<CartItemUI>
)

data class CartItemUI(
    val productId: Int,
    val productTitle: String,
    val quantity: Int
)

// ==========================================
// RESPUESTAS DE LA API (DTOs)
// ==========================================

data class UserResponse(
    val id: Int? = 0,
    val email: String? = "",
    val username: String? = "",
    val name: NameResponse? = null,
    val phone: String? = ""
)

data class NameResponse(
    val firstname: String? = "",
    val lastname: String? = ""
)

data class CartResponse(
    val id: Int? = 0,
    val userId: Int? = 0,
    val date: String? = "",
    val products: List<CartProductResponse>? = emptyList()
)

data class CartProductResponse(
    val productId: Int? = 0,
    val quantity: Int? = 0
)

data class ProductResponse(
    val id: Int? = 0,
    val title: String? = "",
    val price: Double? = 0.0,
    val description: String? = "",
    val category: String? = "",
    val image: String? = ""
)