package br.com.george.commerce.service;

import br.com.george.commerce.dto.catalog.CatalogFiltersResponse;
import br.com.george.commerce.dto.catalog.CatalogPageResponse;
import br.com.george.commerce.dto.catalog.CatalogProductResponse;
import br.com.george.commerce.dto.catalog.CatalogSearchRequest;

/** Vitrine da loja (público): só produtos publicados. */
public interface CatalogService {

    /** Filtra, ordena e pagina. */
    CatalogPageResponse search(CatalogSearchRequest request);

    CatalogProductResponse findBySlug(String slug);

    /** Opções de filtro que existem nos produtos publicados. */
    CatalogFiltersResponse filters();
}
