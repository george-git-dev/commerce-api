package br.com.george.commerce.exception;

import br.com.george.commerce.enums.PaymentStatus;

public class InvalidPaymentStatusException extends RuntimeException {

    public InvalidPaymentStatusException(PaymentStatus status) {
        super("Payment is already " + status);
    }
}
