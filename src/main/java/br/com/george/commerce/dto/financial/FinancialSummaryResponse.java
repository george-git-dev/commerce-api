package br.com.george.commerce.dto.financial;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
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
