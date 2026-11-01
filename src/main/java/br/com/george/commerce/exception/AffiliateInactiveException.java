package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

public class AffiliateInactiveException extends BusinessException {

    public AffiliateInactiveException() {
        super(HttpStatus.UNPROCESSABLE_CONTENT, "AFFILIATE_INACTIVE", "Este afiliado está inativo.");
    }
}
