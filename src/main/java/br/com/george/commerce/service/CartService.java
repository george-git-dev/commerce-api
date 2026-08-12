package br.com.george.commerce.service;

import br.com.george.commerce.dto.cart.CartResponse;
import br.com.george.commerce.dto.cart.CreateCartItemRequest;
import br.com.george.commerce.dto.cart.UpdateCartItemRequest;

public interface CartService {

    CartResponse findByUser(Long userId);

    CartResponse addItem(Long userId, CreateCartItemRequest request);

    void removeItem(Long userId, Long itemId);

    CartResponse updateItemQuantity(Long userId, Long itemId, UpdateCartItemRequest request);

    void clearCart(Long userId);

}
