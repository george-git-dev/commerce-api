package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

public class AffiliateAlreadyExistsException extends BusinessException {

    public AffiliateAlreadyExistsException() {
        super(HttpStatus.CONFLICT, "AFFILIATE_ALREADY_EXISTS", "Esta conta já participa do programa de afiliados.");
    }
}
