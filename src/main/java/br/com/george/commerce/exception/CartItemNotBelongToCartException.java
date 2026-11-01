package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

public class CartItemNotBelongToCartException extends BusinessException {

    public CartItemNotBelongToCartException() {
        super(HttpStatus.NOT_FOUND, "CART_ITEM_NOT_FOUND", "Item não encontrado na sua sacola.");
    }
}
