package br.com.george.commerce.exception;

public class PaymentNotFoundException extends RuntimeException {

    public PaymentNotFoundException(Long id) {
        super("Payment not found with id: " + id);
    }
}
