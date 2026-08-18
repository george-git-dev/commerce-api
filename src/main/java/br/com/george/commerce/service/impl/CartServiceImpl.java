package br.com.george.commerce.service.impl;

import br.com.george.commerce.dto.cart.CartResponse;
import br.com.george.commerce.dto.cart.CreateCartItemRequest;
import br.com.george.commerce.dto.cart.UpdateCartItemRequest;
import br.com.george.commerce.entity.Cart;
import br.com.george.commerce.entity.CartItem;
import br.com.george.commerce.entity.Product;
import br.com.george.commerce.entity.User;
import br.com.george.commerce.exception.*;
import br.com.george.commerce.mapper.CartMapper;
import br.com.george.commerce.repository.CartItemRepository;
import br.com.george.commerce.repository.CartRepository;
import br.com.george.commerce.repository.ProductRepository;
import br.com.george.commerce.repository.UserRepository;
import br.com.george.commerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private final CartMapper mapper;

    @Override
    public CartResponse findByUser(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new CartNotFoundException(userId));
        CartResponse response = mapper.toResponse(cart);

        return new CartResponse(
                response.id(),
                response.userId(),
                response.userName(),
                calculateTotal(cart),
                response.items()
        );
    }

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
                response.items()
        );
    }

    @Override
    public void removeItem(Long userId, Long itemId) {

        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new CartNotFoundException(userId));

        CartItem item = cartItemRepository.findById(itemId).orElseThrow(() -> new CartItemNotFoundException(itemId));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Item does not belong to cart");
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
}
