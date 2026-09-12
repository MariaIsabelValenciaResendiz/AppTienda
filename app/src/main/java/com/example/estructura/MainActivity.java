package com.example.estructura;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.estructura.databinding.ActivityMainBinding;
import com.example.estructura.session.SessionManager;
import com.example.estructura.session.UserRole;
import com.example.estructura.productdetail.ProductDetailActivity;
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SessionManager sessionManager;

    private ActivityResultLauncher<Intent> productDetailLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(getApplicationContext());

        configureSystemBarInsets();
        configureProductDetailResult();
        restoreSavedRole();
        configureOpenProductButton();
    }

    private void configureSystemBarInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(
                binding.main,
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

    private void configureProductDetailResult() {
        productDetailLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode()
                            == ProductDetailActivity.RESULT_PRODUCT_UNAVAILABLE) {
                        showProductUnavailableMessage();
                    }
                }
        );
    }

    private void restoreSavedRole() {
        UserRole savedRole = sessionManager.getUserRole();

        switch (savedRole) {
            case CLIENTE:
                binding.radioClient.setChecked(true);
                break;

            case ADMINISTRADOR:
                binding.radioAdministrator.setChecked(true);
                break;

            case AUDITOR:
                binding.radioAuditor.setChecked(true);
                break;
        }
    }

    private void configureOpenProductButton() {
        binding.buttonOpenProduct.setOnClickListener(
                view -> openProductDetail()
        );
    }

    private void openProductDetail() {
        String productIdText = binding.inputProductId
                .getText()
                .toString()
                .trim();

        int productId;

        try {
            productId = Integer.parseInt(productIdText);
        } catch (NumberFormatException exception) {
            showInvalidProductIdError();
            return;
        }

        if (productId <= 0) {
            showInvalidProductIdError();
            return;
        }

        binding.productIdInputLayout.setError(null);
        binding.errorAlert.setVisibility(View.GONE);

        UserRole selectedRole = getSelectedRole();

        /*
         * Mecanismo temporal de prueba.
         * Cuando se integren US01 y US03, esta asignación será realizada
         * por el flujo real de inicio de sesión.
         */
        sessionManager.setUserRole(selectedRole);

        Intent detailIntent = ProductDetailActivity.createIntent(
                this,
                productId
        );

        productDetailLauncher.launch(detailIntent);
    }

    private UserRole getSelectedRole() {
        int selectedRadioButtonId =
                binding.radioGroupRole.getCheckedRadioButtonId();

        if (selectedRadioButtonId == R.id.radioAdministrator) {
            return UserRole.ADMINISTRADOR;
        }

        if (selectedRadioButtonId == R.id.radioAuditor) {
            return UserRole.AUDITOR;
        }

        return UserRole.CLIENTE;
    }

    private void showInvalidProductIdError() {
        binding.productIdInputLayout.setError(
                getString(R.string.invalid_product_id)
        );

        binding.productIdInputLayout.requestFocus();
    }

    private void showProductUnavailableMessage() {
        binding.errorAlert.setVisibility(View.VISIBLE);

        binding.errorAlert.announceForAccessibility(
                getString(R.string.product_unavailable)
        );
    }
}