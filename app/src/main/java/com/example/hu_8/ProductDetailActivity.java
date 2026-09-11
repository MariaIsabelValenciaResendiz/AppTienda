package com.example.hu_8;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.bumptech.glide.Glide;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;

public class ProductDetailActivity extends AppCompatActivity {

    private int currentPosition;
    private Button btnDelete;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            if (insets != null) {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            }
            return insets;
        });

        ImageView ivDetailImage = findViewById(R.id.ivDetailImage);
        TextView tvDetailTitle = findViewById(R.id.tvDetailTitle);
        TextView tvDetailPrice = findViewById(R.id.tvDetailPrice);
        TextView tvDetailDesc = findViewById(R.id.tvDetailDesc);
        TextView tvDetailCategory = findViewById(R.id.tvDetailCategory);
        TextView tvBack = findViewById(R.id.tvBack);

        btnDelete = findViewById(R.id.btnDelete);
        progressBar = findViewById(R.id.progressBar);

        Intent intent = getIntent();
        if (intent != null) {
            currentPosition = intent.getIntExtra("position", -1);
            tvDetailTitle.setText(intent.getStringExtra("title"));
            tvDetailPrice.setText("$" + intent.getStringExtra("price") + " MXN");
            tvDetailDesc.setText(intent.getStringExtra("desc"));
            tvDetailCategory.setText(intent.getStringExtra("category"));
            Glide.with(this).load(intent.getStringExtra("imageUrl")).centerCrop().into(ivDetailImage);
        }

        tvBack.setOnClickListener(v -> finish());
        btnDelete.setOnClickListener(v -> showDeleteDialog());
    }

    private void showDeleteDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_delete);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        Button btnCancel = dialog.findViewById(R.id.btnCancelDelete);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirmDelete);

        if (btnCancel != null) btnCancel.setOnClickListener(v -> dialog.dismiss());

        if (btnConfirm != null) {
            btnConfirm.setOnClickListener(v -> {
                btnConfirm.setEnabled(false);
                btnConfirm.setText("Borrando...");

                btnDelete.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);

                Executors.newSingleThreadExecutor().execute(() -> {
                    try {
                        URL url = new URL("https://fakestoreapi.com/products/1");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("DELETE");
                        conn.getResponseCode();

                        new Handler(Looper.getMainLooper()).post(() -> {
                            dialog.dismiss();
                            Toast.makeText(ProductDetailActivity.this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("positionToDelete", currentPosition);
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        });
                    } catch (Exception e) {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            dialog.dismiss();
                            btnConfirm.setEnabled(true);
                            btnConfirm.setText("Eliminar");
                            btnDelete.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ProductDetailActivity.this, "Error de red", Toast.LENGTH_LONG).show();
                        });
                    }
                });
            });
        }
        dialog.show();
    }
}