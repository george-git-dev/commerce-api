package br.com.george.commerce.dto.catalog;

import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;

/**
 * Opções de filtro que existem nos produtos publicados (o front só mostra o que vem aqui).
 *
 * @param categories slugs, na ordem de cadastro
 * @param minPrice   menor preço (já com promoção) entre todos os tamanhos
 * @param maxPrice   maior preço (já com promoção) entre todos os tamanhos
 */
@Builder
public record CatalogFiltersResponse(List<String> categories, List<String> brands, List<String> families,
        List<String> occasions, List<String> genders, List<String> badges, BigDecimal minPrice,
        BigDecimal maxPrice) {
}
