package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

public class AddressNotFoundException extends BusinessException {

    public AddressNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "ADDRESS_NOT_FOUND", "Endereço não encontrado.");
    }
}
