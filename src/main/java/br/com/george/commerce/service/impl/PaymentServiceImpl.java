package br.com.george.commerce.service.impl;

import br.com.george.commerce.dto.payment.CreatePaymentRequest;
import br.com.george.commerce.dto.payment.PaymentResponse;
import br.com.george.commerce.entity.Order;
import br.com.george.commerce.entity.Payment;
import br.com.george.commerce.enums.OrderStatus;
import br.com.george.commerce.enums.PaymentStatus;
import br.com.george.commerce.exception.InvalidPaymentStatusException;
import br.com.george.commerce.exception.OrderNotFoundException;
import br.com.george.commerce.exception.PaymentAlreadyExistsException;
import br.com.george.commerce.exception.PaymentNotFoundException;
import br.com.george.commerce.mapper.PaymentMapper;
import br.com.george.commerce.repository.OrderRepository;
import br.com.george.commerce.repository.PaymentRepository;
import br.com.george.commerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper mapper;

    @Override
    public PaymentResponse createPayment(Long orderId, CreatePaymentRequest request) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));

        if (paymentRepository.findByOrderId(orderId).isPresent()) {
            throw new PaymentAlreadyExistsException(orderId);
        }

        Payment payment = Payment.builder()
                .order(order)
                .amount(order.getTotal())
                .method(request.method())
                .status(PaymentStatus.PENDENTE)
                .createdAt(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))
                .build();

        payment = paymentRepository.save(payment);

        return mapper.toResponse(payment);
    }

    @Override
    public PaymentResponse approvePayment(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));

        if (payment.getStatus() != PaymentStatus.PENDENTE) {
            throw new InvalidPaymentStatusException(payment.getStatus());
        }

        payment.setStatus(PaymentStatus.APROVADO);

        payment = paymentRepository.save(payment);

        Order order = payment.getOrder();

        order.setStatus(OrderStatus.PAGO);

        orderRepository.save(order);

        return mapper.toResponse(payment);
    }

    @Override
    public PaymentResponse findByOrder(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new PaymentNotFoundException(orderId));
        return mapper.toResponse(payment);
    }

    @Override
    public PaymentResponse rejectPayment(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));

        if (payment.getStatus() != PaymentStatus.PENDENTE) {
            throw new InvalidPaymentStatusException(payment.getStatus());
        }

        payment.setStatus(PaymentStatus.RECUSADO);

        payment = paymentRepository.save(payment);

        return mapper.toResponse(payment);
    }
}
