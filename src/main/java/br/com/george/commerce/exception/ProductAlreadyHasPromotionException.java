package br.com.george.commerce.exception;

public class ProductAlreadyHasPromotionException extends RuntimeException {

    public ProductAlreadyHasPromotionException(Long productId) {
        super("Product already has a promotion: " + productId);
    }
}
