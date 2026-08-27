package br.com.george.commerce.repository;

import br.com.george.commerce.entity.ProductPromotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductPromotionRepository extends JpaRepository<ProductPromotion, Long> {

    Optional<ProductPromotion> findByProductId(Long productId);
}
