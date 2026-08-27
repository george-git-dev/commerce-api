package br.com.george.commerce.repository;

import br.com.george.commerce.entity.Affiliate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AffiliateRepository extends JpaRepository<Affiliate, Long> {

    Optional<Affiliate> findByUserId(Long userId);

    Optional<Affiliate> findByCouponCode(String couponCode);

    List<Affiliate> findByActiveTrue();
}
