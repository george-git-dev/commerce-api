package br.com.george.commerce.dto.report.inventory;

public record CategoryInventoryStatsResponse(
        String categoryName,
        Long totalProducts
) {
}

