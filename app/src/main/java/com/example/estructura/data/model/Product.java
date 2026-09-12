package com.example.estructura.data.model;

import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("price")
    private double price;

    @SerializedName("description")
    private String description;

    @SerializedName("image")
    private String image;

    @SerializedName("category")
    private String category;

    public Product() {
        // Constructor vacío utilizado por Gson.
    }

    public Product(
            int id,
            String title,
            double price,
            String description,
            String image,
            String category
    ) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.image = image;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getCategory() {
        return category;
    }
}