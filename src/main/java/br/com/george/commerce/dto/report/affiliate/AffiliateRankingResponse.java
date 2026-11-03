package br.com.george.commerce.dto.report.affiliate;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record AffiliateRankingResponse(
        Long affiliateId,
        String affiliateName,
        Long totalSales,
        BigDecimal totalRevenue,
        BigDecimal totalCommission
) {
}
