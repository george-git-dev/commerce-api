package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

public class EmptyCartException extends BusinessException {

    public EmptyCartException() {
        super(HttpStatus.UNPROCESSABLE_CONTENT, "CART_EMPTY", "Sua sacola está vazia.");
    }
}
