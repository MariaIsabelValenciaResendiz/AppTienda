package com.example.estructura.domain.repository;

import com.example.estructura.data.model.CreateProductRequest;
import com.example.estructura.data.model.Product;

public interface ProductRepository {

    // US05: consultar el detalle
    void getProductById(
            int productId,
            ProductDetailCallback callback
    );

    // US06: crear un producto
    void createProduct(
            CreateProductRequest request,
            CreateProductCallback callback
    );

    interface ProductDetailCallback {
        void onSuccess(Product product);
        void onError(Throwable throwable);
    }

    interface CreateProductCallback {
        void onSuccess(Product product);
        void onError(Throwable throwable);
    }
}