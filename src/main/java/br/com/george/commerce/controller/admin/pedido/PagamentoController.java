package br.com.george.commerce.controller.admin.pedido;

import br.com.george.commerce.util.Locations;
import org.springframework.http.ResponseEntity;
import br.com.george.commerce.dto.payment.CreatePaymentRequest;
import br.com.george.commerce.dto.payment.PaymentResponse;
import br.com.george.commerce.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Administração - Pagamentos",
        description = "Aprovação e rejeição de pagamentos."
)
@RestController
@RequiredArgsConstructor
public class PagamentoController {

    private final PaymentService paymentService;

    @Operation(summary = "Registrar pagamento do pedido")
    @PostMapping("/orders/{orderId}/payment")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaymentResponse> createPayment(@PathVariable Long orderId, @Valid @RequestBody CreatePaymentRequest request) {
        PaymentResponse created = paymentService.createPayment(orderId, request);
        return ResponseEntity.created(Locations.of("/orders/{orderId}/payment", orderId)).body(created);
    }

    @Operation(summary = "Aprovar pagamento")
    @PatchMapping("/payments/{paymentId}/approve")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<PaymentResponse> approvePayment(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.approvePayment(paymentId));
    }

    @Operation(summary = "Consultar pagamento do pedido")
    @GetMapping("/orders/{orderId}/payment")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaymentResponse> findByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.findByOrder(orderId));
    }

    @Operation(summary = "Recusar pagamento")
    @PatchMapping("/payments/{paymentId}/reject")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<PaymentResponse> rejectPayment(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.rejectPayment(paymentId));
    }
}


