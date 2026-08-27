package br.com.george.commerce.entity;

import br.com.george.commerce.enums.DiscountType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "affiliates")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Affiliate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, unique = true)
    private String couponCode;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private BigDecimal commissionPercentage;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Column(nullable = false)
    private BigDecimal discountValue;

    @Column(nullable = false)
    private LocalDateTime expiresAt;
}
