package com.example.historicocarritos

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("carts")
    suspend fun getCarts(): List<CartResponse>

    @GET("products")
    suspend fun getProducts(): List<ProductResponse>

    companion object {
        // Ejemplo usando Fakestore API (o reemplaza con la URL base de tu servidor)
        private const val BASE_URL = "https://fakestoreapi.com/"

        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}