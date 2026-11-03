package br.com.george.commerce.repository;

import br.com.george.commerce.entity.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /** Vitrine: só o que está publicado. Variantes, fotos e notas vêm em lote (batch fetch). */
    List<Product> findByStatus(String status);

    Optional<Product> findBySlugAndStatus(String slug, String status);

    @Modifying
    @Query("""
                update Product p
                set p.stock = p.stock - :quantity
                where p.id = :productId
                and p.stock >= :quantity
            """)
    int decreaseStock(
            @Param("productId") Long productId,
            @Param("quantity") Integer quantity
    );
}
