package br.com.george.commerce.dto.report.sales;

import java.math.BigDecimal;

public record TopProductResponse(
        String productName,
        Long quantitySold,
        BigDecimal revenue
) {
}


