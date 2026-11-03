package br.com.george.commerce.dto.report.inventory;

import lombok.Builder;
@Builder
public record InventorySummaryResponse(
        long totalProducts,
        long activeProducts,
        long inactiveProducts,
        long outOfStockProducts
) {
}

