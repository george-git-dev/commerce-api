package br.com.george.commerce.dto.affiliate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AffiliateSaleResponse(

        Long id,

        Long affiliateId,

        Long orderId,

        BigDecimal saleAmount,

        BigDecimal commissionAmount,

        Boolean paid,

        LocalDateTime createdAt,

        LocalDateTime paidAt

) {
}
