package br.com.george.commerce.dto.report.customer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CustomerStatsResponse(
        Long userId,
        String userName,
        Long totalOrders,
        BigDecimal totalSpent,
        LocalDateTime lastPurchase
) {
}

