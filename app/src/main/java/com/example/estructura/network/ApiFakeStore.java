package com.example.estructura.network;

import com.example.estructura.model.RespuestaCarrito;
import com.example.estructura.model.SolicitudCarrito;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Retrofit convierte esta interfaz en código real que hace peticiones
 * HTTP. Nosotros solo describimos QUÉ endpoint es y QUÉ datos entran
 * y salen; Retrofit se encarga de armar la petición de red.
 *
 * Documentación de referencia: https://fakestoreapi.com/docs
 */

public interface ApiFakeStore {
    /** US09 - Agregar un producto al carrito (POST /carts). */
    @POST("carts")
    Call<RespuestaCarrito> agregarCarrito(@Body SolicitudCarrito solicitud);

    /** US10 - Escenario 1: modificar cantidades (PUT /carts/{id}). */
    @PUT("carts/{id}")
    Call<RespuestaCarrito> actualizarCarrito(@Path("id") int id, @Body SolicitudCarrito solicitud);

    /** US10 - Escenario 2: eliminar un artículo (DELETE /carts/{id}). */
    @DELETE("carts/{id}")
    Call<RespuestaCarrito> eliminarCarrito(@Path("id") int id);
}
