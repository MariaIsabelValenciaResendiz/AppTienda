package com.example.estructura.data.model;

import com.google.gson.annotations.SerializedName;

public class CreateProductRequest {

    @SerializedName("title")
    private final String title;

    @SerializedName("price")
    private final double price;

    @SerializedName("description")
    private final String description;

    @SerializedName("image")
    private final String image;

    @SerializedName("category")
    private final String category;

    public CreateProductRequest(
            String title,
            double price,
            String description,
            String image,
            String category
    ) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.image = image;
        this.category = category;
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