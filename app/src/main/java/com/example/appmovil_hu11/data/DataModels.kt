package com.example.appmovil_hu11.data

// MODELOS UI
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

// RESPUESTAS DE LA API (DTOs)
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