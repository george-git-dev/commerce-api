package br.com.george.commerce.exception;

import br.com.george.commerce.enums.OrderStatus;

public class InvalidOrderStatusTransitionException extends RuntimeException {

    public InvalidOrderStatusTransitionException(OrderStatus current, OrderStatus target) {

        super("Cannot change order status from " + current + " to " + target);
    }
}
