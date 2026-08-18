package br.com.george.commerce.controller;


import br.com.george.commerce.dto.address.AddressResponse;
import br.com.george.commerce.dto.cart.CartResponse;
import br.com.george.commerce.dto.order.OrderResponse;
import br.com.george.commerce.dto.user.ChangeEmailRequest;
import br.com.george.commerce.dto.user.ChangePasswordRequest;
import br.com.george.commerce.dto.user.UserResponse;
import br.com.george.commerce.service.AddressService;
import br.com.george.commerce.service.CartService;
import br.com.george.commerce.service.OrderService;
import br.com.george.commerce.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class MeController {

    private final OrderService orderService;
    private final CartService cartService;
    private final AddressService addressService;
    private final UserService userService;

    @GetMapping("/orders")
    public List<OrderResponse> myOrders() {
        return orderService.myOrders();
    }

    @GetMapping("/cart")
    public CartResponse myCart() {
        return cartService.myCart();
    }

    @GetMapping("/addresses")
    public List<AddressResponse> myAddresses() {
        return addressService.myAddresses();
    }

    @GetMapping
    public UserResponse user() {
        return userService.me();
    }

    @PatchMapping("/password")
    public void changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
    }

    @PatchMapping("/email")
    public void changeEmail(@Valid @RequestBody ChangeEmailRequest request) {
        userService.changeEmail(request);
    }
}
