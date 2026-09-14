package br.com.george.commerce.dto.report.sales;

import java.math.BigDecimal;

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

