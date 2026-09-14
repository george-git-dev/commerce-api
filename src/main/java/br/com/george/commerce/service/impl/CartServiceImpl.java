package br.com.george.commerce.service.impl;

import br.com.george.commerce.dto.cart.CartResponse;
import br.com.george.commerce.dto.cart.CreateCartItemRequest;
import br.com.george.commerce.dto.cart.UpdateCartItemRequest;
import br.com.george.commerce.entity.*;
import br.com.george.commerce.exception.*;
import br.com.george.commerce.mapper.CartMapper;
import br.com.george.commerce.repository.*;
import br.com.george.commerce.service.CartService;
import br.com.george.commerce.service.DiscountService;
import br.com.george.commerce.service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private static final int MAX_ITEMS_PER_PRODUCT = 10;

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final AffiliateRepository affiliateRepository;
    private final CartMapper mapper;
    private final JwtService jwtService;
    private final DiscountService discountService;

    @Override
    public CartResponse addItem(Long userId, CreateCartItemRequest request) {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = Cart.builder().user(user).build();
            return cartRepository.save(newCart);
        });

        Product product = productRepository.findById(request.productId()).orElseThrow(() -> new ProductNotFoundException(request.productId()));

        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());

        if (request.quantity() > MAX_ITEMS_PER_PRODUCT) {
            throw new ProductQuantityLimitExceededException(MAX_ITEMS_PER_PRODUCT);
        }

        if (request.quantity() > product.getStock()) {
            throw new InsufficientStockException(product.getName());
        }

        if (existingItem.isPresent()) {

            CartItem item = existingItem.get();

            int newQuantity = item.getQuantity() + request.quantity();

            if (newQuantity > MAX_ITEMS_PER_PRODUCT) {
                throw new ProductQuantityLimitExceededException(MAX_ITEMS_PER_PRODUCT);
            }

            if (newQuantity > product.getStock()) {
                throw new InsufficientStockException(product.getName());
            }

            item.setQuantity(newQuantity);

            cartItemRepository.save(item);

        } else {

            CartItem item = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.quantity())
                    .build();

            cartItemRepository.save(item);
        }

        CartResponse response = mapper.toResponse(cart);

        return new CartResponse(
                response.id(),
                response.userId(),
                response.userName(),
                calculateTotal(cart),
                cart.getCouponCode(),
                discountService.calculateDiscount(cart),
                discountService.calculateTotalWithDiscount(cart),
                response.items()
        );
    }

    @Override
    public void removeItem(Long userId, Long itemId) {

        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new CartNotFoundException(userId));

        CartItem item = cartItemRepository.findById(itemId).orElseThrow(() -> new CartItemNotFoundException(itemId));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new CartItemNotBelongToCartException();
        }

        cartItemRepository.delete(item);
    }

    @Override
    public CartResponse updateItemQuantity(Long userId, Long itemId, UpdateCartItemRequest request) {

        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new CartNotFoundException(userId));

        CartItem item = cartItemRepository.findById(itemId).orElseThrow(() -> new CartItemNotFoundException(itemId));

        if (request.quantity() > MAX_ITEMS_PER_PRODUCT) {
            throw new ProductQuantityLimitExceededException(MAX_ITEMS_PER_PRODUCT);
        }

        Product product = item.getProduct();

        if (request.quantity() > product.getStock()) {
            throw new InsufficientStockException(product.getName());
        }

        item.setQuantity(request.quantity());

        cartItemRepository.save(item);

        CartResponse response = mapper.toResponse(cart);

        return new CartResponse(
                response.id(),
                response.userId(),
                response.userName(),
                calculateTotal(cart),
                cart.getCouponCode(),
                discountService.calculateDiscount(cart),
                discountService.calculateTotalWithDiscount(cart),
                response.items()
        );
    }

    @Override
    public void clearCart(Long userId) {

        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new CartNotFoundException(userId));

        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());

        cartItemRepository.deleteAll(items);
    }

    private BigDecimal calculateTotal(Cart cart) {
        return cart.getItems()
                .stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public CartResponse myCart() {

        String email = jwtService.getCurrentUserEmail();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

        Cart cart = cartRepository.findByUserId(user.getId()).orElseGet(() -> {
            Cart newCart = Cart.builder().user(user).build();
            return cartRepository.save(newCart);
        });

        CartResponse response = mapper.toResponse(cart);

        return new CartResponse(
                response.id(),
                response.userId(),
                response.userName(),
                calculateTotal(cart),
                cart.getCouponCode(),
                discountService.calculateDiscount(cart),
                discountService.calculateTotalWithDiscount(cart),
                response.items()
        );
    }

    @Override
    public void applyCoupon(String couponCode) {

        String email = jwtService.getCurrentUserEmail();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .user(user)
                            .build();

                    return cartRepository.save(newCart);
                });

        Affiliate affiliate = affiliateRepository.findByCouponCode(couponCode).orElseThrow(CouponNotFoundException::new);

        if (!affiliate.getActive()) {
            throw new AffiliateInactiveException();
        }

        if (affiliate.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new CouponExpiredException();
        }

        cart.setCouponCode(couponCode);

        cartRepository.save(cart);
    }

    @Override
    public CartResponse addItem(CreateCartItemRequest request) {
        String email = jwtService.getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        return addItem(user.getId(), request);
    }

    @Override
    public void removeItem(Long itemId) {
        String email = jwtService.getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        removeItem(user.getId(), itemId);
    }

    @Override
    public CartResponse updateItemQuantity(Long itemId, UpdateCartItemRequest request) {
        String email = jwtService.getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        return updateItemQuantity(user.getId(), itemId, request);
    }

    @Override
    public void clearCart() {
        String email = jwtService.getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        clearCart(user.getId());
    }

}
