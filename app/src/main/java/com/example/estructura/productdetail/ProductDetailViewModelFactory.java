package com.example.estructura.productdetail;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.estructura.domain.usecase.GetProductDetailUseCase;

public class ProductDetailViewModelFactory
        implements ViewModelProvider.Factory {

    private final GetProductDetailUseCase getProductDetailUseCase;

    public ProductDetailViewModelFactory(
            GetProductDetailUseCase getProductDetailUseCase
    ) {
        if (getProductDetailUseCase == null) {
            throw new IllegalArgumentException(
                    "GetProductDetailUseCase no puede ser nulo."
            );
        }

        this.getProductDetailUseCase = getProductDetailUseCase;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(
            @NonNull Class<T> modelClass
    ) {
        if (modelClass.isAssignableFrom(
                ProductDetailViewModel.class
        )) {
            return (T) new ProductDetailViewModel(
                    getProductDetailUseCase
            );
        }

        throw new IllegalArgumentException(
                "ViewModel desconocido: " + modelClass.getName()
        );
    }
}