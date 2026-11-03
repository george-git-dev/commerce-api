package br.com.george.commerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer stock;

    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductAttribute> attributes;

    // ---- Catálogo da loja (V4) ----

    /** Identificador da URL: {@code /produtos/lattafa-asad}. */
    private String slug;

    /** Linha do perfume ("Asad"). O nome exibido é linha + versão. */
    private String line;

    /** Versão dentro da linha ("Bourbon", "Elixir"); o original não tem. */
    private String subtitle;

    /** Masculino | Feminino | Unissex. */
    private String gender;

    /** Só perfume ("Eau de Parfum"…). */
    private String concentration;

    /** rascunho | publicado — só publicado aparece na loja. */
    @Builder.Default
    private String status = "rascunho";

    /** Selo manual do card ("Exclusivo"). Lançamento, mais vendido e oferta são calculados. */
    private String badge;

    /** Unidades vendidas (somadas quando o pagamento é confirmado). */
    @Builder.Default
    private Integer soldCount = 0;

    /** Frasco (no máximo um) e decants; preço e estoque são da variante. */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position")
    @Builder.Default
    private List<ProductVariant> variants = new ArrayList<>();

    /** Fotos na ordem da galeria (a primeira é a principal). */
    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @OrderColumn(name = "position")
    @Column(name = "path")
    @Builder.Default
    private List<String> images = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "product_families", joinColumns = @JoinColumn(name = "product_id"))
    @OrderColumn(name = "position")
    @Column(name = "family")
    @Builder.Default
    private List<String> families = new ArrayList<>();

    /** dia | noite. */
    @ElementCollection
    @CollectionTable(name = "product_occasions", joinColumns = @JoinColumn(name = "product_id"))
    @OrderColumn(name = "position")
    @Column(name = "occasion")
    @Builder.Default
    private List<String> occasions = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "product_notes", joinColumns = @JoinColumn(name = "product_id"))
    @OrderColumn(name = "position")
    @Builder.Default
    private List<ProductNote> notes = new ArrayList<>();

    /** Só kit: o que vem na caixa. */
    @ElementCollection
    @CollectionTable(name = "product_kit_items", joinColumns = @JoinColumn(name = "product_id"))
    @OrderColumn(name = "position")
    @Column(name = "item")
    @Builder.Default
    private List<String> kitItems = new ArrayList<>();

    /** Nome exibido: linha + versão ("Asad Bourbon"); produto antigo sem linha usa o {@code name}. */
    public String displayName() {
        if (line == null || line.isBlank()) {
            return name;
        }
        return subtitle == null || subtitle.isBlank() ? line : line + " " + subtitle;
    }
}
