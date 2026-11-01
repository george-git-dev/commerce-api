package br.com.george.commerce.dto.order;

import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(@NotNull(message = "Informe o endereço.") Long addressId) {
}
