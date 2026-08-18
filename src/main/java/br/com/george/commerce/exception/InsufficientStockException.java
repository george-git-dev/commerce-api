package br.com.george.commerce.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String productName) {
        super("Insufficient stock for product: " + productName);
    }
}
