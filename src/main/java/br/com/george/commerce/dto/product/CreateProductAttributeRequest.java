package br.com.george.commerce.dto.product;

public record CreateProductAttributeRequest(
        String name,
        String value
) {
}
