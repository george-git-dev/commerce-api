package br.com.george.commerce.dto.report.sales;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record TopBrandResponse(
        String brandName,
        Long quantitySold,
        BigDecimal revenue
) {
}


