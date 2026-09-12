package com.example.estructura.productcreate;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.estructura.R;
import com.example.estructura.data.model.Product;
import com.example.estructura.data.remote.ProductApiService;
import com.example.estructura.data.remote.RetrofitClient;
import com.example.estructura.data.repository.ProductRepositoryImpl;
import com.example.estructura.databinding.ActivityCreateProductBinding;
import com.example.estructura.databinding.DialogProductCreatedBinding;
import com.example.estructura.domain.repository.ProductRepository;
import com.example.estructura.domain.usecase.CreateProductUseCase;
import com.example.estructura.domain.validation.ProductFormValidator;
import com.example.estructura.domain.validation.ProductValidationResult;
import com.example.estructura.session.SessionManager;
import com.example.estructura.session.UserRole;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public class CreateProductActivity extends AppCompatActivity {

    public static final String EXTRA_ACCESS_DENIED =
            "com.example.estructura.extra.ACCESS_DENIED";

    public static final int RESULT_ACCESS_DENIED =
            RESULT_FIRST_USER + 6;

    private static final String STATE_SELECTED_CATEGORY =
            "selected_category";

    private static final String CATEGORY_ELECTRONICS =
            "electronics";

    private static final String CATEGORY_MENS_CLOTHING =
            "men's clothing";

    private static final String CATEGORY_WOMENS_CLOTHING =
            "women's clothing";

    private static final String CATEGORY_JEWELERY =
            "jewelery";

    private ActivityCreateProductBinding binding;
    private CreateProductViewModel viewModel;

    private String selectedCategoryApiValue = "";
    private AlertDialog confirmationDialog;

    @NonNull
    public static Intent createIntent(
            @NonNull Context context
    ) {
        return new Intent(
                context,
                CreateProductActivity.class
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManager sessionManager =
                new SessionManager(getApplicationContext());

        if (sessionManager.getUserRole()
                != UserRole.ADMINISTRADOR) {
            returnAccessDeniedResult();
            return;
        }

        EdgeToEdge.enable(this);

        binding = ActivityCreateProductBinding.inflate(
                getLayoutInflater()
        );

        setContentView(binding.getRoot());

        if (savedInstanceState != null) {
            selectedCategoryApiValue =
                    savedInstanceState.getString(
                            STATE_SELECTED_CATEGORY,
                            ""
                    );
        }

        configureSystemBarInsets();
        configureCategoryDropdown();
        configureActions();
        configureErrorClearing();
        configureErrorColors();

        viewModel = createViewModel();
        observeUiState();
    }

    @Override
    protected void onSaveInstanceState(
            @NonNull Bundle outState
    ) {
        outState.putString(
                STATE_SELECTED_CATEGORY,
                selectedCategoryApiValue
        );

        super.onSaveInstanceState(outState);
    }

    private CreateProductViewModel createViewModel() {
        ProductApiService apiService =
                RetrofitClient.createProductApiService();

        ProductRepository productRepository =
                new ProductRepositoryImpl(apiService);

        ProductFormValidator formValidator =
                new ProductFormValidator();

        CreateProductUseCase useCase =
                new CreateProductUseCase(
                        productRepository,
                        formValidator
                );

        CreateProductViewModelFactory factory =
                new CreateProductViewModelFactory(useCase);

        return new ViewModelProvider(this, factory)
                .get(CreateProductViewModel.class);
    }

    private void configureSystemBarInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(
                binding.createProductRoot,
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

    private void configureCategoryDropdown() {
        String[] visibleCategories =
                getResources().getStringArray(
                        R.array.product_categories
                );

        ArrayAdapter<String> categoryAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        visibleCategories
                );

        binding.inputCategory.setAdapter(categoryAdapter);

        binding.inputCategory.setOnItemClickListener(
                (parent, view, position, id) -> {
                    selectedCategoryApiValue =
                            getCategoryApiValue(position);

                    clearFieldError(
                            binding.categoryInputLayout
                    );
                }
        );

        binding.inputCategory.setOnClickListener(
                view -> binding.inputCategory.showDropDown()
        );
    }

    private String getCategoryApiValue(int position) {
        switch (position) {
            case 0:
                return CATEGORY_ELECTRONICS;

            case 1:
                return CATEGORY_MENS_CLOTHING;

            case 2:
                return CATEGORY_WOMENS_CLOTHING;

            case 3:
                return CATEGORY_JEWELERY;

            default:
                return "";
        }
    }

    private void configureActions() {
        binding.buttonBack.setOnClickListener(
                view -> closeWithoutCreating()
        );

        binding.buttonCancel.setOnClickListener(
                view -> closeWithoutCreating()
        );

        binding.buttonSaveProduct.setOnClickListener(
                view -> submitProduct()
        );
    }

    private void closeWithoutCreating() {
        if (viewModel != null
                && viewModel.isRequestInProgress()) {
            return;
        }

        finish();
    }

    private void submitProduct() {
        viewModel.createProduct(
                getText(binding.inputTitle),
                getText(binding.inputPrice),
                getText(binding.inputDescription),
                getText(binding.inputImageUrl),
                selectedCategoryApiValue
        );
    }

    private String getText(EditText editText) {
        Editable editable = editText.getText();

        return editable != null
                ? editable.toString()
                : "";
    }

    private void observeUiState() {
        viewModel.getUiState().observe(
                this,
                this::renderUiState
        );
    }

    private void renderUiState(
            CreateProductUiState state
    ) {
        if (state == null) {
            return;
        }

        switch (state.getStatus()) {
            case IDLE:
                setLoading(false);
                break;

            case VALIDATION_ERROR:
                setLoading(false);
                showValidationErrors(
                        state.getValidationResult()
                );
                break;

            case LOADING:
                clearAllFieldErrors();
                setLoading(true);
                break;

            case SUCCESS:
                setLoading(false);
                handleSuccess(
                        state.getCreatedProduct()
                );
                break;

            case NETWORK_ERROR:
                setLoading(false);
                showNetworkError();
                break;
        }
    }

    private void setLoading(boolean loading) {
        binding.savingContainer.setVisibility(
                loading ? View.VISIBLE : View.GONE
        );

        binding.buttonSaveProduct.setEnabled(!loading);
        binding.buttonCancel.setEnabled(!loading);
        binding.buttonBack.setEnabled(!loading);

        binding.inputTitle.setEnabled(!loading);
        binding.inputPrice.setEnabled(!loading);
        binding.inputDescription.setEnabled(!loading);
        binding.inputImageUrl.setEnabled(!loading);
        binding.inputCategory.setEnabled(!loading);
    }

    private void showValidationErrors(
            ProductValidationResult validationResult
    ) {
        if (validationResult == null) {
            return;
        }

        setFieldError(
                binding.titleInputLayout,
                validationResult.hasTitleError(),
                R.string.title_required_error
        );

        setFieldError(
                binding.priceInputLayout,
                validationResult.hasPriceError(),
                R.string.price_invalid_error
        );

        setFieldError(
                binding.descriptionInputLayout,
                validationResult.hasDescriptionError(),
                R.string.description_required_error
        );

        setFieldError(
                binding.imageUrlInputLayout,
                validationResult.hasImageUrlError(),
                R.string.image_url_invalid_error
        );

        setFieldError(
                binding.categoryInputLayout,
                validationResult.hasCategoryError(),
                R.string.category_required_error
        );

        focusFirstInvalidField(validationResult);
    }

    private void setFieldError(
            TextInputLayout inputLayout,
            boolean hasError,
            int messageResource
    ) {
        if (hasError) {
            inputLayout.setError(
                    getString(messageResource)
            );

            inputLayout.setBoxBackgroundColor(
                    ContextCompat.getColor(
                            this,
                            R.color.nova_error_background
                    )
            );
            return;
        }

        clearFieldError(inputLayout);
    }

    private void focusFirstInvalidField(
            ProductValidationResult validationResult
    ) {
        View firstInvalidField;

        if (validationResult.hasTitleError()) {
            firstInvalidField = binding.inputTitle;
        } else if (validationResult.hasPriceError()) {
            firstInvalidField = binding.inputPrice;
        } else if (validationResult.hasDescriptionError()) {
            firstInvalidField = binding.inputDescription;
        } else if (validationResult.hasImageUrlError()) {
            firstInvalidField = binding.inputImageUrl;
        } else if (validationResult.hasCategoryError()) {
            firstInvalidField = binding.inputCategory;
        } else {
            return;
        }

        firstInvalidField.requestFocus();

        binding.formScroll.post(
                () -> binding.formScroll.requestChildFocus(
                        firstInvalidField,
                        firstInvalidField
                )
        );
    }

    private void configureErrorClearing() {
        addErrorClearingWatcher(
                binding.inputTitle,
                binding.titleInputLayout
        );

        addErrorClearingWatcher(
                binding.inputPrice,
                binding.priceInputLayout
        );

        addErrorClearingWatcher(
                binding.inputDescription,
                binding.descriptionInputLayout
        );

        addErrorClearingWatcher(
                binding.inputImageUrl,
                binding.imageUrlInputLayout
        );
    }

    private void addErrorClearingWatcher(
            EditText editText,
            TextInputLayout inputLayout
    ) {
        editText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence text,
                            int start,
                            int count,
                            int after
                    ) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence text,
                            int start,
                            int before,
                            int count
                    ) {
                    }

                    @Override
                    public void afterTextChanged(
                            Editable editable
                    ) {
                        clearFieldError(inputLayout);
                    }
                }
        );
    }

    private void configureErrorColors() {
        ColorStateList errorColor =
                ColorStateList.valueOf(
                        ContextCompat.getColor(
                                this,
                                R.color.nova_error
                        )
                );

        binding.titleInputLayout.setBoxStrokeErrorColor(
                errorColor
        );

        binding.priceInputLayout.setBoxStrokeErrorColor(
                errorColor
        );

        binding.descriptionInputLayout.setBoxStrokeErrorColor(
                errorColor
        );

        binding.imageUrlInputLayout.setBoxStrokeErrorColor(
                errorColor
        );

        binding.categoryInputLayout.setBoxStrokeErrorColor(
                errorColor
        );
    }

    private void clearFieldError(
            TextInputLayout inputLayout
    ) {
        inputLayout.setError(null);

        inputLayout.setBoxBackgroundColor(
                ContextCompat.getColor(
                        this,
                        R.color.nova_surface
                )
        );
    }

    private void clearAllFieldErrors() {
        clearFieldError(binding.titleInputLayout);
        clearFieldError(binding.priceInputLayout);
        clearFieldError(binding.descriptionInputLayout);
        clearFieldError(binding.imageUrlInputLayout);
        clearFieldError(binding.categoryInputLayout);
    }

    private void showNetworkError() {
        Snackbar.make(
                binding.getRoot(),
                R.string.create_product_network_error,
                Snackbar.LENGTH_LONG
        ).show();

        viewModel.acknowledgeNetworkError();
    }

    private void handleSuccess(Product createdProduct) {
        if (createdProduct == null) {
            showNetworkError();
            return;
        }

        clearForm();
        showCreatedProductConfirmation(
                createdProduct.getId()
        );
    }

    private void clearForm() {
        binding.inputTitle.setText("");
        binding.inputPrice.setText("");
        binding.inputDescription.setText("");
        binding.inputImageUrl.setText("");
        binding.inputCategory.setText("", false);

        selectedCategoryApiValue = "";

        clearAllFieldErrors();
    }

    private void showCreatedProductConfirmation(
            int productId
    ) {
        if (confirmationDialog != null
                && confirmationDialog.isShowing()) {
            return;
        }

        DialogProductCreatedBinding dialogBinding =
                DialogProductCreatedBinding.inflate(
                        getLayoutInflater()
                );

        dialogBinding.textCreatedProductId.setText(
                getString(
                        R.string.created_product_id_format,
                        productId
                )
        );

        confirmationDialog =
                new MaterialAlertDialogBuilder(this)
                        .setView(dialogBinding.getRoot())
                        .setCancelable(false)
                        .create();

        dialogBinding.buttonReturnToCatalog.setOnClickListener(
                view -> {
                    confirmationDialog.dismiss();
                    setResult(RESULT_OK);
                    finish();
                }
        );

        dialogBinding.buttonAddAnotherProduct.setOnClickListener(
                view -> {
                    confirmationDialog.dismiss();
                    confirmationDialog = null;

                    viewModel.resetToIdle();
                    focusTitleField();
                }
        );

        confirmationDialog.show();

        if (confirmationDialog.getWindow() != null) {
            confirmationDialog
                    .getWindow()
                    .setBackgroundDrawable(
                            new ColorDrawable(
                                    Color.TRANSPARENT
                            )
                    );
        }
    }

    private void focusTitleField() {
        binding.inputTitle.post(
                () -> {
                    binding.inputTitle.requestFocus();

                    InputMethodManager inputMethodManager =
                            (InputMethodManager) getSystemService(
                                    Context.INPUT_METHOD_SERVICE
                            );

                    inputMethodManager.showSoftInput(
                            binding.inputTitle,
                            InputMethodManager.SHOW_IMPLICIT
                    );
                }
        );
    }

    private void returnAccessDeniedResult() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_ACCESS_DENIED, true);

        setResult(
                RESULT_ACCESS_DENIED,
                resultIntent
        );

        finish();
    }

    @Override
    protected void onDestroy() {
        if (confirmationDialog != null
                && confirmationDialog.isShowing()) {
            confirmationDialog.dismiss();
        }

        confirmationDialog = null;

        super.onDestroy();
    }
}