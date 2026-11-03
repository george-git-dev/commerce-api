package br.com.george.commerce.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    /** Identificador da URL e do contrato da loja ({@code perfume}, {@code kit}…). */
    private String slug;

    private Boolean active;
}
