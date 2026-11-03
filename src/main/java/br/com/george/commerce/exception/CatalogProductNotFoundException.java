package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

public class CatalogProductNotFoundException extends BusinessException {

    public CatalogProductNotFoundException() {
        super(HttpStatus.NOT_FOUND, "PRODUCT_NOT_FOUND", "Produto não encontrado.");
    }
}
