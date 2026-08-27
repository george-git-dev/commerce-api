package br.com.george.commerce.exception;

public class CouponNotFoundException extends RuntimeException {

    public CouponNotFoundException() {
        super("Coupon not found");
    }
}
