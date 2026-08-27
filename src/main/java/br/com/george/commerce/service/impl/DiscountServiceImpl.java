package br.com.george.commerce.service.impl;

import br.com.george.commerce.entity.Affiliate;
import br.com.george.commerce.entity.Cart;
import br.com.george.commerce.enums.DiscountType;
import br.com.george.commerce.exception.CouponNotFoundException;
import br.com.george.commerce.repository.AffiliateRepository;
import br.com.george.commerce.repository.CartRepository;
import br.com.george.commerce.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private final AffiliateRepository affiliateRepository;
    private final CartRepository cartRepository;


    @Override
    public BigDecimal calculateDiscount(Cart cart) {

        if (cart.getCouponCode() == null) {
            return BigDecimal.ZERO;
        }

        Affiliate affiliate = affiliateRepository.findByCouponCode(cart.getCouponCode()).orElseThrow(CouponNotFoundException::new);

        if (!affiliate.getActive()) {
            cart.setCouponCode(null);
            cartRepository.save(cart);

            return BigDecimal.ZERO;
        }

        if (affiliate.getExpiresAt().isBefore(LocalDateTime.now())) {
            cart.setCouponCode(null);
            cartRepository.save(cart);

            return BigDecimal.ZERO;
        }

        BigDecimal total = calculateTotal(cart);

        if (affiliate.getDiscountType() == DiscountType.PERCENTAGE) {

            return total.multiply(affiliate.getDiscountValue()
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return affiliate.getDiscountValue()
                .min(total)
                .setScale(2, RoundingMode.HALF_UP);
    }



    @Override
    public BigDecimal calculateTotalWithDiscount(Cart cart) {
        return calculateTotal(cart).subtract(calculateDiscount(cart)).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateTotal(Cart cart) {

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            return BigDecimal.ZERO;
        }

        return cart.getItems().stream().map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
