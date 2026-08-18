package br.com.george.commerce.service;

import br.com.george.commerce.dto.order.CreateOrderRequest;
import br.com.george.commerce.dto.order.OrderResponse;
import br.com.george.commerce.dto.order.UpdateOrderStatusRequest;

import java.util.List;

public interface OrderService {

    OrderResponse checkout(Long userId, CreateOrderRequest request);

    List<OrderResponse> findByUser(Long userId);

    OrderResponse findById(Long id);

    OrderResponse updateStatus(Long orderId, UpdateOrderStatusRequest request);

    List<OrderResponse> myOrders();

}
