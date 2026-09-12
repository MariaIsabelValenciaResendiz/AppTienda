package com.example.estructura.productcreate;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.estructura.data.model.Product;
import com.example.estructura.domain.usecase.CreateProductUseCase;
import com.example.estructura.domain.validation.ProductValidationResult;

public class CreateProductViewModel extends ViewModel {

    private final CreateProductUseCase createProductUseCase;

    private final MutableLiveData<CreateProductUiState> mutableUiState =
            new MutableLiveData<>();

    private boolean requestInProgress;

    public CreateProductViewModel(
            CreateProductUseCase createProductUseCase
    ) {
        if (createProductUseCase == null) {
            throw new IllegalArgumentException(
                    "CreateProductUseCase no puede ser nulo."
            );
        }

        this.createProductUseCase = createProductUseCase;
        mutableUiState.setValue(CreateProductUiState.idle());
    }

    public LiveData<CreateProductUiState> getUiState() {
        return mutableUiState;
    }

    public void createProduct(
            String title,
            String priceText,
            String description,
            String imageUrl,
            String category
    ) {
        if (requestInProgress) {
            return;
        }

        createProductUseCase.execute(
                title,
                priceText,
                description,
                imageUrl,
                category,
                new CreateProductUseCase.Callback() {
                    @Override
                    public void onValidationError(
                            ProductValidationResult validationResult
                    ) {
                        requestInProgress = false;

                        mutableUiState.setValue(
                                CreateProductUiState.validationError(
                                        validationResult
                                )
                        );
                    }

                    @Override
                    public void onRequestStarted() {
                        requestInProgress = true;

                        mutableUiState.setValue(
                                CreateProductUiState.loading()
                        );
                    }

                    @Override
                    public void onSuccess(Product product) {
                        requestInProgress = false;

                        mutableUiState.postValue(
                                CreateProductUiState.success(product)
                        );
                    }

                    @Override
                    public void onNetworkError(
                            @NonNull Throwable throwable
                    ) {
                        requestInProgress = false;

                        mutableUiState.postValue(
                                CreateProductUiState.networkError(
                                        throwable
                                )
                        );
                    }
                }
        );
    }

    public void resetToIdle() {
        if (requestInProgress) {
            return;
        }

        mutableUiState.setValue(
                CreateProductUiState.idle()
        );
    }

    public void acknowledgeNetworkError() {
        CreateProductUiState currentState =
                mutableUiState.getValue();

        if (currentState != null
                && currentState.getStatus()
                == CreateProductUiState.Status.NETWORK_ERROR) {
            mutableUiState.setValue(
                    CreateProductUiState.idle()
            );
        }
    }

    public boolean isRequestInProgress() {
        return requestInProgress;
    }
}