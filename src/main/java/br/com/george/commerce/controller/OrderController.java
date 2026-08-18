package br.com.george.commerce.controller;

import br.com.george.commerce.dto.order.CreateOrderRequest;
import br.com.george.commerce.dto.order.OrderResponse;
import br.com.george.commerce.dto.order.UpdateOrderStatusRequest;
import br.com.george.commerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public OrderResponse checkout(@PathVariable Long userId, @Valid @RequestBody CreateOrderRequest request) {
        return orderService.checkout(userId, request);
    }

    @GetMapping("/{orderId}")
    public OrderResponse findById(@PathVariable Long orderId) {
        return orderService.findById(orderId);
    }

    @PatchMapping("/{orderId}/status")
    public OrderResponse updateStatus(@PathVariable Long orderId, @Valid @RequestBody UpdateOrderStatusRequest request) {
        return orderService.updateStatus(orderId, request);
    }
}
