package br.com.george.commerce.dto.affiliate;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record AffiliateSummaryResponse(

        BigDecimal totalSales,

        BigDecimal totalCommission,

        BigDecimal pendingCommission,

        BigDecimal paidCommission

) {
}