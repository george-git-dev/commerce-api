package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

public class CouponNotFoundException extends BusinessException {

    public CouponNotFoundException() {
        super(HttpStatus.NOT_FOUND, "COUPON_NOT_FOUND", "Cupom inválido. Confira o código e tente de novo.");
    }
}
