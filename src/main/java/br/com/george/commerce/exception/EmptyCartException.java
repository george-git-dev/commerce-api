package br.com.george.commerce.exception;

public class EmptyCartException extends RuntimeException {

    public EmptyCartException() {
        super("Cart is empty");
    }

}
