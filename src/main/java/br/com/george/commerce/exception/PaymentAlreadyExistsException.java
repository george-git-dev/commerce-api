package br.com.george.commerce.exception;

public class PaymentAlreadyExistsException extends RuntimeException {

    public PaymentAlreadyExistsException(Long orderId) {
        super("Payment already exists for order: " + orderId);
    }

}
