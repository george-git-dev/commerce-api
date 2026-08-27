package br.com.george.commerce.service;

import br.com.george.commerce.entity.Cart;

import java.math.BigDecimal;

public interface DiscountService {

    BigDecimal calculateDiscount(Cart cart);

    BigDecimal calculateTotalWithDiscount(Cart cart);

}
