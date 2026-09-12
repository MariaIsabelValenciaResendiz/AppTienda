package com.example.estructura.productdetail;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.estructura.data.model.Product;

public final class ProductDetailUiState {

    public enum Status {
        LOADING,
        SUCCESS,
        ERROR
    }

    @NonNull
    private final Status status;

    @Nullable
    private final Product product;

    private final int quantity;

    @Nullable
    private final Throwable error;

    private ProductDetailUiState(
            @NonNull Status status,
            @Nullable Product product,
            int quantity,
            @Nullable Throwable error
    ) {
        this.status = status;
        this.product = product;
        this.quantity = quantity;
        this.error = error;
    }

    public static ProductDetailUiState loading() {
        return new ProductDetailUiState(
                Status.LOADING,
                null,
                0,
                null
        );
    }

    public static ProductDetailUiState success(
            @NonNull Product product,
            int quantity
    ) {
        if (quantity < 1) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser al menos uno."
            );
        }

        return new ProductDetailUiState(
                Status.SUCCESS,
                product,
                quantity,
                null
        );
    }

    public static ProductDetailUiState error(
            @NonNull Throwable throwable
    ) {
        return new ProductDetailUiState(
                Status.ERROR,
                null,
                0,
                throwable
        );
    }

    @NonNull
    public Status getStatus() {
        return status;
    }

    @Nullable
    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getSubtotal() {
        if (product == null) {
            return 0;
        }

        return product.getPrice() * quantity;
    }

    @Nullable
    public Throwable getError() {
        return error;
    }
}
