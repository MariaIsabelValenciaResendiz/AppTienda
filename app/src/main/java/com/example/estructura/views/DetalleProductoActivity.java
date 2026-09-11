package com.example.estructura.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.estructura.R;
import com.example.estructura.data.GestorCarritoLocal;
import com.example.estructura.model.ArticuloCarrito;
import com.example.estructura.model.ItemSolicitudCarrito;
import com.example.estructura.model.RespuestaCarrito;
import com.example.estructura.model.SolicitudCarrito;
import com.example.estructura.network.ApiFakeStore;
import com.example.estructura.network.ClienteApi;
import com.example.estructura.utils.FormateadorPrecio;
import com.example.estructura.utils.SesionUsuario;

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
 *  US09 - Añadir artículos al carrito personal
 * ============================================================
 *
 * ¿Quién abre esta pantalla?
 * Normalmente la abre la pantalla de Catálogo o la de Detalle general
 * del producto (historias US03/US05, que programa otro compañero de
 * equipo) usando un Intent y enviando los datos del producto como
 * "extras". Por eso esta clase NO depende de la clase Producto que
 * ellos hayan creado: solo pide los datos sueltos que necesita.
 *
 * ¿Qué hace esta pantalla?
 * 1. Muestra la información del producto recibido.
 * 2. Permite elegir una cantidad con los botones + / -.
 * 3. Al presionar "Agregar al carrito":
 *      a) Guarda (o suma) el artículo en el carrito LOCAL del
 *         dispositivo (GestorCarritoLocal), que es lo que en verdad
 *         sostiene el estado del carrito.
 *      b) Envía una petición POST /carts a la Fake Store API, solo
 *         para practicar el consumo de la red (la API no guarda nada
 *         de verdad).
 *      c) Muestra un mensaje de confirmación con acceso directo al
 *         carrito.
 * 4. Si el usuario autenticado tiene el perfil "Auditor", oculta por
 *    completo los controles de compra (Escenario 3 de la historia).
 */

public class DetalleProductoActivity extends AppCompatActivity {
    // Nombres de los "extras" que debe traer el Intent que abre esta
    // pantalla. Cualquier compañero que quiera navegar hacia aquí debe
    // usar estas mismas constantes.
    public static final String EXTRA_ID_PRODUCTO = "extra_id_producto";
    public static final String EXTRA_TITULO = "extra_titulo";
    public static final String EXTRA_PRECIO = "extra_precio";
    public static final String EXTRA_DESCRIPCION = "extra_descripcion";
    public static final String EXTRA_CATEGORIA = "extra_categoria";
    public static final String EXTRA_IMAGEN_URL = "extra_imagen_url";

    private ImageView imagenProducto;
    private TextView textoCategoria;
    private TextView textoTitulo;
    private TextView textoPrecio;
    private TextView textoDescripcion;
    private TextView textoCantidad;
    private TextView textoSubtotal;
    private TextView textoConfirmacion;
    private ImageButton botonSumar;
    private ImageButton botonRestar;
    private Button botonAgregarCarrito;
    private Button botonVerCarrito;
    private View tarjetaConfirmacion;
    private View contenedorCantidad;

    // Datos del producto que llegan por el Intent.
    private int idProducto;
    private String titulo;
    private double precio;
    private String imagenUrl;
    private String categoria;

    // Cantidad que el Cliente va eligiendo con los botones + / -.
    private int cantidadSeleccionada = 1;

    private GestorCarritoLocal gestorCarrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);

        // obtenerInstancia(this) devuelve siempre el MISMO gestor de
        // carrito que usa toda la app (patrón singleton).
        gestorCarrito = GestorCarritoLocal.obtenerInstancia(this);

        vincularVistas();
        leerDatosDelIntent();
        mostrarInformacionProducto();
        configurarBotonesDeCantidad();
        configurarBotonesDeAccion();
        aplicarRestriccionPorRol();
    }

    /** findViewById conecta cada variable Java con su vista del XML. */
    private void vincularVistas() {
        imagenProducto = findViewById(R.id.imagenProductoDetalle);
        textoCategoria = findViewById(R.id.textoCategoriaDetalle);
        textoTitulo = findViewById(R.id.textoTituloDetalle);
        textoPrecio = findViewById(R.id.textoPrecioDetalle);
        textoDescripcion = findViewById(R.id.textoDescripcionDetalle);
        textoCantidad = findViewById(R.id.textoCantidadDetalle);
        textoSubtotal = findViewById(R.id.textoSubtotalDetalle);
        botonSumar = findViewById(R.id.botonSumarDetalle);
        botonRestar = findViewById(R.id.botonRestarDetalle);
        botonAgregarCarrito = findViewById(R.id.botonAgregarCarrito);
        tarjetaConfirmacion = findViewById(R.id.tarjetaConfirmacion);
        textoConfirmacion = findViewById(R.id.textoConfirmacion);
        botonVerCarrito = findViewById(R.id.botonVerCarrito);
        contenedorCantidad = findViewById(R.id.contenedorCantidad);

        findViewById(R.id.botonRegresarDetalle).setOnClickListener(v -> finish());
    }

    /** getIntent() recupera el Intent con el que se abrió esta Activity. */
    private void leerDatosDelIntent() {
        Intent intent = getIntent();
        idProducto = intent.getIntExtra(EXTRA_ID_PRODUCTO, -1);
        titulo = intent.getStringExtra(EXTRA_TITULO);
        precio = intent.getDoubleExtra(EXTRA_PRECIO, 0.0);
        categoria = intent.getStringExtra(EXTRA_CATEGORIA);
        imagenUrl = intent.getStringExtra(EXTRA_IMAGEN_URL);
        String descripcion = intent.getStringExtra(EXTRA_DESCRIPCION);

        if (descripcion != null) {
            textoDescripcion.setText(descripcion);
        }
    }

    private void mostrarInformacionProducto() {
        textoCategoria.setText(categoria);
        textoTitulo.setText(titulo);
        textoPrecio.setText(FormateadorPrecio.formatear(precio));

        Glide.with(this)
                .load(imagenUrl)
                .placeholder(R.drawable.fondo_tarjeta_redondeada)
                .into(imagenProducto);

        actualizarSubtotalMostrado();
    }

    private void configurarBotonesDeCantidad() {
        botonSumar.setOnClickListener(v -> {
            cantidadSeleccionada++;
            actualizarSubtotalMostrado();
        });

        botonRestar.setOnClickListener(v -> {
            // La cantidad mínima para comprar es 1 (no tiene sentido
            // "agregar cero unidades").
            if (cantidadSeleccionada > 1) {
                cantidadSeleccionada--;
                actualizarSubtotalMostrado();
            }
        });
    }

    private void actualizarSubtotalMostrado() {
        textoCantidad.setText(String.valueOf(cantidadSeleccionada));
        double subtotal = precio * cantidadSeleccionada;
        textoSubtotal.setText(getString(R.string.formato_subtotal,
                FormateadorPrecio.formatear(subtotal)));
    }

    private void configurarBotonesDeAccion() {
        botonAgregarCarrito.setOnClickListener(v -> agregarProductoAlCarrito());

        botonVerCarrito.setOnClickListener(v -> {
            startActivity(new Intent(this, CarritoActivity.class));
            finish();
        });
    }

    /**
     * Escenario 1 ("Agregado exitoso") y Escenario 2 ("Producto
     * previamente existente") de US09.
     */
    private void agregarProductoAlCarrito() {
        if (cantidadSeleccionada <= 0) {
            Toast.makeText(this, R.string.error_cantidad_invalida, Toast.LENGTH_SHORT).show();
            return;
        }

        ArticuloCarrito nuevoArticulo = new ArticuloCarrito(
                idProducto, titulo, precio, imagenUrl, categoria, cantidadSeleccionada);

        // 1) Fuente de verdad local: si el producto ya existía, el
        //    propio GestorCarritoLocal se encarga de sumar cantidades
        //    en vez de duplicar el renglón (Escenario 2).
        gestorCarrito.agregarProducto(nuevoArticulo);

        // 2) Práctica de red (POST /carts). No es indispensable para
        //    que el carrito funcione, pero la historia lo pide para
        //    practicar el consumo de la API.
        enviarPeticionPostAlServidor();

        mostrarConfirmacionVisual();
    }

    private void enviarPeticionPostAlServidor() {
        ApiFakeStore servicio = ClienteApi.obtenerServicio();

        List<ItemSolicitudCarrito> productos = new ArrayList<>();
        productos.add(new ItemSolicitudCarrito(idProducto, cantidadSeleccionada));

        String fechaActual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());

        // El "userId" es un valor de ejemplo (1). En la versión final
        // del equipo, este dato debería tomarse del usuario que inició
        // sesión (historia US01).
        SolicitudCarrito solicitud = new SolicitudCarrito(1, fechaActual, productos);

        // enqueue() hace la petición EN SEGUNDO PLANO (sin congelar la
        // pantalla) y avisa el resultado en onResponse/onFailure.
        servicio.agregarCarrito(solicitud).enqueue(new Callback<RespuestaCarrito>() {
            @Override
            public void onResponse(Call<RespuestaCarrito> call, Response<RespuestaCarrito> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(DetalleProductoActivity.this,
                            R.string.aviso_error_red_pero_guardado_local,
                            Toast.LENGTH_SHORT).show();
                }
                // Si fue exitoso no hacemos nada más: el carrito local
                // ya se actualizó antes de llamar a este método.
            }

            @Override
            public void onFailure(Call<RespuestaCarrito> call, Throwable t) {
                Toast.makeText(DetalleProductoActivity.this,
                        R.string.aviso_sin_conexion_pero_guardado_local,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarConfirmacionVisual() {
        tarjetaConfirmacion.setVisibility(View.VISIBLE);
        String mensaje = getResources().getQuantityString(
                R.plurals.unidades_anadidas, cantidadSeleccionada, cantidadSeleccionada);
        textoConfirmacion.setText(mensaje);
    }

    /**
     * Escenario 3 de US09: para el perfil Auditor se oculta POR
     * COMPLETO el botón de compra (no basta con deshabilitarlo), para
     * que su experiencia sea de solo lectura.
     */
    private void aplicarRestriccionPorRol() {
        if (SesionUsuario.esAuditor(this)) {
            botonAgregarCarrito.setVisibility(View.GONE);
            contenedorCantidad.setVisibility(View.GONE);
        }
    }
}
