package br.com.george.commerce.dto.order;

import java.math.BigDecimal;

public record OrderItemResponse(

        Long id,

        String productName,

        BigDecimal productPrice,

        Integer quantity,

        BigDecimal subtotal

) {
}
