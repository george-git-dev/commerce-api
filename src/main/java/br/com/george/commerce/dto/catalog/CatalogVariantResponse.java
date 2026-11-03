package br.com.george.commerce.dto.catalog;

import java.math.BigDecimal;
import lombok.Builder;

/**
 * Um tamanho do produto.
 *
 * @param id         SKU
 * @param kind       frasco | decant
 * @param promoPrice preço com a promoção no ar (calculado aqui); {@code null} = sem promoção
 */
@Builder
public record CatalogVariantResponse(String id, String kind, Integer volumeMl, BigDecimal price,
        BigDecimal promoPrice, Integer stock) {
}
