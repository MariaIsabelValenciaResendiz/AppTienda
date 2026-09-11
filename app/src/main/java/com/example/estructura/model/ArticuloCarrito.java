package com.example.estructura.model;
/**
 * Representa UN renglón dentro del carrito de compras del Cliente.
 *
 * No es el mismo objeto que "Producto" del catálogo: aquí solo
 * guardamos lo mínimo que la pantalla del carrito necesita mostrar y
 * calcular (US10), y lo guardamos en el dispositivo (SharedPreferences)
 * porque la Fake Store API no persiste el carrito de verdad.
 */

public class ArticuloCarrito {
    private int idProducto;
    private String titulo;
    private double precioUnitario;
    private String imagenUrl;
    private String categoria;
    private int cantidad;

    // Gson (la librería que convierte objetos Java <-> JSON) necesita
    // un constructor vacío para poder reconstruir el objeto al leerlo
    // de SharedPreferences.
    public ArticuloCarrito() {
    }

    public ArticuloCarrito(int idProducto, String titulo, double precioUnitario,
                           String imagenUrl, String categoria, int cantidad) {
        this.idProducto = idProducto;
        this.titulo = titulo;
        this.precioUnitario = precioUnitario;
        this.imagenUrl = imagenUrl;
        this.categoria = categoria;
        this.cantidad = cantidad;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Regla de negocio de US10: los precios deben mostrarse redondeados
     * a dos decimales. Multiplicamos precio x cantidad y redondeamos.
     */
    public double calcularSubtotal() {
        double subtotal = precioUnitario * cantidad;
        return Math.round(subtotal * 100.0) / 100.0;
    }
}
