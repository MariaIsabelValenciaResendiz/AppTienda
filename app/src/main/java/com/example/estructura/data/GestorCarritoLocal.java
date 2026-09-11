package com.example.estructura.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.estructura.model.ArticuloCarrito;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * ESTA es la clase más importante de las historias US09 y US10.
 *
 * Las notas de negocio de ambas historias piden explícitamente un
 * "manejador de estado" (State Management / SharedPreferences /
 * SQLite) porque la Fake Store API NO guarda el carrito de verdad en
 * ningún servidor. Aquí usamos SharedPreferences (un almacenamiento
 * clave-valor sencillo que ya trae Android) guardando la lista de
 * artículos convertida a texto JSON con Gson.
 *
 * Es un "singleton": toda la app comparte una sola instancia, así que
 * la pantalla de Detalle (US09) y la pantalla de Carrito (US10) ven
 * siempre los mismos datos.
 */

public class GestorCarritoLocal {
    private static final String ARCHIVO_PREFERENCIAS = "preferencias_carrito_nova";
    private static final String CLAVE_ARTICULOS = "clave_articulos_carrito";

    private static GestorCarritoLocal instancia;

    private final SharedPreferences preferencias;
    private final Gson gson;
    private final List<ArticuloCarrito> articulos;

    private GestorCarritoLocal(Context contexto) {
        // getApplicationContext() evita quedarnos con la referencia de
        // una Activity (que puede destruirse) y usamos el contexto de
        // toda la aplicación, que vive mientras la app esté abierta.
        preferencias = contexto.getApplicationContext()
                .getSharedPreferences(ARCHIVO_PREFERENCIAS, Context.MODE_PRIVATE);
        gson = new Gson();
        articulos = cargarDesdeAlmacenamiento();
    }

    public static synchronized GestorCarritoLocal obtenerInstancia(Context contexto) {
        if (instancia == null) {
            instancia = new GestorCarritoLocal(contexto);
        }
        return instancia;
    }

    public synchronized List<ArticuloCarrito> obtenerArticulos() {
        return articulos;
    }

    /**
     * US09 - Escenario 1 y 2: agrega un producto nuevo, o si ya existía
     * en el carrito, SUMA la cantidad en lugar de crear un renglón
     * duplicado.
     */
    public synchronized void agregarProducto(ArticuloCarrito nuevoArticulo) {
        for (ArticuloCarrito existente : articulos) {
            if (existente.getIdProducto() == nuevoArticulo.getIdProducto()) {
                existente.setCantidad(existente.getCantidad() + nuevoArticulo.getCantidad());
                guardarEnAlmacenamiento();
                return;
            }
        }
        articulos.add(nuevoArticulo);
        guardarEnAlmacenamiento();
    }

    /**
     * US10 - Escenario 1: cambia la cantidad de un artículo ya
     * existente. Si la nueva cantidad es cero o menor, se comporta
     * igual que eliminarlo (Escenario 2).
     */
    public synchronized void actualizarCantidad(int idProducto, int nuevaCantidad) {
        if (nuevaCantidad <= 0) {
            eliminarProducto(idProducto);
            return;
        }
        for (ArticuloCarrito articulo : articulos) {
            if (articulo.getIdProducto() == idProducto) {
                articulo.setCantidad(nuevaCantidad);
                break;
            }
        }
        guardarEnAlmacenamiento();
    }

    /** US10 - Escenario 2: elimina por completo un artículo del carrito. */
    public synchronized void eliminarProducto(int idProducto) {
        ArticuloCarrito aEliminar = null;
        for (ArticuloCarrito articulo : articulos) {
            if (articulo.getIdProducto() == idProducto) {
                aEliminar = articulo;
                break;
            }
        }
        if (aEliminar != null) {
            articulos.remove(aEliminar);
            guardarEnAlmacenamiento();
        }
    }

    /** Suma los subtotales de todos los artículos, redondeado a 2 decimales. */
    public synchronized double calcularTotal() {
        double total = 0;
        for (ArticuloCarrito articulo : articulos) {
            total += articulo.calcularSubtotal();
        }
        return Math.round(total * 100.0) / 100.0;
    }

    public synchronized int contarUnidadesTotales() {
        int total = 0;
        for (ArticuloCarrito articulo : articulos) {
            total += articulo.getCantidad();
        }
        return total;
    }

    public synchronized boolean estaVacio() {
        return articulos.isEmpty();
    }

    /**
     * Útil para cuando la persona responsable de US02 (cerrar sesión)
     * necesite borrar el carrito local al salir de la cuenta.
     */
    public synchronized void vaciarCarrito() {
        articulos.clear();
        guardarEnAlmacenamiento();
    }

    private void guardarEnAlmacenamiento() {
        String json = gson.toJson(articulos);
        preferencias.edit().putString(CLAVE_ARTICULOS, json).apply();
    }

    private List<ArticuloCarrito> cargarDesdeAlmacenamiento() {
        String json = preferencias.getString(CLAVE_ARTICULOS, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type tipoLista = new TypeToken<ArrayList<ArticuloCarrito>>() {
        }.getType();
        List<ArticuloCarrito> listaGuardada = gson.fromJson(json, tipoLista);
        return listaGuardada != null ? listaGuardada : new ArrayList<>();
    }
}
