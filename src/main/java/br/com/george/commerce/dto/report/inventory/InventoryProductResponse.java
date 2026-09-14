package br.com.george.commerce.dto.report.inventory;

public record InventoryProductResponse(
        Long id,
        String name,
        String categoryName,
        String brandName,
        Integer stock,
        Boolean active
) {
}

