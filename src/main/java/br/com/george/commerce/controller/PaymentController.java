package br.com.george.commerce.controller;

import br.com.george.commerce.dto.payment.CreatePaymentRequest;
import br.com.george.commerce.dto.payment.PaymentResponse;
import br.com.george.commerce.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/orders/{orderId}/payment")
    public PaymentResponse createPayment(@PathVariable Long orderId, @Valid @RequestBody CreatePaymentRequest request) {
        return paymentService.createPayment(orderId, request);
    }

    @PatchMapping("/payments/{paymentId}/approve")
    public PaymentResponse approvePayment(@PathVariable Long paymentId) {
        return paymentService.approvePayment(paymentId);
    }

    @GetMapping("/orders/{orderId}/payment")
    public PaymentResponse findByOrder(@PathVariable Long orderId) {
        return paymentService.findByOrder(orderId);
    }

    @PatchMapping("/payments/{paymentId}/reject")
    public PaymentResponse rejectPayment(@PathVariable Long paymentId) {
        return paymentService.rejectPayment(paymentId);
    }
}
