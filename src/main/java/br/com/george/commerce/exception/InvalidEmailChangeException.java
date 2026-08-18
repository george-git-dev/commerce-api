package br.com.george.commerce.exception;

public class InvalidEmailChangeException extends RuntimeException {

    public InvalidEmailChangeException(String message) {
        super(message);
    }
}
