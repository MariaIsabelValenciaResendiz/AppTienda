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
import com.example.estructura.databinding.LayoutAdminCatalogActionBinding;
import com.example.estructura.session.SessionManager;
import com.example.estructura.session.UserRole;
import com.example.estructura.productcreate.CreateProductActivity;
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private LayoutAdminCatalogActionBinding adminActionBinding;

    private SessionManager sessionManager;

    private ActivityResultLauncher<Intent>
            createProductLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(
                getLayoutInflater()
        );

        setContentView(binding.getRoot());

        sessionManager = new SessionManager(
                getApplicationContext()
        );

        configureSystemBarInsets();
        configureCreateProductResult();

        UserRole savedRole = restoreSavedRole();

        updateAdminAction(savedRole);
        configureRoleSelector();
        configureTemporaryProtectedRouteTest();
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

    private void configureCreateProductResult() {
        createProductLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode()
                            == CreateProductActivity.RESULT_ACCESS_DENIED) {
                        showAccessDeniedAlert();
                    }
                }
        );
    }

    private UserRole restoreSavedRole() {
        UserRole savedRole = sessionManager.getUserRole();

        switch (savedRole) {
            case ADMINISTRADOR:
                binding.radioAdministrator.setChecked(true);
                break;

            case CLIENTE:
                binding.radioClient.setChecked(true);
                break;

            case AUDITOR:
                binding.radioAuditor.setChecked(true);
                break;
        }

        return savedRole;
    }

    private void configureRoleSelector() {
        binding.radioGroupRole.setOnCheckedChangeListener(
                (group, checkedId) -> {
                    UserRole selectedRole =
                            getSelectedRole(checkedId);

                    /*
                     * Configuración temporal de prueba.
                     * US01 reemplazará esta asignación.
                     */
                    sessionManager.setUserRole(selectedRole);

                    binding.accessDeniedAlert.setVisibility(
                            View.GONE
                    );

                    updateAdminAction(selectedRole);
                }
        );
    }

    private UserRole getSelectedRole(int checkedId) {
        if (checkedId == R.id.radioAdministrator) {
            return UserRole.ADMINISTRADOR;
        }

        if (checkedId == R.id.radioAuditor) {
            return UserRole.AUDITOR;
        }

        return UserRole.CLIENTE;
    }

    private void updateAdminAction(UserRole userRole) {
        if (userRole == UserRole.ADMINISTRADOR) {
            inflateAdminAction();
            return;
        }

        removeAdminAction();
    }

    private void inflateAdminAction() {
        if (adminActionBinding != null) {
            return;
        }

        adminActionBinding =
                LayoutAdminCatalogActionBinding.inflate(
                        getLayoutInflater(),
                        binding.adminActionContainer,
                        true
                );

        adminActionBinding.buttonNewProduct.setOnClickListener(
                view -> openCreateProduct()
        );
    }

    private void removeAdminAction() {
        binding.adminActionContainer.removeAllViews();
        adminActionBinding = null;
    }

    private void openCreateProduct() {
        binding.accessDeniedAlert.setVisibility(View.GONE);

        createProductLauncher.launch(
                CreateProductActivity.createIntent(this)
        );
    }

    private void showAccessDeniedAlert() {
        binding.accessDeniedAlert.setVisibility(View.VISIBLE);

        binding.accessDeniedAlert.announceForAccessibility(
                getString(R.string.access_denied)
        );
    }

    private void configureTemporaryProtectedRouteTest() {
        /*
         * Gesto temporal de prueba:
         * mantener presionado el texto NOVA intenta abrir directamente
         * CreateProductActivity con el rol actualmente guardado.
         *
         * Eliminar este método y su llamada cuando se integren
         * el inicio de sesión de US01 y el catálogo de US03.
         */
        binding.textAppName.setOnLongClickListener(
                view -> {
                    openCreateProduct();
                    return true;
                }
        );
    }
}