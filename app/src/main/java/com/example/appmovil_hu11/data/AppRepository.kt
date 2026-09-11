package com.example.appmovil_hu11.data

class AppRepository(private val api: ApiService = ApiService.instance) {

    suspend fun getUsersUI(): List<UserUI> {
        val users = api.getUsers()
        return users.map { response ->
            val firstName = response.name?.firstname?.replaceFirstChar { it.uppercase() } ?: "Usuario"
            val lastName = response.name?.lastname?.replaceFirstChar { it.uppercase() } ?: ""

            UserUI(
                id = response.id ?: 0,
                name = "$firstName $lastName".trim(),
                username = response.username ?: "sin_usuario",
                email = response.email ?: "sin_email",
                phone = response.phone ?: "sin_telefono"
            )
        }
    }
}