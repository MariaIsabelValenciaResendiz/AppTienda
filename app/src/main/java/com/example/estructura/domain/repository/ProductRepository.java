package com.example.estructura.domain.repository;

import com.example.estructura.data.model.CreateProductRequest;
import com.example.estructura.data.model.Product;

public interface ProductRepository {

    void createProduct(
            CreateProductRequest request,
            CreateProductCallback callback
    );

    interface CreateProductCallback {

        void onSuccess(Product product);

        void onError(Throwable throwable);
    }
}