package com.example.estructura.data.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitClient {

    private static final String BASE_URL = "https://fakestoreapi.com/";

    private static volatile Retrofit retrofitInstance;

    private RetrofitClient() {
        // Evita que esta clase de configuración sea instanciada.
    }

    public static Retrofit getInstance() {
        if (retrofitInstance == null) {
            synchronized (RetrofitClient.class) {
                if (retrofitInstance == null) {
                    retrofitInstance = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }

        return retrofitInstance;
    }

    public static ProductApiService createProductApiService() {
        return getInstance().create(ProductApiService.class);
    }
}