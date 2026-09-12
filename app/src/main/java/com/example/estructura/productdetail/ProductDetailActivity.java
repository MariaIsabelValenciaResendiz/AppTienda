package com.example.estructura.productdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.estructura.R;
import com.example.estructura.data.model.Product;
import com.example.estructura.data.remote.ProductApiService;
import com.example.estructura.data.remote.RetrofitClient;
import com.example.estructura.data.repository.ProductRepositoryImpl;
import com.example.estructura.databinding.ActivityProductDetailBinding;
import com.example.estructura.databinding.LayoutProductActionsAdminBinding;
import com.example.estructura.databinding.LayoutProductActionsAuditorBinding;
import com.example.estructura.databinding.LayoutProductActionsClientBinding;
import com.example.estructura.domain.repository.ProductRepository;
import com.example.estructura.domain.usecase.GetProductDetailUseCase;
import com.example.estructura.modelos.RolUsuario;
import com.example.estructura.utilidades.GestorSesion;
import com.google.android.material.snackbar.Snackbar;

public class ProductDetailActivity extends AppCompatActivity {

    private static final String EXTRA_PRODUCT_ID =
            "com.example.estructura.extra.PRODUCT_ID";

    public static final String EXTRA_PRODUCT_UNAVAILABLE =
            "com.example.estructura.extra.PRODUCT_UNAVAILABLE";

    public static final int RESULT_PRODUCT_UNAVAILABLE =
            RESULT_FIRST_USER + 5;

    private static final int INVALID_PRODUCT_ID = -1;

    private ActivityProductDetailBinding binding;
    private ProductDetailViewModel viewModel;
    private RolUsuario currentUserRole;
    private LayoutProductActionsClientBinding clientActionsBinding;

    private boolean roleActionsInflated;
    private boolean errorResultSent;
    private Integer displayedProductId;

    @NonNull
    public static Intent createIntent(
            @NonNull Context context,
            int productId
    ) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        applySystemBarInsets();
        configureBackButton();

        GestorSesion gestorSesion = GestorSesion.obtenerInstancia(this);
        currentUserRole = gestorSesion.obtenerRol();

        viewModel = createViewModel();
        observeUiState();

        int productId = getIntent().getIntExtra(
                EXTRA_PRODUCT_ID,
                INVALID_PRODUCT_ID
        );

        viewModel.loadProduct(productId);
    }

    private ProductDetailViewModel createViewModel() {
        ProductApiService apiService =
                RetrofitClient.createProductApiService();

        ProductRepository productRepository =
                new ProductRepositoryImpl(apiService);

        GetProductDetailUseCase useCase =
                new GetProductDetailUseCase(productRepository);

        ProductDetailViewModelFactory factory =
                new ProductDetailViewModelFactory(useCase);

        return new ViewModelProvider(this, factory)
                .get(ProductDetailViewModel.class);
    }

    private void applySystemBarInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(
                binding.productDetailRoot,
                (view, windowInsets) -> {
                    Insets systemBars = windowInsets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );

                    view.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return windowInsets;
                }
        );
    }

    private void configureBackButton() {
        binding.buttonBack.setOnClickListener(view -> finish());
    }

    private void observeUiState() {
        viewModel.getUiState().observe(
                this,
                this::renderUiState
        );
    }

    private void renderUiState(ProductDetailUiState state) {
        if (state == null) {
            return;
        }

        switch (state.getStatus()) {
            case LOADING:
                showLoadingState();
                break;

            case SUCCESS:
                showSuccessState(state);
                break;

            case ERROR:
                showErrorState();
                break;
        }
    }

    private void showLoadingState() {
        binding.loadingContainer.setVisibility(View.VISIBLE);
        binding.contentScroll.setVisibility(View.INVISIBLE);
        binding.productActionsContainer.setVisibility(View.GONE);
    }

    private void showSuccessState(ProductDetailUiState state) {
        Product product = state.getProduct();

        if (product == null) {
            showErrorState();
            return;
        }

        binding.loadingContainer.setVisibility(View.GONE);
        binding.contentScroll.setVisibility(View.VISIBLE);

        if (displayedProductId == null
                || displayedProductId != product.getId()) {
            displayProduct(product);
            displayedProductId = product.getId();
        }

        inflateActionsForCurrentRole();
        updateRoleActions(state);

        binding.productActionsContainer.setVisibility(View.VISIBLE);
    }

    private void displayProduct(Product product) {
        binding.textProductCategoryBadge.setText(product.getCategory());
        binding.textProductTitle.setText(product.getTitle());

        binding.textProductPrice.setText(
                getString(
                        R.string.product_price_format,
                        product.getPrice()
                )
        );

        binding.textProductDescription.setText(
                product.getDescription()
        );

        binding.textProductInfoCategory.setText(
                product.getCategory()
        );

        binding.textProductInfoId.setText(
                getString(
                        R.string.product_identifier_format,
                        product.getId()
                )
        );

        Glide.with(this)
                .load(product.getImage())
                .placeholder(
                        R.drawable.imagen_provisional_para_github
                )
                .error(
                        R.drawable.imagen_provisional_para_github
                )
                .into(binding.imageProduct);
    }

    private void inflateActionsForCurrentRole() {
        if (roleActionsInflated) {
            return;
        }

        binding.productActionsContainer.removeAllViews();

        switch (currentUserRole) {
            case CLIENTE:
                inflateClientActions();
                break;

            case ADMINISTRADOR:
                inflateAdministratorActions();
                break;

            case AUDITOR:
                inflateAuditorActions();
                break;
        }

        roleActionsInflated = true;
    }

    private void inflateClientActions() {
        clientActionsBinding =
                LayoutProductActionsClientBinding.inflate(
                        getLayoutInflater(),
                        binding.productActionsContainer,
                        true
                );

        clientActionsBinding.buttonDecreaseQuantity.setOnClickListener(
                view -> viewModel.decreaseQuantity()
        );

        clientActionsBinding.buttonIncreaseQuantity.setOnClickListener(
                view -> viewModel.increaseQuantity()
        );

        clientActionsBinding.buttonAddToCart.setOnClickListener(
                view -> Snackbar.make(
                        binding.getRoot(),
                        R.string.cart_integration_pending,
                        Snackbar.LENGTH_LONG
                ).show()
        );
    }

    private void inflateAdministratorActions() {
        LayoutProductActionsAdminBinding adminBinding =
                LayoutProductActionsAdminBinding.inflate(
                        getLayoutInflater(),
                        binding.productActionsContainer,
                        true
                );

        adminBinding.buttonEditProduct.setOnClickListener(
                view -> Snackbar.make(
                        binding.getRoot(),
                        R.string.edit_integration_pending,
                        Snackbar.LENGTH_LONG
                ).show()
        );

        adminBinding.buttonDeleteProduct.setOnClickListener(
                view -> Snackbar.make(
                        binding.getRoot(),
                        R.string.delete_integration_pending,
                        Snackbar.LENGTH_LONG
                ).show()
        );
    }

    private void inflateAuditorActions() {
        LayoutProductActionsAuditorBinding auditorBinding =
                LayoutProductActionsAuditorBinding.inflate(
                        getLayoutInflater(),
                        binding.productActionsContainer,
                        true
                );

        auditorBinding.buttonReturnToCatalog.setOnClickListener(
                view -> finish()
        );
    }

    private void updateRoleActions(ProductDetailUiState state) {
        if (currentUserRole != RolUsuario.CLIENTE
                || clientActionsBinding == null) {
            return;
        }

        int quantity = state.getQuantity();

        clientActionsBinding.textQuantity.setText(
                getString(
                        R.string.quantity_value_format,
                        quantity
                )
        );

        clientActionsBinding.textSubtotal.setText(
                getString(
                        R.string.subtotal_format,
                        state.getSubtotal()
                )
        );

        boolean canDecrease = quantity > 1;

        clientActionsBinding.buttonDecreaseQuantity.setEnabled(
                canDecrease
        );

        clientActionsBinding.buttonDecreaseQuantity.setAlpha(
                canDecrease ? 1.0f : 0.4f
        );
    }

    private void showErrorState() {
        if (errorResultSent) {
            return;
        }

        errorResultSent = true;

        binding.loadingContainer.setVisibility(View.GONE);
        binding.contentScroll.setVisibility(View.INVISIBLE);
        binding.productActionsContainer.setVisibility(View.GONE);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_PRODUCT_UNAVAILABLE, true);

        setResult(
                RESULT_PRODUCT_UNAVAILABLE,
                resultIntent
        );

        finish();
    }
}