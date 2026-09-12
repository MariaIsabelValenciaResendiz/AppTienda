package com.example.estructura.data.repository;

import androidx.annotation.NonNull;

import com.example.estructura.data.model.CreateProductRequest;
import com.example.estructura.data.model.Product;
import com.example.estructura.data.remote.ProductApiService;
import com.example.estructura.domain.repository.ProductRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepositoryImpl implements ProductRepository {

    private final ProductApiService productApiService;

    public ProductRepositoryImpl(
            ProductApiService productApiService
    ) {
        if (productApiService == null) {
            throw new IllegalArgumentException(
                    "ProductApiService no puede ser nulo."
            );
        }

        this.productApiService = productApiService;
    }

    @Override
    public void createProduct(
            CreateProductRequest request,
            CreateProductCallback callback
    ) {
        if (request == null) {
            throw new IllegalArgumentException(
                    "CreateProductRequest no puede ser nulo."
            );
        }

        if (callback == null) {
            throw new IllegalArgumentException(
                    "CreateProductCallback no puede ser nulo."
            );
        }

        productApiService.createProduct(request).enqueue(
                new Callback<Product>() {
                    @Override
                    public void onResponse(
                            @NonNull Call<Product> call,
                            @NonNull Response<Product> response
                    ) {
                        Product createdProduct = response.body();

                        if (response.isSuccessful()
                                && isValidResponse(createdProduct)) {
                            callback.onSuccess(createdProduct);
                            return;
                        }

                        callback.onError(
                                new IllegalStateException(
                                        "La API devolvió una respuesta inválida."
                                )
                        );
                    }

                    @Override
                    public void onFailure(
                            @NonNull Call<Product> call,
                            @NonNull Throwable throwable
                    ) {
                        callback.onError(throwable);
                    }
                }
        );
    }

    private boolean isValidResponse(Product product) {
        return product != null && product.getId() > 0;
    }
}