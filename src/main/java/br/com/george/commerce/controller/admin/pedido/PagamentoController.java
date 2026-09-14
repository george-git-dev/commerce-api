package br.com.george.commerce.controller.admin.pedido;

import br.com.george.commerce.dto.payment.CreatePaymentRequest;
import br.com.george.commerce.dto.payment.PaymentResponse;
import br.com.george.commerce.service.PaymentService;
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

    @PostMapping("/orders/{orderId}/payment")
    @PreAuthorize("isAuthenticated()")
    public PaymentResponse createPayment(@PathVariable Long orderId, @Valid @RequestBody CreatePaymentRequest request) {
        return paymentService.createPayment(orderId, request);
    }

    @PatchMapping("/payments/{paymentId}/approve")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public PaymentResponse approvePayment(@PathVariable Long paymentId) {
        return paymentService.approvePayment(paymentId);
    }

    @GetMapping("/orders/{orderId}/payment")
    @PreAuthorize("isAuthenticated()")
    public PaymentResponse findByOrder(@PathVariable Long orderId) {
        return paymentService.findByOrder(orderId);
    }

    @PatchMapping("/payments/{paymentId}/reject")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public PaymentResponse rejectPayment(@PathVariable Long paymentId) {
        return paymentService.rejectPayment(paymentId);
    }
}


