package com.example.estructura.domain.usecase;

import com.example.estructura.domain.repository.ProductRepository;

public class GetProductDetailUseCase {

    private final ProductRepository productRepository;

    public GetProductDetailUseCase(ProductRepository productRepository) {
        if (productRepository == null) {
            throw new IllegalArgumentException("ProductRepository no puede ser nulo.");
        }

        this.productRepository = productRepository;
    }

    public void execute(
            int productId,
            ProductRepository.ProductDetailCallback callback
    ) {
        if (callback == null) {
            throw new IllegalArgumentException("ProductDetailCallback no puede ser nulo.");
        }

        if (productId <= 0) {
            callback.onError(
                    new IllegalArgumentException(
                            "El identificador del producto debe ser mayor que cero."
                    )
            );
            return;
        }

        productRepository.getProductById(productId, callback);
    }
}
