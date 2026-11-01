package br.com.george.commerce.dto.payment;

import br.com.george.commerce.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record CreatePaymentRequest(

        @NotNull(message = "Informe a forma de pagamento.")
        PaymentMethod method

) {
}
