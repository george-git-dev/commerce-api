package br.com.george.commerce.controller;

import br.com.george.commerce.dto.cart.CartResponse;
import br.com.george.commerce.dto.cart.CreateCartItemRequest;
import br.com.george.commerce.dto.cart.UpdateCartItemRequest;
import br.com.george.commerce.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public CartResponse findByUser(@PathVariable Long userId) {
        return cartService.findByUser(userId);
    }

    @PostMapping("/items")
    public CartResponse addItem(@PathVariable Long userId, @Valid @RequestBody CreateCartItemRequest request) {
        return cartService.addItem(userId, request);
    }

    @DeleteMapping("/items/{itemId}")
    public void removeItem(@PathVariable Long userId, @PathVariable Long itemId) {
        cartService.removeItem(userId, itemId);
    }

    @PatchMapping("/items/{itemId}")
    public CartResponse updateQuantity(@PathVariable Long userId, @PathVariable Long itemId, @Valid @RequestBody UpdateCartItemRequest request) {
        return cartService.updateItemQuantity(userId, itemId, request);
    }

    @DeleteMapping
    public void clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
    }

}
