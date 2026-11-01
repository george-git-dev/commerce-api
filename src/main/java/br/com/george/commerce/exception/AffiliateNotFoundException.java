package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

public class AffiliateNotFoundException extends BusinessException {

    public AffiliateNotFoundException() {
        super(HttpStatus.NOT_FOUND, "AFFILIATE_NOT_FOUND", "Afiliado não encontrado.");
    }
}
