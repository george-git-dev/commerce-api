package br.com.george.commerce.dto.report.sales;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record SalesSummaryResponse(

        Long totalSales,

        Long onlineSales,

        Long manualSales,

        BigDecimal totalRevenue,

        BigDecimal onlineRevenue,

        BigDecimal manualRevenue,

        BigDecimal averageTicket

) {
}

