package br.com.george.commerce.exception;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException() {
        super("Current password is invalid");
    }
}
