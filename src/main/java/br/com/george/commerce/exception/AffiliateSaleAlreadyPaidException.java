package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

public class AffiliateSaleAlreadyPaidException extends BusinessException {

    public AffiliateSaleAlreadyPaidException() {
        super(HttpStatus.CONFLICT, "AFFILIATE_SALE_ALREADY_PAID", "Esta comissão já foi paga.");
    }
}
