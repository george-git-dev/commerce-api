package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

public class ProductQuantityLimitExceededException extends BusinessException {

    public ProductQuantityLimitExceededException(Integer limit) {
        super(HttpStatus.UNPROCESSABLE_CONTENT, "PRODUCT_QUANTITY_LIMIT", "Você pode levar no máximo " + limit + " unidades de cada produto.");
    }
}
