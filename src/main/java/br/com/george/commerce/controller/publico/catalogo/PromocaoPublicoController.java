package br.com.george.commerce.controller.publico.catalogo;

import org.springframework.http.ResponseEntity;
import br.com.george.commerce.dto.promotion.PromotionResponse;
import br.com.george.commerce.service.PromotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Público - Catálogo",
        description = "Consulta de produtos, categorias, marcas e promoções."
)
@RestController
@RequestMapping("/promotions")
@RequiredArgsConstructor
public class PromocaoPublicoController {

    private final PromotionService promotionService;

    @Operation(summary = "Listar promoções")
    @GetMapping
    public ResponseEntity<List<PromotionResponse>> findAll() {
        return ResponseEntity.ok(promotionService.findAll());
    }

    @Operation(summary = "Consultar promoção")
    @GetMapping("/{id}")
    public ResponseEntity<PromotionResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(promotionService.findById(id));
    }
}
