package br.com.george.commerce.dto.report.sales;

import java.math.BigDecimal;

public record TopCategoryResponse(
        String categoryName,
        Long quantitySold,
        BigDecimal revenue
) {
}

