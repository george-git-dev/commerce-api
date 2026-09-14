package br.com.george.commerce.dto.financial;

import java.math.BigDecimal;

public record FinancialSummaryResponse(

        BigDecimal totalRevenue,

        BigDecimal onlineRevenue,

        BigDecimal manualRevenue,

        BigDecimal totalExpenses,

        BigDecimal affiliateCommissions,

        BigDecimal resultadoBruto,

        BigDecimal resultadoLiquido
) {
}
