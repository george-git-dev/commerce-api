package br.com.george.commerce.exception;

public class AffiliateInactiveException extends RuntimeException {

    public AffiliateInactiveException() {
        super("Affiliate is inactive");
    }
}