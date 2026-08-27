package br.com.george.commerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "affiliate_sales")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AffiliateSale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "affiliate_id")
    private Affiliate affiliate;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private BigDecimal saleAmount;

    private BigDecimal commissionAmount;

    private Boolean paid;

    private LocalDateTime createdAt;

    private LocalDateTime paidAt;
}

