package br.com.george.commerce.controller.admin.catalogo;

import br.com.george.commerce.util.Locations;
import org.springframework.http.ResponseEntity;
import br.com.george.commerce.dto.promotion.CreatePromotionRequest;
import br.com.george.commerce.dto.promotion.LinkPromotionToProductRequest;
import br.com.george.commerce.dto.promotion.PromotionResponse;
import br.com.george.commerce.service.PromotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Administração - Catálogo",
        description = "Cadastro, atualização e exclusão de produtos, categorias, marcas e promoções."
)
@RestController
@RequestMapping("/promotions")
@RequiredArgsConstructor
public class PromocaoController {

    private final PromotionService promotionService;

    @Operation(summary = "Cadastrar promoção")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<PromotionResponse> save(@Valid @RequestBody CreatePromotionRequest request) {
        PromotionResponse created = promotionService.save(request);
        return ResponseEntity.created(Locations.of("/promotions/{id}", created.id())).body(created);
    }

    @Operation(summary = "Atualizar promoção")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<PromotionResponse> update(@PathVariable Long id, @Valid @RequestBody CreatePromotionRequest request) {
        return ResponseEntity.ok(promotionService.update(id, request));
    }

    @Operation(summary = "Excluir promoção")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        promotionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Vincular promoção a produto")
    @PostMapping("/link-product")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<Void> linkToProduct(@Valid @RequestBody LinkPromotionToProductRequest request) {
        promotionService.linkToProduct(request);
        return ResponseEntity.noContent().build();
    }
}
