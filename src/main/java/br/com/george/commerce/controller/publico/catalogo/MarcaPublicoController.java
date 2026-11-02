package br.com.george.commerce.controller.publico.catalogo;

import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "Listar marcas")
    @GetMapping
    public ResponseEntity<List<BrandResponse>> findAll() {
        return ResponseEntity.ok(brandService.findAll());
    }

    @Operation(summary = "Consultar marca")
    @GetMapping("/{id}")
    public ResponseEntity<BrandResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(brandService.findById(id));
    }
}
