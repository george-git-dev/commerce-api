package br.com.george.commerce.exception;

public class ProductQuantityLimitExceededException extends RuntimeException {

    public ProductQuantityLimitExceededException(Integer limit) {
        super("Maximum quantity per product is " + limit);
    }
}
