package com.example.estructura;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.estructura.views.DetalleProductoActivity;

public class MainActivity extends AppCompatActivity {

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

        // ---- CÓDIGO TEMPORAL SOLO PARA PROBAR LA PANTALLA DE DETALLE ----
        // Borrar este bloque antes de subir a Git; la navegación real
        // la hará quien programe el catálogo (US03/US05).
        Intent intent = new Intent(this, DetalleProductoActivity.class);
        intent.putExtra(DetalleProductoActivity.EXTRA_ID_PRODUCTO, 1);
        intent.putExtra(DetalleProductoActivity.EXTRA_TITULO, "Audífonos Aura");
        intent.putExtra(DetalleProductoActivity.EXTRA_PRECIO, 899.00);
        intent.putExtra(DetalleProductoActivity.EXTRA_CATEGORIA, "Electrónica");
        intent.putExtra(DetalleProductoActivity.EXTRA_DESCRIPCION,
                "Sonido envolvente en un diseño ligero.");
        intent.putExtra(DetalleProductoActivity.EXTRA_IMAGEN_URL,
                "https://fakestoreapi.com/img/81QpkIctqPL._AC_SX679_.jpg");
        startActivity(intent);
        // ---- FIN DEL CÓDIGO TEMPORAL ----
    }
}