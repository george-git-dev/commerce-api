package br.com.george.commerce.controller.me;

import org.springframework.http.ResponseEntity;
import br.com.george.commerce.dto.cart.ApplyCouponRequest;
import br.com.george.commerce.dto.cart.CartResponse;
import br.com.george.commerce.dto.cart.CreateCartItemRequest;
import br.com.george.commerce.dto.cart.UpdateCartItemRequest;
import br.com.george.commerce.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Ver minha sacola")
    @GetMapping("/cart")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponse> myCart() {
        return ResponseEntity.ok(cartService.myCart());
    }

    @Operation(summary = "Esvaziar sacola")
    @DeleteMapping("/cart")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> clearCart() {
        cartService.clearCart();
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Adicionar item à sacola")
    @PostMapping("/cart/items")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponse> addItem(@Valid @RequestBody CreateCartItemRequest request) {
        return ResponseEntity.ok(cartService.addItem(request));
    }

    @Operation(summary = "Alterar quantidade do item")
    @PatchMapping("/cart/items/{itemId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponse> updateItemQuantity(@PathVariable Long itemId, @Valid @RequestBody UpdateCartItemRequest request) {
        return ResponseEntity.ok(cartService.updateItemQuantity(itemId, request));
    }

    @Operation(summary = "Remover item da sacola")
    @DeleteMapping("/cart/items/{itemId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeItem(@PathVariable Long itemId) {
        cartService.removeItem(itemId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Aplicar cupom")
    @PostMapping("/cart/coupon")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> applyCoupon(@Valid @RequestBody ApplyCouponRequest request) {
        cartService.applyCoupon(request.couponCode());
        return ResponseEntity.noContent().build();
    }
}

