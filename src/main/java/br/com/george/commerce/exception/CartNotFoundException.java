package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

public class CartNotFoundException extends BusinessException {

    public CartNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "CART_NOT_FOUND", "Sacola não encontrada.");
    }
}
