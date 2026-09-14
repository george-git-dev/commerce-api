package br.com.george.commerce.controller.me;

import br.com.george.commerce.dto.order.CreateOrderRequest;
import br.com.george.commerce.dto.order.OrderResponse;
import br.com.george.commerce.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Área do Cliente - Pedidos",
        description = "Pedidos realizados pelo usuário autenticado e checkout."
)
@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class MeusPedidosController {

    private final OrderService orderService;

    @GetMapping("/orders")
    @PreAuthorize("isAuthenticated()")
    public List<OrderResponse> myOrders() {
        return orderService.myOrders();
    }

    @PostMapping("/orders/checkout")
    @PreAuthorize("isAuthenticated()")
    public OrderResponse checkout(@Valid @RequestBody CreateOrderRequest request) {
        return orderService.checkout(request);
    }
}

