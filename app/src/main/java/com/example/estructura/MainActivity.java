package com.example.estructura;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.estructura.callback.CategoriaCallback;
import com.example.estructura.callback.ProductoCallback;
import com.example.estructura.model.Producto;
import com.example.estructura.repository.ProductoRepository;
import com.example.estructura.repository.ProductoRepositoryImpl;
import com.example.estructura.view.CategoriaBottomSheet;
import com.example.estructura.view.ProductoAdapter;
import com.example.estructura.modelos.RolUsuario;
import com.example.estructura.pantallas.LoginActivity;
import com.example.estructura.pantallas.PrincipalActivity;
import com.example.estructura.utilidades.GestorSesion;
import com.example.estructura.productcreate.CreateProductActivity;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvProductos;
    private ProgressBar progressBar;
    private LinearLayout llError;
    private TextView btnReintentar;

    private LinearLayout rowSinFiltro;
    private LinearLayout rowConFiltro;
    private LinearLayout rowContador;

    private TextView btnCategorias;
    private TextView btnCambiarCategoria;
    private TextView txtCategoriaActiva;
    private TextView txtContadorResultados;
    private TextView txtVerTodos;
    private TextView txtRolUsuario;
    private TextView btnMiCuenta;

    private ProductoAdapter adapter;
    private ProductoRepository repository;
    private GestorSesion gestorSesion;

    private List<String> listaCategorias;
    private MaterialButton btnNuevoProducto;
    private String categoriaActual = "todos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gestorSesion = GestorSesion.obtenerInstancia(this);

        if (!gestorSesion.haySesionActiva()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        inicializarVistas();
        RolUsuario rolActual = gestorSesion.obtenerRol();
        txtRolUsuario.setText(obtenerNombreRol(rolActual));

        btnNuevoProducto.setVisibility(
                rolActual == RolUsuario.ADMINISTRADOR
                        ? View.VISIBLE
                        : View.GONE
        );

        btnNuevoProducto.setOnClickListener(v ->
                startActivity(
                        CreateProductActivity.createIntent(MainActivity.this)
                )
        );

        btnMiCuenta.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PrincipalActivity.class);
            startActivity(intent);
        });

        configurarRecyclerView();

        repository = new ProductoRepositoryImpl();

        cargarProductos();
        cargarCategorias();

        btnReintentar.setOnClickListener(v -> cargarProductos());

        btnCategorias.setOnClickListener(v -> abrirSelectorCategorias());
        btnCambiarCategoria.setOnClickListener(v -> abrirSelectorCategorias());

        txtVerTodos.setOnClickListener(v -> aplicarFiltro("todos"));
    }

    private void inicializarVistas() {
        btnNuevoProducto = findViewById(R.id.btnNuevoProducto);
        rvProductos = findViewById(R.id.rvProductos);
        progressBar = findViewById(R.id.progressBar);
        llError = findViewById(R.id.llError);
        btnReintentar = findViewById(R.id.btnReintentar);

        rowSinFiltro = findViewById(R.id.rowSinFiltro);
        rowConFiltro = findViewById(R.id.rowConFiltro);
        rowContador = findViewById(R.id.rowContador);

        btnCategorias = findViewById(R.id.btnCategorias);
        btnCambiarCategoria = findViewById(R.id.btnCambiarCategoria);
        txtCategoriaActiva = findViewById(R.id.txtCategoriaActiva);
        txtContadorResultados = findViewById(R.id.txtContadorResultados);
        txtVerTodos = findViewById(R.id.txtVerTodos);
        txtRolUsuario = findViewById(R.id.txtRolUsuario);
        btnMiCuenta = findViewById(R.id.btnMiCuenta);
    }

    private void configurarRecyclerView() {
        adapter = new ProductoAdapter();
        rvProductos.setLayoutManager(new GridLayoutManager(this, 2));
        rvProductos.setAdapter(adapter);
    }

    // ===== Carga inicial del catálogo completo =====
    private void cargarProductos() {
        mostrarCargando();

        repository.obtenerProductos(new ProductoCallback() {
            @Override
            public void onSuccess(List<Producto> productos) {
                mostrarCatalogo();
                adapter.actualizarLista(productos);
            }

            @Override
            public void onError(String mensaje) {
                mostrarError(mensaje);
            }
        });
    }

    // ===== Carga de categorías (para llenar el bottom sheet) =====
    private void cargarCategorias() {
        repository.obtenerCategorias(new CategoriaCallback() {
            @Override
            public void onSuccess(List<String> categorias) {
                listaCategorias = categorias;
            }

            @Override
            public void onError(String mensaje) {
                Toast.makeText(MainActivity.this, "No se pudieron cargar las categorías", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void abrirSelectorCategorias() {
        if (listaCategorias == null) {
            Toast.makeText(this, "Cargando categorías, intenta de nuevo en un momento", Toast.LENGTH_SHORT).show();
            return;
        }

        CategoriaBottomSheet.mostrar(this, listaCategorias, categoriaActual, categoria -> {
            aplicarFiltro(categoria);
        });
    }

    // ===== Aplicar filtro seleccionado (Escenario 2 y 3 de US04) =====
    private void aplicarFiltro(String categoria) {
        categoriaActual = categoria;

        if ("todos".equals(categoria)) {
            actualizarHeaderSinFiltro();
            cargarProductos();
            return;
        }

        mostrarCargando();

        repository.obtenerProductosPorCategoria(categoria, new ProductoCallback() {
            @Override
            public void onSuccess(List<Producto> productos) {
                mostrarCatalogo();
                adapter.actualizarLista(productos);
                actualizarHeaderConFiltro(categoria, productos.size());
            }

            @Override
            public void onError(String mensaje) {
                mostrarError(mensaje);
            }
        });
    }

    private void actualizarHeaderSinFiltro() {
        rowSinFiltro.setVisibility(View.VISIBLE);
        rowConFiltro.setVisibility(View.GONE);
        rowContador.setVisibility(View.GONE);
    }

    private void actualizarHeaderConFiltro(String categoria, int cantidadResultados) {
        rowSinFiltro.setVisibility(View.GONE);
        rowConFiltro.setVisibility(View.VISIBLE);
        rowContador.setVisibility(View.VISIBLE);

        txtCategoriaActiva.setText(nombreLegible(categoria));

        String texto = cantidadResultados == 1
                ? "1 resultado"
                : cantidadResultados + " resultados";
        txtContadorResultados.setText(texto);
    }

    private String nombreLegible(String categoria) {
        switch (categoria) {
            case "electronics":
                return "Electrónica";
            case "men's clothing":
                return "Ropa de hombre";
            case "women's clothing":
                return "Ropa de mujer";
            case "jewelery":
                return "Joyería";
            default:
                return categoria;
        }
    }

    // ===== Estados visuales compartidos =====

    private String obtenerNombreRol(RolUsuario rol) {
        switch (rol) {
            case ADMINISTRADOR:
                return "Administrador";
            case AUDITOR:
                return "Auditor";
            case CLIENTE:
            default:
                return "Cliente";
        }
    }
    private void mostrarCargando() {
        progressBar.setVisibility(View.VISIBLE);
        rvProductos.setVisibility(View.GONE);
        llError.setVisibility(View.GONE);
    }

    private void mostrarCatalogo() {
        progressBar.setVisibility(View.GONE);
        rvProductos.setVisibility(View.VISIBLE);
        llError.setVisibility(View.GONE);
    }

    private void mostrarError(String mensaje) {
        progressBar.setVisibility(View.GONE);
        rvProductos.setVisibility(View.GONE);
        llError.setVisibility(View.VISIBLE);

        TextView txtError = findViewById(R.id.txtError);
        txtError.setText(mensaje);
    }
}