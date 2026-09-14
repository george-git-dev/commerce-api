package br.com.george.commerce.dto.report.affiliate;

import java.math.BigDecimal;

public record AffiliateRankingResponse(
        Long affiliateId,
        String affiliateName,
        Long totalSales,
        BigDecimal totalRevenue,
        BigDecimal totalCommission
) {
}
