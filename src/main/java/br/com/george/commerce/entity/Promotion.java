package br.com.george.commerce.entity;

import br.com.george.commerce.enums.DiscountType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "promotions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Boolean active;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    private BigDecimal discountValue;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    public boolean isActiveNow() {
        LocalDateTime now = LocalDateTime.now();
        return Boolean.TRUE.equals(active) && !now.isBefore(startAt) && !now.isAfter(endAt);
    }
}
