package com.example.appmovil_hu11.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {

    @GET("users")
    suspend fun getUsers(): List<UserResponse>

    @GET("carts")
    suspend fun getCarts(): List<CartResponse>

    @GET("products")
    suspend fun getProducts(): List<ProductResponse>

    companion object {
        private const val BASE_URL = "https://fakestoreapi.com/"

        val instance: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}