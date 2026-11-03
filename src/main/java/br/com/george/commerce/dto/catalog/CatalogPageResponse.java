package br.com.george.commerce.dto.catalog;

import java.util.List;
import lombok.Builder;

/**
 * Uma página do catálogo.
 *
 * @param page       página atual (começa em 0)
 * @param totalItems quantos produtos passam nos filtros (todas as páginas)
 */
@Builder
public record CatalogPageResponse(List<CatalogProductResponse> items, int page, int size, long totalItems,
        int totalPages) {
}
