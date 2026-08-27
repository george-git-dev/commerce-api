package br.com.george.commerce.exception;

public class CouponExpiredException extends RuntimeException {
    public CouponExpiredException() {
        super("Coupon expired");
    }
}
