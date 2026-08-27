package br.com.george.commerce.exception;

public class PromotionNotFoundException extends RuntimeException {

    public PromotionNotFoundException(Long id) {
        super("Promotion not found with id: " + id);
    }
}
