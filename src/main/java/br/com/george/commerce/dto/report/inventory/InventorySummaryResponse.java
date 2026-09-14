package br.com.george.commerce.dto.report.inventory;

public record InventorySummaryResponse(
        long totalProducts,
        long activeProducts,
        long inactiveProducts,
        long outOfStockProducts
) {
}

