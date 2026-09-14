package br.com.george.commerce.dto.report.financial;

import java.math.BigDecimal;

public record ExpenseCategoryStatsResponse(
        String categoryName,
        BigDecimal totalAmount
) {
}

