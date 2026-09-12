package com.example.estructura.data.remote;

import com.example.estructura.data.model.Product;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductApiService {

    @GET("products/{id}")
    Call<Product> getProductById(@Path("id") int productId);
}