package br.com.george.commerce.exception;

public class CartItemNotFoundException extends RuntimeException {

    public CartItemNotFoundException(Long id) {
        super("Cart item not found with id: " + id);
    }

}
