package br.com.george.commerce.exception;

public class AffiliateAlreadyExistsException extends  RuntimeException {
    public AffiliateAlreadyExistsException() {
        super("Affiliate already exists");
    }
}
