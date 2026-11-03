package br.com.george.commerce.controller.publico.catalogo;

import br.com.george.commerce.dto.catalog.CatalogFiltersResponse;
import br.com.george.commerce.dto.catalog.CatalogPageResponse;
import br.com.george.commerce.dto.catalog.CatalogProductResponse;
import br.com.george.commerce.dto.catalog.CatalogSearchRequest;
import br.com.george.commerce.service.CatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Vitrine da loja: só produtos publicados, com filtro, busca, ordem e página feitos aqui. */
@Tag(name = "Público - Vitrine", description = "Produtos publicados da loja, no formato da vitrine.")
@RestController
@RequestMapping("/catalog")
@RequiredArgsConstructor
public class VitrineController {

    private final CatalogService catalogService;

    @Operation(summary = "Buscar produtos (filtros, ordem e página)")
    @GetMapping("/products")
    public ResponseEntity<CatalogPageResponse> search(@Valid @ParameterObject CatalogSearchRequest request) {
        return ResponseEntity.ok(catalogService.search(request));
    }

    @Operation(summary = "Consultar produto pelo slug")
    @GetMapping("/products/{slug}")
    public ResponseEntity<CatalogProductResponse> findBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(catalogService.findBySlug(slug));
    }

    @Operation(summary = "Opções de filtro")
    @GetMapping("/filters")
    public ResponseEntity<CatalogFiltersResponse> filters() {
        return ResponseEntity.ok(catalogService.filters());
    }
}
