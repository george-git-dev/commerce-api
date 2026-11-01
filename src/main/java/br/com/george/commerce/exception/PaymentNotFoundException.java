package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

public class PaymentNotFoundException extends BusinessException {

    public PaymentNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "PAYMENT_NOT_FOUND", "Pagamento não encontrado.");
    }
}
