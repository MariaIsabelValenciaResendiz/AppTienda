package com.example.hu_8;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Product> productList;
    private ProductAdapter adapter;
    private TextView tvCount;

    private final ActivityResultLauncher<Intent> detailLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    int pos = result.getData().getIntExtra("positionToDelete", -1);
                    if (pos != -1) {
                        productList.remove(pos);
                        adapter.notifyItemRemoved(pos);
                        adapter.notifyItemRangeChanged(pos, productList.size());
                        updateCounter();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvCount = findViewById(R.id.tvCount);
        // 6 artículos con URLs 100% reales de la FakeStore API
        productList = new ArrayList<>();

        productList.add(new Product("Disco Duro WD 2TB", "1200.00", "Almacenamiento veloz para gamers y creadores.", "Cómputo", "https://i5.walmartimages.com/asr/f51bee86-b595-436c-bfff-2e75e91264ee_1.00999206758f2a71fa95cf2a1107841a.jpeg?odnHeight=612&odnWidth=612&odnBg=FFFFFF"));

        productList.add(new Product("Mochila Nova Fjallraven", "550.00", "Mochila resistente para laptops de 15 pulgadas.", "Accesorios", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS7hGrkEGN6N8VRzoAy4f21n9fmzvoUlX4B7-0KkEZoLsAeW66CrxQC3LeP&s=10"));

        productList.add(new Product("Monitor Samsung 49\"", "15400.00", "Monitor curvo ultra ancho a 144Hz.", "Pantallas", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRpR-6o-WH1-bFH94GEVhU1kG0ybafGoLY-K9Z6CgPc4AwxrXxH196uFek&s=10"));

        productList.add(new Product("Monitor Acer 21\"", "2800.00", "Pantalla Full HD ideal para oficina.", "Pantallas", "https://m.media-amazon.com/images/I/81QpkIctqPL.jpg"));

        productList.add(new Product("Memoria SanDisk", "450.00", "Tarjeta de memoria interna de alta velocidad.", "Electrónica", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQi1eNkAeiLp--PfA25dr0WRlTbuN3XDKJodGOaftqr82pIE_XIOz1d3Nps&s=10"));

        productList.add(new Product("Disco Duro Silicon", "950.00", "Disco duro externo con protección contra caídas.", "Cómputo", "https://m.media-amazon.com/images/I/71rVAobI+tL.jpg"));
        updateCounter();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ProductAdapter(productList, position -> {
            Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
            Product p = productList.get(position);
            intent.putExtra("position", position);
            intent.putExtra("title", p.title);
            intent.putExtra("price", p.price);
            intent.putExtra("desc", p.desc);
            intent.putExtra("category", p.category);
            intent.putExtra("imageUrl", p.imageUrl);
            detailLauncher.launch(intent);
        });

        recyclerView.setAdapter(adapter);
    }

    private void updateCounter() {
        tvCount.setText(productList.size() + " artículos en inventario");
    }
}