package com.example.estructura.domain.repository;

import com.example.estructura.data.model.Product;

public interface ProductRepository {

    void getProductById(int productId, ProductDetailCallback callback);

    interface ProductDetailCallback {

        void onSuccess(Product product);

        void onError(Throwable throwable);
    }
}