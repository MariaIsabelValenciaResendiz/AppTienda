package com.example.estructura.domain.validation;

public final class ProductValidationResult {

    private final boolean titleValid;
    private final boolean priceValid;
    private final boolean descriptionValid;
    private final boolean imageUrlValid;
    private final boolean categoryValid;
    private final double validatedPrice;

    ProductValidationResult(
            boolean titleValid,
            boolean priceValid,
            boolean descriptionValid,
            boolean imageUrlValid,
            boolean categoryValid,
            double validatedPrice
    ) {
        this.titleValid = titleValid;
        this.priceValid = priceValid;
        this.descriptionValid = descriptionValid;
        this.imageUrlValid = imageUrlValid;
        this.categoryValid = categoryValid;
        this.validatedPrice = validatedPrice;
    }

    public boolean isValid() {
        return titleValid
                && priceValid
                && descriptionValid
                && imageUrlValid
                && categoryValid;
    }

    public boolean hasTitleError() {
        return !titleValid;
    }

    public boolean hasPriceError() {
        return !priceValid;
    }

    public boolean hasDescriptionError() {
        return !descriptionValid;
    }

    public boolean hasImageUrlError() {
        return !imageUrlValid;
    }

    public boolean hasCategoryError() {
        return !categoryValid;
    }

    public double getValidatedPrice() {
        if (!priceValid) {
            throw new IllegalStateException(
                    "No se puede obtener un precio inválido."
            );
        }

        return validatedPrice;
    }
}