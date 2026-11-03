package br.com.george.commerce.dto.report.inventory;

import lombok.Builder;
@Builder
public record BrandInventoryStatsResponse(
        String brandName,
        Long totalProducts
) {
}

