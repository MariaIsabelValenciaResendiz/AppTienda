package com.example.estructura.domain.validation;

import java.net.URI;
import java.net.URISyntaxException;

public class ProductFormValidator {

    public ProductValidationResult validate(
            String title,
            String priceText,
            String description,
            String imageUrl,
            String category
    ) {
        boolean titleValid = hasText(title);
        boolean descriptionValid = hasText(description);
        boolean categoryValid = hasText(category);
        boolean imageUrlValid = isValidHttpsUrl(imageUrl);

        Double parsedPrice = parsePositivePrice(priceText);
        boolean priceValid = parsedPrice != null;

        return new ProductValidationResult(
                titleValid,
                priceValid,
                descriptionValid,
                imageUrlValid,
                categoryValid,
                parsedPrice != null ? parsedPrice : 0.0
        );
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private Double parsePositivePrice(String priceText) {
        if (!hasText(priceText)) {
            return null;
        }

        String normalizedPrice = priceText
                .trim()
                .replace(',', '.');

        try {
            double price = Double.parseDouble(normalizedPrice);

            if (!Double.isFinite(price) || price <= 0) {
                return null;
            }

            return price;
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private boolean isValidHttpsUrl(String imageUrl) {
        if (!hasText(imageUrl)) {
            return false;
        }

        try {
            URI uri = new URI(imageUrl.trim());

            return "https".equalsIgnoreCase(uri.getScheme())
                    && uri.getHost() != null
                    && !uri.getHost().trim().isEmpty();
        } catch (URISyntaxException exception) {
            return false;
        }
    }
}