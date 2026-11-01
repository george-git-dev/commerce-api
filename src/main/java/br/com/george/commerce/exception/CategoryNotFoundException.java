package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends BusinessException {

    public CategoryNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "CATEGORY_NOT_FOUND", "Categoria não encontrada.");
    }
}
