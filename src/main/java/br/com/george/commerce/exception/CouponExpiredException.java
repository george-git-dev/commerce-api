package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

public class CouponExpiredException extends BusinessException {

    public CouponExpiredException() {
        super(HttpStatus.UNPROCESSABLE_CONTENT, "COUPON_EXPIRED", "Este cupom expirou.");
    }
}
