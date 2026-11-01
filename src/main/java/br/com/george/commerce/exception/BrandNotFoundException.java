package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

public class BrandNotFoundException extends BusinessException {

    public BrandNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "BRAND_NOT_FOUND", "Marca não encontrada.");
    }
}
