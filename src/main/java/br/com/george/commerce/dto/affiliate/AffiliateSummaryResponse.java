package br.com.george.commerce.dto.affiliate;

import java.math.BigDecimal;

public record AffiliateSummaryResponse(

        BigDecimal totalSales,

        BigDecimal totalCommission,

        BigDecimal pendingCommission,

        BigDecimal paidCommission

) {
}