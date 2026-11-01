package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

public class PaymentAlreadyExistsException extends BusinessException {

    public PaymentAlreadyExistsException(Long orderId) {
        super(HttpStatus.CONFLICT, "PAYMENT_ALREADY_EXISTS", "Este pedido já tem um pagamento.");
    }
}
