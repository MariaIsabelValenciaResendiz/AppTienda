package com.example.estructura.data.remote;

import com.example.estructura.data.model.Product;
import com.example.estructura.data.model.CreateProductRequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ProductApiService {

    @GET("products/{id}")
    Call<Product> getProductById(@Path("id") int productId);
    @POST("products")
    Call<Product> createProduct(@Body CreateProductRequest request);
}