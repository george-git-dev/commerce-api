package br.com.george.commerce.controller.publico.catalogo;

import br.com.george.commerce.dto.brand.BrandResponse;
import br.com.george.commerce.service.BrandService;
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
@RequestMapping("/brands")
@RequiredArgsConstructor
public class MarcaPublicoController {

    private final BrandService brandService;

    @GetMapping
    @Operation(summary = "Listar marcas")
    public List<BrandResponse> findAll() {
        return brandService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar marca por ID")
    public BrandResponse findById(@PathVariable Long id) {
        return brandService.findById(id);
    }
}
