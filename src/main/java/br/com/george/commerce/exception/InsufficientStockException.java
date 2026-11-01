package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

public class InsufficientStockException extends BusinessException {

    public InsufficientStockException(String productName) {
        super(HttpStatus.UNPROCESSABLE_CONTENT, "OUT_OF_STOCK", "Não temos mais unidades suficientes de " + productName + ".");
    }
}
