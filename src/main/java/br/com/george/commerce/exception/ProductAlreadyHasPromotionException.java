package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

public class ProductAlreadyHasPromotionException extends BusinessException {

    public ProductAlreadyHasPromotionException(Long productId) {
        super(HttpStatus.CONFLICT, "PRODUCT_ALREADY_IN_PROMOTION", "Este produto já está em outra promoção.");
    }
}
