package br.com.george.commerce.controller.admin.catalogo;

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

    @PostMapping
    @Operation(summary = "Cadastrar promoção")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public PromotionResponse save(@Valid @RequestBody CreatePromotionRequest request) {
        return promotionService.save(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar promoção")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public PromotionResponse update(@PathVariable Long id, @Valid @RequestBody CreatePromotionRequest request) {
        return promotionService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir promoção")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public void delete(@PathVariable Long id) {
        promotionService.delete(id);
    }

    @PostMapping("/link-product")
    @Operation(summary = "Vincular promoção a produto")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public void linkToProduct(@Valid @RequestBody LinkPromotionToProductRequest request) {
        promotionService.linkToProduct(request);
    }
}
