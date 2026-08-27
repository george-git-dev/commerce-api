package br.com.george.commerce.dto.promotion;

import br.com.george.commerce.enums.DiscountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreatePromotionRequest(

        String name,

        String description,

        Boolean active,

        DiscountType discountType,

        BigDecimal discountValue,

        LocalDateTime startAt,

        LocalDateTime endAt

) {
}
