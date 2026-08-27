package br.com.george.commerce.exception;

public class AffiliateSaleNotFoundException extends RuntimeException {

    public AffiliateSaleNotFoundException(Long id) {
        super("Affiliate sale not found with id: " + id);
    }
}