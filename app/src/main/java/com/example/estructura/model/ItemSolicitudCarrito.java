package com.example.estructura.model;
/**
 * La Fake Store API espera que cada producto dentro de un carrito
 * venga como { "productId": X, "quantity": Y }.
 *
 * IMPORTANTE: los nombres de estos atributos (productId, quantity)
 * deben quedarse en inglés y EXACTAMENTE así, porque Gson los usa
 * para armar el JSON que se envía por la red. Si los renombramos,
 * la API ya no los reconocería.
 */

public class ItemSolicitudCarrito {
    private int productId;
    private int quantity;

    public ItemSolicitudCarrito(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
