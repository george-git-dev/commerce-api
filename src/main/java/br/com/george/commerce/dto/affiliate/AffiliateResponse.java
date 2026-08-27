package br.com.george.commerce.dto.affiliate;

import br.com.george.commerce.enums.DiscountType;

import java.math.BigDecimal;

public record AffiliateResponse(

        Long id,

        Long userId,

        String userName,

        String couponCode,

        Boolean active,

        BigDecimal commissionPercentage,

        DiscountType discountType,

        BigDecimal discountValue

) {
}
