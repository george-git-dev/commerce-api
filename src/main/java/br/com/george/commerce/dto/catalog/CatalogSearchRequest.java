package br.com.george.commerce.dto.catalog;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;

/**
 * Filtros, ordem e página do catálogo ({@code GET /catalog/products?category=perfume,kit&sort=price_asc}).
 * Listas aceitam vários valores separados por vírgula e valem como "ou". Tudo opcional.
 *
 * @param q          busca no nome, linha, marca e famílias (sem diferenciar maiúscula nem acento)
 * @param minPrice   o produto entra se ALGUM tamanho estiver na faixa (preço com promoção)
 * @param inStock    só produtos com algum tamanho em estoque
 * @param kind       frasco | decant: só produtos que têm esse formato
 * @param deal       destaques ({@code deal}, {@code launch}, {@code bestSeller}, {@code badge}) valem
 *                   como "ou" entre si: Lançamentos + Ofertas mostra os dois
 * @param sort       relevance (padrão: disponíveis primeiro, depois os mais novos) | price_asc | price_desc |
 *                   best_sellers | newest
 * @param page       começa em 0
 * @param size       1 a 60 (padrão 24)
 */
public record CatalogSearchRequest(
        List<String> category,
        List<String> family,
        List<String> occasion,
        String gender,
        String brand,
        @Min(value = 0, message = "O preço mínimo não pode ser negativo.") BigDecimal minPrice,
        @Min(value = 0, message = "O preço máximo não pode ser negativo.") BigDecimal maxPrice,
        String q,
        Boolean inStock,
        String kind,
        Boolean deal,
        Boolean launch,
        Boolean bestSeller,
        List<String> badge,
        String sort,
        @Min(value = 0, message = "A página começa em 0.") Integer page,
        @Min(value = 1, message = "Informe de 1 a 60 itens por página.")
        @Max(value = 60, message = "Informe de 1 a 60 itens por página.") Integer size) {
}
