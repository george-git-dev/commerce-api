package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

public class AddressLimitException extends BusinessException {

    public AddressLimitException(int limit) {
        super(HttpStatus.CONFLICT, "ADDRESS_LIMIT",
                "Você chegou ao limite de " + limit + " endereços. Remova um para cadastrar outro.");
    }
}
