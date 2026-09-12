package com.example.estructura.productcreate;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.estructura.domain.usecase.CreateProductUseCase;

@SuppressWarnings({
        "unchecked",
        "deprecation"
})
public class CreateProductViewModelFactory
        implements ViewModelProvider.Factory {

    private final CreateProductUseCase createProductUseCase;

    public CreateProductViewModelFactory(
            CreateProductUseCase createProductUseCase
    ) {
        if (createProductUseCase == null) {
            throw new IllegalArgumentException(
                    "CreateProductUseCase no puede ser nulo."
            );
        }

        this.createProductUseCase = createProductUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(
            @NonNull Class<T> modelClass
    ) {
        if (modelClass.isAssignableFrom(
                CreateProductViewModel.class
        )) {
            return (T) new CreateProductViewModel(
                    createProductUseCase
            );
        }

        throw new IllegalArgumentException(
                "ViewModel desconocido: " + modelClass.getName()
        );
    }
}