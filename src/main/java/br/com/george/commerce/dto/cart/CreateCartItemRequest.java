package br.com.george.commerce.dto.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateCartItemRequest(

        @NotNull(message = "Informe o produto.")
        Long productId,

        @NotNull(message = "Informe a quantidade.")
        @Positive(message = "A quantidade precisa ser maior que zero.")
        Integer quantity

) {
}
