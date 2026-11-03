package br.com.george.commerce.dto.catalog;

import java.util.List;
import lombok.Builder;

/**
 * Produto da vitrine, no contrato da loja ({@code Product} do front). Lançamento, mais vendido e
 * preço promocional já vêm calculados.
 *
 * @param name     linha + versão ("Asad Bourbon")
 * @param category slug da categoria ({@code perfume}, {@code kit}…)
 * @param notes    {@code null} quando o produto não tem pirâmide olfativa
 */
@Builder
public record CatalogProductResponse(
        Long id,
        String slug,
        String line,
        String subtitle,
        String name,
        String description,
        Long brandId,
        String brandName,
        String category,
        String gender,
        String concentration,
        List<String> families,
        CatalogNotesResponse notes,
        List<String> kitItems,
        List<String> occasions,
        Boolean launch,
        Boolean bestSeller,
        String badge,
        Integer soldCount,
        List<String> images,
        List<CatalogVariantResponse> variants) {
}
