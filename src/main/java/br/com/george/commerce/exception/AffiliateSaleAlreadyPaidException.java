package br.com.george.commerce.exception;

public class AffiliateSaleAlreadyPaidException extends RuntimeException {

    public AffiliateSaleAlreadyPaidException() {
        super("Affiliate sale is already paid");
    }
}
