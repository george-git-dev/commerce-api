package br.com.george.commerce.dto.report.customer;

import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record CustomerStatsResponse(
        Long userId,
        String userName,
        Long totalOrders,
        BigDecimal totalSpent,
        LocalDateTime lastPurchase
) {
}

