package br.com.george.commerce.dto.report.inventory;

public record InventoryFilterRequest(
        String name,
        Long categoryId,
        Long brandId,
        Boolean active,
        Boolean outOfStock
) {
}

