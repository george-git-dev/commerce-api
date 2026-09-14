package br.com.george.commerce.service.impl;

import br.com.george.commerce.dto.order.CreateOrderRequest;
import br.com.george.commerce.dto.order.OrderResponse;
import br.com.george.commerce.dto.order.UpdateOrderStatusRequest;
import br.com.george.commerce.entity.*;
import br.com.george.commerce.enums.OrderStatus;
import br.com.george.commerce.enums.SaleChannel;
import br.com.george.commerce.exception.*;
import br.com.george.commerce.mapper.OrderMapper;
import br.com.george.commerce.repository.*;
import br.com.george.commerce.service.DiscountService;
import br.com.george.commerce.service.OrderService;
import br.com.george.commerce.service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final OrderMapper mapper;
    private final JwtService jwtService;
    private final DiscountService discountService;
    private final AffiliateRepository affiliateRepository;
    private final AffiliateSaleRepository affiliateSaleRepository;

    @Transactional
    public OrderResponse checkout(Long userId, CreateOrderRequest request) {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        Address address = addressRepository.findById(request.addressId()).orElseThrow(() -> new AddressNotFoundException(request.addressId()));

        if (!address.getUser().getId().equals(userId)) {
            throw new InvalidAddressException();
        }

        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new CartNotFoundException(userId));

        if (cart.getItems() == null ||
                cart.getItems().isEmpty()) {

            throw new EmptyCartException();
        }

        BigDecimal total = cart.getItems().stream().map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discountApplied = discountService.calculateDiscount(cart);

        Order order = Order.builder()
                .user(user)
                .address(address)
                .status(OrderStatus.PENDENTE)
                .couponCode(cart.getCouponCode())
                .discountApplied(discountApplied)
                .createdAt(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))
                .saleChannel(SaleChannel.ONLINE)
                .build();

        for (CartItem cartItem : cart.getItems()) {

            int updatedRows = productRepository.decreaseStock(cartItem.getProduct().getId(), cartItem.getQuantity());

            if (updatedRows == 0) {
                throw new InsufficientStockException(cartItem.getProduct().getName());
            }
        }

        List<OrderItem> items = cart.getItems()
                .stream()
                .map(cartItem -> {

                    BigDecimal subtotal = cartItem.getProduct()
                            .getPrice()
                            .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

                    return OrderItem.builder()
                            .order(order)
                            .productName(cartItem.getProduct().getName())
                            .productPrice(cartItem.getProduct().getPrice())
                            .quantity(cartItem.getQuantity())
                            .subtotal(subtotal)
                            .categoryName(cartItem.getProduct().getCategory().getName())
                            .brandName(cartItem.getProduct().getBrand().getName())
                            .build();
                })
                .toList();

        order.setTotal(total.subtract(discountApplied));
        order.setItems(items);

        Order savedOrder = orderRepository.save(order);

        if (cart.getCouponCode() != null) {

            Affiliate affiliate = affiliateRepository
                    .findByCouponCode(cart.getCouponCode())
                    .orElseThrow(CouponNotFoundException::new);

            BigDecimal commissionAmount = order.getTotal()
                    .multiply(affiliate.getCommissionPercentage().divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP))
                    .setScale(2, RoundingMode.HALF_UP);

            AffiliateSale affiliateSale =
                    AffiliateSale.builder()
                            .affiliate(affiliate)
                            .order(savedOrder)
                            .saleAmount(order.getTotal())
                            .commissionAmount(commissionAmount)
                            .paid(false)
                            .createdAt(LocalDateTime.now())
                            .build();

            affiliateSaleRepository.save(affiliateSale);
        }

        cartItemRepository.deleteByCartId(cart.getId());

        return mapper.toResponse(savedOrder);

    }

    @Override
    public OrderResponse findById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        return mapper.toResponse(order);
    }

    @Override
    public OrderResponse updateStatus(Long orderId, UpdateOrderStatusRequest request) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));

        OrderStatus current = order.getStatus();
        OrderStatus target = request.status();

        boolean validTransition = switch (current) {

            case PENDENTE -> target == OrderStatus.PAGO || target == OrderStatus.CANCELADO;

            case PAGO -> target == OrderStatus.ENVIADO || target == OrderStatus.CANCELADO;

            case ENVIADO -> target == OrderStatus.ENTREGUE;

            case ENTREGUE, CANCELADO -> false;
        };

        if (!validTransition) {
            throw new InvalidOrderStatusTransitionException(current, target);
        }

        order.setStatus(target);

        order = orderRepository.save(order);

        return mapper.toResponse(order);
    }

    @Override
    public List<OrderResponse> myOrders() {

        String email = jwtService.getCurrentUserEmail();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

        return orderRepository.findByUserId(user.getId())
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public OrderResponse checkout(CreateOrderRequest request) {
        String email = jwtService.getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        return checkout(user.getId(), request);
    }

}