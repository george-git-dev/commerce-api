package br.com.george.commerce.dto.report.sales;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record TopProductResponse(
        String productName,
        Long quantitySold,
        BigDecimal revenue
) {
}


