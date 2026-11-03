package br.com.george.commerce.dto.report.financial;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record ExpenseCategoryStatsResponse(
        String categoryName,
        BigDecimal totalAmount
) {
}

