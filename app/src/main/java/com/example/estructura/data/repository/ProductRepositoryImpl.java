package com.example.estructura.data.repository;

import androidx.annotation.NonNull;

import com.example.estructura.data.model.Product;
import com.example.estructura.data.remote.ProductApiService;
import com.example.estructura.domain.repository.ProductRepository;
import com.example.estructura.data.model.CreateProductRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepositoryImpl implements ProductRepository {

    private final ProductApiService productApiService;

    public ProductRepositoryImpl(ProductApiService productApiService) {
        if (productApiService == null) {
            throw new IllegalArgumentException("ProductApiService no puede ser nulo.");
        }

        this.productApiService = productApiService;
    }

    @Override
    public void getProductById(
            int productId,
            ProductDetailCallback callback
    ) {
        if (callback == null) {
            throw new IllegalArgumentException("ProductDetailCallback no puede ser nulo.");
        }

        productApiService.getProductById(productId).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(
                    @NonNull Call<Product> call,
                    @NonNull Response<Product> response
            ) {
                Product product = response.body();

                if (response.isSuccessful() && isValidProduct(product)) {
                    callback.onSuccess(product);
                    return;
                }

                callback.onError(
                        new IllegalStateException("El producto solicitado no está disponible.")
                );
            }

            @Override
            public void onFailure(
                    @NonNull Call<Product> call,
                    @NonNull Throwable throwable
            ) {
                callback.onError(throwable);
            }
        });
    }
    @Override
    public void createProduct(
            CreateProductRequest request,
            ProductRepository.CreateProductCallback callback
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
                                && createdProduct != null
                                && createdProduct.getId() > 0) {
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

    private boolean isValidProduct(Product product) {
        if (product == null) {
            return false;
        }

        return product.getId() > 0
                && product.getTitle() != null
                && !product.getTitle().trim().isEmpty()
                && product.getDescription() != null
                && !product.getDescription().trim().isEmpty()
                && product.getCategory() != null
                && !product.getCategory().trim().isEmpty()
                && product.getImage() != null
                && !product.getImage().trim().isEmpty()
                && product.getPrice() >= 0;
    }
}