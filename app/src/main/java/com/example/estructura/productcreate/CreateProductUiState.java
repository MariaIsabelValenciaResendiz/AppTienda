package com.example.estructura.productcreate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.estructura.data.model.Product;
import com.example.estructura.domain.validation.ProductValidationResult;

public final class CreateProductUiState {

    public enum Status {
        IDLE,
        VALIDATION_ERROR,
        LOADING,
        SUCCESS,
        NETWORK_ERROR
    }

    @NonNull
    private final Status status;

    @Nullable
    private final ProductValidationResult validationResult;

    @Nullable
    private final Product createdProduct;

    @Nullable
    private final Throwable networkError;

    private CreateProductUiState(
            @NonNull Status status,
            @Nullable ProductValidationResult validationResult,
            @Nullable Product createdProduct,
            @Nullable Throwable networkError
    ) {
        this.status = status;
        this.validationResult = validationResult;
        this.createdProduct = createdProduct;
        this.networkError = networkError;
    }

    public static CreateProductUiState idle() {
        return new CreateProductUiState(
                Status.IDLE,
                null,
                null,
                null
        );
    }

    public static CreateProductUiState validationError(
            @NonNull ProductValidationResult validationResult
    ) {
        return new CreateProductUiState(
                Status.VALIDATION_ERROR,
                validationResult,
                null,
                null
        );
    }

    public static CreateProductUiState loading() {
        return new CreateProductUiState(
                Status.LOADING,
                null,
                null,
                null
        );
    }

    public static CreateProductUiState success(
            @NonNull Product createdProduct
    ) {
        return new CreateProductUiState(
                Status.SUCCESS,
                null,
                createdProduct,
                null
        );
    }

    public static CreateProductUiState networkError(
            @NonNull Throwable throwable
    ) {
        return new CreateProductUiState(
                Status.NETWORK_ERROR,
                null,
                null,
                throwable
        );
    }

    @NonNull
    public Status getStatus() {
        return status;
    }

    @Nullable
    public ProductValidationResult getValidationResult() {
        return validationResult;
    }

    @Nullable
    public Product getCreatedProduct() {
        return createdProduct;
    }

    @Nullable
    public Throwable getNetworkError() {
        return networkError;
    }
}