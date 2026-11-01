package br.com.george.commerce.exception;

import br.com.george.commerce.enums.PaymentStatus;
import org.springframework.http.HttpStatus;

public class InvalidPaymentStatusException extends BusinessException {

    public InvalidPaymentStatusException(PaymentStatus status) {
        super(HttpStatus.CONFLICT, "PAYMENT_STATUS_INVALID", "Este pagamento já está " + status + ".");
    }
}
