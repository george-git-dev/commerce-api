package br.com.george.commerce.dto.order;

import br.com.george.commerce.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequest(

        @NotNull(message = "Informe a situação.")
        OrderStatus status

) {
}
