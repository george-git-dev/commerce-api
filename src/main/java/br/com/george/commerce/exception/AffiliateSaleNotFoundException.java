package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

public class AffiliateSaleNotFoundException extends BusinessException {

    public AffiliateSaleNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "AFFILIATE_SALE_NOT_FOUND", "Venda do afiliado não encontrada.");
    }
}
