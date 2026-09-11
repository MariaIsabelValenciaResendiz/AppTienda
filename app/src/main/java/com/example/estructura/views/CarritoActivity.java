package com.example.estructura.views;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.estructura.R;
import com.example.estructura.adapters.AdaptadorCarrito;
import com.example.estructura.data.GestorCarritoLocal;
import com.example.estructura.model.ArticuloCarrito;
import com.example.estructura.model.ItemSolicitudCarrito;
import com.example.estructura.model.RespuestaCarrito;
import com.example.estructura.model.SolicitudCarrito;
import com.example.estructura.network.ApiFakeStore;
import com.example.estructura.network.ClienteApi;
import com.example.estructura.utils.FormateadorPrecio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ============================================================
 *  US10 - Visualizar, modificar o eliminar artículos del carrito
 * ============================================================
 *
 * Esta pantalla muestra todos los artículos que el Cliente ha ido
 * agregando (en la pantalla de US09) y le permite:
 *   - Cambiar la cantidad con los botones + / - (Escenario 1).
 *   - Eliminar un artículo, o llegar a cantidad cero (Escenario 2).
 *   - Ver un estado especial cuando el carrito queda vacío
 *     (Escenario 3).
 *
 * La clase implementa AdaptadorCarrito.EscuchadorCarrito: eso
 * significa que ESTA Activity es quien decide qué pasa cuando se
 * toca "+", "-" o el ícono de eliminar dentro de cada renglón de la
 * lista.
 */

public class CarritoActivity extends AppCompatActivity implements AdaptadorCarrito.EscuchadorCarrito {
    private RecyclerView recyclerCarrito;
    private View vistaCarritoVacio;
    private View vistaCarritoConProductos;
    private TextView textoTotalProductos;
    private TextView textoCantidadArticulos;
    private Button botonProcederPago;
    private Button botonExplorarCatalogo;

    private GestorCarritoLocal gestorCarrito;
    private AdaptadorCarrito adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        gestorCarrito = GestorCarritoLocal.obtenerInstancia(this);

        vincularVistas();
        configurarLista();

        findViewById(R.id.botonRegresarCarrito).setOnClickListener(v -> finish());

        // "Explorar el catálogo" simplemente regresa a la pantalla
        // anterior (el catálogo, historia US03/US04).
        botonExplorarCatalogo.setOnClickListener(v -> finish());

        botonProcederPago.setOnClickListener(v -> Toast.makeText(this,
                R.string.aviso_simulacion_pago, Toast.LENGTH_LONG).show());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // onResume() se ejecuta cada vez que esta pantalla vuelve a
        // quedar visible (por ejemplo, si el usuario agrega otro
        // producto y regresa). Así el carrito siempre se ve
        // actualizado sin necesidad de recargar manualmente.
        actualizarPantalla();
    }

    private void vincularVistas() {
        recyclerCarrito = findViewById(R.id.recyclerCarrito);
        vistaCarritoVacio = findViewById(R.id.vistaCarritoVacio);
        vistaCarritoConProductos = findViewById(R.id.vistaCarritoConProductos);
        textoTotalProductos = findViewById(R.id.textoTotalProductos);
        textoCantidadArticulos = findViewById(R.id.textoCantidadArticulos);
        botonProcederPago = findViewById(R.id.botonProcederPago);
        botonExplorarCatalogo = findViewById(R.id.botonExplorarCatalogo);
    }

    private void configurarLista() {
        recyclerCarrito.setLayoutManager(new LinearLayoutManager(this));
        adaptador = new AdaptadorCarrito(gestorCarrito.obtenerArticulos(), this);
        recyclerCarrito.setAdapter(adaptador);
    }

    /**
     * Vuelve a leer el estado actual del carrito y refresca TODA la
     * pantalla: la lista, el total, el contador de artículos y qué
     * vista mostrar (carrito con productos o carrito vacío).
     */
    private void actualizarPantalla() {
        adaptador.notifyDataSetChanged();

        double total = gestorCarrito.calcularTotal();
        int totalUnidades = gestorCarrito.contarUnidadesTotales();

        textoTotalProductos.setText(FormateadorPrecio.formatear(total));
        textoCantidadArticulos.setText(getResources().getQuantityString(
                R.plurals.articulos_en_carrito, totalUnidades, totalUnidades));

        boolean carritoVacio = gestorCarrito.estaVacio();

        // Escenario 3 de US10: "Tu carrito está vacío, explora el
        // catálogo" + botón "Proceder al pago" desactivado.
        vistaCarritoVacio.setVisibility(carritoVacio ? View.VISIBLE : View.GONE);
        vistaCarritoConProductos.setVisibility(carritoVacio ? View.GONE : View.VISIBLE);
        botonProcederPago.setEnabled(!carritoVacio);
    }

    /**
     * Escenario 1 de US10: el usuario cambia la cantidad con los
     * controles de la interfaz. Primero actualizamos la memoria local
     * (para que la pantalla reaccione al instante) y después
     * disparamos la petición PUT de práctica.
     */
    @Override
    public void alCambiarCantidad(ArticuloCarrito articulo, int nuevaCantidad) {
        if (nuevaCantidad <= 0) {
            // Bajar la cantidad a cero equivale a eliminar el artículo.
            alEliminarArticulo(articulo);
            return;
        }

        gestorCarrito.actualizarCantidad(articulo.getIdProducto(), nuevaCantidad);
        actualizarPantalla();
        enviarPeticionPut(articulo.getIdProducto(), nuevaCantidad);
    }

    /**
     * Escenario 2 de US10: elimina el artículo de la memoria local,
     * recalcula el total y dispara la petición DELETE de práctica.
     */
    @Override
    public void alEliminarArticulo(ArticuloCarrito articulo) {
        gestorCarrito.eliminarProducto(articulo.getIdProducto());
        actualizarPantalla();
        enviarPeticionDelete(articulo.getIdProducto());
    }

    private void enviarPeticionPut(int idProducto, int nuevaCantidad) {
        ApiFakeStore servicio = ClienteApi.obtenerServicio();

        List<ItemSolicitudCarrito> productos = new ArrayList<>();
        productos.add(new ItemSolicitudCarrito(idProducto, nuevaCantidad));

        String fechaActual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());
        SolicitudCarrito solicitud = new SolicitudCarrito(1, fechaActual, productos);

        // NOTA ACADÉMICA: la Fake Store API identifica cada carrito con
        // un "id" propio de carrito (no de producto). Como esta app no
        // guarda carritos reales en el servidor, usamos el id del
        // producto como identificador de práctica para poder completar
        // el endpoint PUT /carts/{id} que pide la historia de usuario.
        servicio.actualizarCarrito(idProducto, solicitud).enqueue(new Callback<RespuestaCarrito>() {
            @Override
            public void onResponse(Call<RespuestaCarrito> call, Response<RespuestaCarrito> response) {
                // Práctica de red completada. El estado real ya vive
                // en GestorCarritoLocal, así que no hace falta hacer
                // nada más aquí.
            }

            @Override
            public void onFailure(Call<RespuestaCarrito> call, Throwable t) {
                // Sin conexión: no afecta al carrito local, que ya
                // quedó actualizado antes de llamar a este método.
            }
        });
    }

    private void enviarPeticionDelete(int idProducto) {
        ApiFakeStore servicio = ClienteApi.obtenerServicio();
        servicio.eliminarCarrito(idProducto).enqueue(new Callback<RespuestaCarrito>() {
            @Override
            public void onResponse(Call<RespuestaCarrito> call, Response<RespuestaCarrito> response) {
                // Práctica de red completada.
            }

            @Override
            public void onFailure(Call<RespuestaCarrito> call, Throwable t) {
                // Sin conexión: no afecta al carrito local.
            }
        });
    }
}
