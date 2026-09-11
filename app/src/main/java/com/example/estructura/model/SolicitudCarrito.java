package com.example.estructura.model;
import java.util.List;

/**
 * Cuerpo (body) que se envía en las peticiones POST /carts y
 * PUT /carts/{id} de la Fake Store API.
 *
 * Formato esperado por la API:
 * {
 *   "userId": 1,
 *   "date": "2026-09-10",
 *   "products": [ { "productId": 1, "quantity": 2 } ]
 * }
 */

public class SolicitudCarrito {
    private int userId;
    private String date;
    private List<ItemSolicitudCarrito> products;

    public SolicitudCarrito(int userId, String date, List<ItemSolicitudCarrito> products) {
        this.userId = userId;
        this.date = date;
        this.products = products;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ItemSolicitudCarrito> getProducts() {
        return products;
    }

    public void setProducts(List<ItemSolicitudCarrito> products) {
        this.products = products;
    }
}
