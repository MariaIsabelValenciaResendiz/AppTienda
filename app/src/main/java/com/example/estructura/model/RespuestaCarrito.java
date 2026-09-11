package com.example.estructura.model;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Representa lo que la Fake Store API responde después de un
 * POST /carts o PUT /carts/{id}. Solo se usa para leer la respuesta
 * de práctica; el carrito real que ve el usuario vive en
 * GestorCarritoLocal.
 */

public class RespuestaCarrito {
    private int id;
    private int userId;
    private String date;
    private List<ItemSolicitudCarrito> products;

    @SerializedName("__v")
    private int version;

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    public List<ItemSolicitudCarrito> getProducts() {
        return products;
    }
}
