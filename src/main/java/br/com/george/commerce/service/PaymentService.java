package br.com.george.commerce.service;

import br.com.george.commerce.dto.payment.CreatePaymentRequest;
import br.com.george.commerce.dto.payment.PaymentResponse;

public interface PaymentService {

    PaymentResponse createPayment(Long orderId, CreatePaymentRequest request);

    PaymentResponse approvePayment(Long paymentId);

    PaymentResponse findByOrder(Long orderId);

    PaymentResponse rejectPayment(Long paymentId);

}
