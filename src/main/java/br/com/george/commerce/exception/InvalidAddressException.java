package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

public class InvalidAddressException extends BusinessException {

    public InvalidAddressException() {
        super(HttpStatus.NOT_FOUND, "ADDRESS_NOT_FOUND", "Endereço não encontrado.");
    }
}
