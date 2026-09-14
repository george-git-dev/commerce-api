package br.com.george.commerce.dto.report.inventory;

public record BrandInventoryStatsResponse(
        String brandName,
        Long totalProducts
) {
}

