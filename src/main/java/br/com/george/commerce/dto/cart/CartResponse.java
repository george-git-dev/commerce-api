package br.com.george.commerce.dto.cart;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(

        Long id,

        Long userId,

        String userName,

        BigDecimal total,

        List<CartItemResponse> items

) {
}
