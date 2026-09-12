package com.example.estructura.domain.usecase;

import androidx.annotation.NonNull;

import com.example.estructura.data.model.CreateProductRequest;
import com.example.estructura.data.model.Product;
import com.example.estructura.domain.repository.ProductRepository;
import com.example.estructura.domain.validation.ProductFormValidator;
import com.example.estructura.domain.validation.ProductValidationResult;

public class CreateProductUseCase {

    private final ProductRepository productRepository;
    private final ProductFormValidator productFormValidator;

    public CreateProductUseCase(
            ProductRepository productRepository,
            ProductFormValidator productFormValidator
    ) {
        if (productRepository == null) {
            throw new IllegalArgumentException(
                    "ProductRepository no puede ser nulo."
            );
        }

        if (productFormValidator == null) {
            throw new IllegalArgumentException(
                    "ProductFormValidator no puede ser nulo."
            );
        }

        this.productRepository = productRepository;
        this.productFormValidator = productFormValidator;
    }

    public void execute(
            String title,
            String priceText,
            String description,
            String imageUrl,
            String category,
            Callback callback
    ) {
        if (callback == null) {
            throw new IllegalArgumentException(
                    "Callback no puede ser nulo."
            );
        }

        ProductValidationResult validationResult =
                productFormValidator.validate(
                        title,
                        priceText,
                        description,
                        imageUrl,
                        category
                );

        if (!validationResult.isValid()) {
            callback.onValidationError(validationResult);
            return;
        }

        CreateProductRequest request =
                new CreateProductRequest(
                        title.trim(),
                        validationResult.getValidatedPrice(),
                        description.trim(),
                        imageUrl.trim(),
                        category.trim()
                );

        callback.onRequestStarted();

        productRepository.createProduct(
                request,
                new ProductRepository.CreateProductCallback() {
                    @Override
                    public void onSuccess(Product product) {
                        callback.onSuccess(product);
                    }

                    @Override
                    public void onError(
                            @NonNull Throwable throwable
                    ) {
                        callback.onNetworkError(throwable);
                    }
                }
        );
    }

    public interface Callback {

        void onValidationError(
                ProductValidationResult validationResult
        );

        void onRequestStarted();

        void onSuccess(Product product);

        void onNetworkError(Throwable throwable);
    }
}