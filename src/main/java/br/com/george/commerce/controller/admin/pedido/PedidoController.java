package br.com.george.commerce.controller.admin.pedido;

import org.springframework.http.ResponseEntity;
import br.com.george.commerce.dto.order.OrderResponse;
import br.com.george.commerce.dto.order.UpdateOrderStatusRequest;
import br.com.george.commerce.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Administração - Pedidos",
        description = "Consulta e gerenciamento administrativo de pedidos."
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class PedidoController {

    private final OrderService orderService;

    @Operation(summary = "Consultar pedido")
    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.findById(orderId));
    }

    @Operation(summary = "Atualizar situação do pedido")
    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long orderId, @Valid @RequestBody UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(orderService.updateStatus(orderId, request));
    }
}


