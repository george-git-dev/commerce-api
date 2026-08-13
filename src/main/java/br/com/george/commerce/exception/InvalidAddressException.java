package br.com.george.commerce.exception;

public class InvalidAddressException extends RuntimeException {

    public InvalidAddressException() {
        super("Address does not belong to user");
    }

}
