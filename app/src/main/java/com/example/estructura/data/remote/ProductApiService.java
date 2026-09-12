package com.example.estructura.data.remote;

import com.example.estructura.data.model.CreateProductRequest;
import com.example.estructura.data.model.Product;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ProductApiService {

    @POST("products")
    Call<Product> createProduct(
            @Body CreateProductRequest request
    );
}