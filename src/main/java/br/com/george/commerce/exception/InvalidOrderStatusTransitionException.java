package br.com.george.commerce.exception;

import br.com.george.commerce.enums.OrderStatus;
import org.springframework.http.HttpStatus;

public class InvalidOrderStatusTransitionException extends BusinessException {

    public InvalidOrderStatusTransitionException(OrderStatus current, OrderStatus target) {
        super(HttpStatus.CONFLICT, "ORDER_STATUS_TRANSITION_INVALID", "Não é possível mudar o pedido de " + current + " para " + target + ".");
    }
}
