package br.com.george.commerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/** O que o cliente compra: um tamanho do produto, com SKU, preço e estoque próprios. */
@Entity
@Table(name = "product_variants")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String sku;

    /** frasco | decant. */
    private String kind;

    private Integer volumeMl;

    private BigDecimal price;

    private Integer stock;

    private Integer position;
}
