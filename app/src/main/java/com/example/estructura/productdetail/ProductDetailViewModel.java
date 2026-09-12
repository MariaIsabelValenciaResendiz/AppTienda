package com.example.estructura.productdetail;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.estructura.data.model.Product;
import com.example.estructura.domain.repository.ProductRepository;
import com.example.estructura.domain.usecase.GetProductDetailUseCase;

public class ProductDetailViewModel extends ViewModel {

    private final GetProductDetailUseCase getProductDetailUseCase;

    private final MutableLiveData<ProductDetailUiState> mutableUiState =
            new MutableLiveData<>();

    private Integer requestedProductId;

    public ProductDetailViewModel(
            GetProductDetailUseCase getProductDetailUseCase
    ) {
        if (getProductDetailUseCase == null) {
            throw new IllegalArgumentException(
                    "GetProductDetailUseCase no puede ser nulo."
            );
        }

        this.getProductDetailUseCase = getProductDetailUseCase;
    }

    public LiveData<ProductDetailUiState> getUiState() {
        return mutableUiState;
    }

    public void loadProduct(int productId) {
        ProductDetailUiState currentState = mutableUiState.getValue();

        boolean sameProductWasRequested =
                requestedProductId != null
                        && requestedProductId == productId;

        boolean requestIsAlreadyResolvedOrRunning =
                currentState != null
                        && (currentState.getStatus()
                        == ProductDetailUiState.Status.LOADING
                        || currentState.getStatus()
                        == ProductDetailUiState.Status.SUCCESS);

        if (sameProductWasRequested && requestIsAlreadyResolvedOrRunning) {
            return;
        }

        requestedProductId = productId;
        mutableUiState.setValue(ProductDetailUiState.loading());

        getProductDetailUseCase.execute(
                productId,
                new ProductRepository.ProductDetailCallback() {
                    @Override
                    public void onSuccess(Product product) {
                        if (!isCurrentRequest(productId)) {
                            return;
                        }

                        mutableUiState.postValue(
                                ProductDetailUiState.success(product, 1)
                        );
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        if (!isCurrentRequest(productId)) {
                            return;
                        }

                        mutableUiState.postValue(
                                ProductDetailUiState.error(throwable)
                        );
                    }
                }
        );
    }

    public void increaseQuantity() {
        ProductDetailUiState currentState = mutableUiState.getValue();

        if (!isSuccessfulState(currentState)) {
            return;
        }

        Product product = currentState.getProduct();
        int currentQuantity = currentState.getQuantity();

        if (product == null || currentQuantity == Integer.MAX_VALUE) {
            return;
        }

        mutableUiState.setValue(
                ProductDetailUiState.success(
                        product,
                        currentQuantity + 1
                )
        );
    }

    public void decreaseQuantity() {
        ProductDetailUiState currentState = mutableUiState.getValue();

        if (!isSuccessfulState(currentState)) {
            return;
        }

        Product product = currentState.getProduct();
        int currentQuantity = currentState.getQuantity();

        if (product == null || currentQuantity <= 1) {
            return;
        }

        mutableUiState.setValue(
                ProductDetailUiState.success(
                        product,
                        currentQuantity - 1
                )
        );
    }

    private boolean isSuccessfulState(
            ProductDetailUiState state
    ) {
        return state != null
                && state.getStatus()
                == ProductDetailUiState.Status.SUCCESS;
    }

    private boolean isCurrentRequest(int productId) {
        return requestedProductId != null
                && requestedProductId == productId;
    }
}
