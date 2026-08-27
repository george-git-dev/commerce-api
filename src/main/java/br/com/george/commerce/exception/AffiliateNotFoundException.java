package br.com.george.commerce.exception;

public class AffiliateNotFoundException extends RuntimeException{

    public AffiliateNotFoundException() {
        super("Affiliate not found");
    }
}
