package br.com.george.commerce.controller.publico.catalogo;

import org.springframework.http.ResponseEntity;
import br.com.george.commerce.dto.product.ProductResponse;
import br.com.george.commerce.service.ProductService;
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
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProdutoPublicoController {

    private final ProductService productService;

    @Operation(summary = "Listar produtos")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @Operation(summary = "Consultar produto")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }
}
