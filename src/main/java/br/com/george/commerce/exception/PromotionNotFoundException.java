package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

public class PromotionNotFoundException extends BusinessException {

    public PromotionNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "PROMOTION_NOT_FOUND", "Promoção não encontrada.");
    }
}
