package br.com.george.commerce.dto.order;

import br.com.george.commerce.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(

        Long id,

        Long userId,

        String userName,

        Long addressId,

        BigDecimal total,

        OrderStatus status,

        LocalDateTime createdAt,

        List<OrderItemResponse> items

) {
}
