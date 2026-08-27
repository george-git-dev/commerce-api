package br.com.george.commerce.service.impl;

import br.com.george.commerce.dto.manualsale.ManualSaleRequest;
import br.com.george.commerce.dto.order.OrderResponse;
import br.com.george.commerce.entity.Order;
import br.com.george.commerce.entity.OrderItem;
import br.com.george.commerce.entity.Product;
import br.com.george.commerce.enums.OrderStatus;
import br.com.george.commerce.enums.SaleChannel;
import br.com.george.commerce.exception.InsufficientStockException;
import br.com.george.commerce.exception.ProductNotFoundException;
import br.com.george.commerce.mapper.OrderMapper;
import br.com.george.commerce.repository.OrderRepository;
import br.com.george.commerce.repository.ProductRepository;
import br.com.george.commerce.service.ManualSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManualSaleServiceImpl implements ManualSaleService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper mapper;

    @Override
    @Transactional
    public OrderResponse createSale(ManualSaleRequest request) {

        Order order = Order.builder()
                .saleChannel(SaleChannel.MANUAL)
                .status(OrderStatus.PAGO)
                .notes(request.notes())
                .createdAt(LocalDateTime.now())
                .build();

        List<OrderItem> items = request.items()
                .stream()
                .map(itemRequest -> {

                    Product product = productRepository
                            .findById(itemRequest.productId())
                            .orElseThrow(() -> new ProductNotFoundException(itemRequest.productId()));

                    int updatedRows = productRepository.decreaseStock(product.getId(), itemRequest.quantity());

                    if (updatedRows == 0) {
                        throw new InsufficientStockException(product.getName());
                    }

                    BigDecimal subtotal = itemRequest.unitPrice().multiply(BigDecimal.valueOf(itemRequest.quantity()));

                    return OrderItem.builder()
                            .order(order)
                            .productName(product.getName())
                            .productPrice(itemRequest.unitPrice())
                            .quantity(itemRequest.quantity())
                            .subtotal(subtotal)
                            .build();
                })
                .toList();

        BigDecimal total = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setItems(items);
        order.setTotal(total);

        Order savedOrder = orderRepository.save(order);

        return mapper.toResponse(savedOrder);
    }
}
