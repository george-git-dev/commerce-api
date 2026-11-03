package br.com.george.commerce.dto.cart;

import lombok.Builder;
import java.math.BigDecimal;
import java.util.List;

@Builder
public record CartResponse(

        Long id,

        Long userId,

        String userName,

        BigDecimal total,

        String couponCode,

        BigDecimal discountApplied,

        BigDecimal totalWithDiscount,

        List<CartItemResponse> items

) {
}
