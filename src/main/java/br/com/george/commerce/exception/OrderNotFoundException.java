package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends BusinessException {

    public OrderNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "ORDER_NOT_FOUND", "Pedido não encontrado.");
    }
}
