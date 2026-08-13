package br.com.george.commerce.dto.payment;

import br.com.george.commerce.enums.PaymentMethod;
import br.com.george.commerce.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(

        Long id,

        Long orderId,

        BigDecimal amount,

        PaymentMethod method,

        PaymentStatus status,

        LocalDateTime createdAt

) {
}
