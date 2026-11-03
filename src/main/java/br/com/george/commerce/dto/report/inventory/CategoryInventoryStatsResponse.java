package br.com.george.commerce.dto.report.inventory;

import lombok.Builder;
@Builder
public record CategoryInventoryStatsResponse(
        String categoryName,
        Long totalProducts
) {
}

