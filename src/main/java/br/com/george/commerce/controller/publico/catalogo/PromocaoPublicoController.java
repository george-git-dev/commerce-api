package br.com.george.commerce.controller.publico.catalogo;

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

    @GetMapping
    @Operation(summary = "Listar promoções")
    public List<PromotionResponse> findAll() {
        return promotionService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar promoção por ID")
    public PromotionResponse findById(@PathVariable Long id) {
        return promotionService.findById(id);
    }
}
