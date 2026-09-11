package com.example.estructura.model;

import com.google.gson.annotations.SerializedName;

public class Producto {

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String titulo;

    @SerializedName("price")
    private double precio;

    @SerializedName("description")
    private String descripcion;

    @SerializedName("category")
    private String categoria;

    @SerializedName("image")
    private String imagenUrl;

    @SerializedName("rating")
    private Rating rating;

    public Producto() {
        // Constructor vacío requerido por Gson
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public double getPrecio() {
        return precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public Rating getRating() {
        return rating;
    }

    public static class Rating {
        @SerializedName("rate")
        private double rate;

        @SerializedName("count")
        private int count;

        public double getRate() {
            return rate;
        }

        public int getCount() {
            return count;
        }
    }
}