package br.com.george.commerce.controller.me;

import br.com.george.commerce.dto.cart.ApplyCouponRequest;
import br.com.george.commerce.dto.cart.CartResponse;
import br.com.george.commerce.dto.cart.CreateCartItemRequest;
import br.com.george.commerce.dto.cart.UpdateCartItemRequest;
import br.com.george.commerce.service.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Área do Cliente - Carrinho",
        description = "Carrinho de compras do usuário autenticado."
)
@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class MeuCarrinhoController {

    private final CartService cartService;

    @GetMapping("/cart")
    @PreAuthorize("isAuthenticated()")
    public CartResponse myCart() {
        return cartService.myCart();
    }

    @DeleteMapping("/cart")
    @PreAuthorize("isAuthenticated()")
    public void clearCart() {
        cartService.clearCart();
    }

    @PostMapping("/cart/items")
    @PreAuthorize("isAuthenticated()")
    public CartResponse addItem(@Valid @RequestBody CreateCartItemRequest request) {
        return cartService.addItem(request);
    }

    @PatchMapping("/cart/items/{itemId}")
    @PreAuthorize("isAuthenticated()")
    public CartResponse updateItemQuantity(@PathVariable Long itemId, @Valid @RequestBody UpdateCartItemRequest request) {
        return cartService.updateItemQuantity(itemId, request);
    }

    @DeleteMapping("/cart/items/{itemId}")
    @PreAuthorize("isAuthenticated()")
    public void removeItem(@PathVariable Long itemId) {
        cartService.removeItem(itemId);
    }

    @PostMapping("/cart/coupon")
    @PreAuthorize("isAuthenticated()")
    public void applyCoupon(@Valid @RequestBody ApplyCouponRequest request) {
        cartService.applyCoupon(request.couponCode());
    }
}

