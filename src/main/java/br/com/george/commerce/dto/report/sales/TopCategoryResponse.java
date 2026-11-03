package br.com.george.commerce.dto.report.sales;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record TopCategoryResponse(
        String categoryName,
        Long quantitySold,
        BigDecimal revenue
) {
}

