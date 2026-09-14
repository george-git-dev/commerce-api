package br.com.george.commerce.dto.report.sales;

import java.math.BigDecimal;

public record TopBrandResponse(
        String brandName,
        Long quantitySold,
        BigDecimal revenue
) {
}


