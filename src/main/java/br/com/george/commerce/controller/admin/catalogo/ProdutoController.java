package br.com.george.commerce.controller.admin.catalogo;

import br.com.george.commerce.util.Locations;
import org.springframework.http.ResponseEntity;
import br.com.george.commerce.dto.product.CreateProductRequest;
import br.com.george.commerce.dto.product.ProductResponse;
import br.com.george.commerce.service.ProductService;
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
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProductService productService;

    @Operation(summary = "Cadastrar produto")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ProductResponse> save(@Valid @RequestBody CreateProductRequest request) {
        ProductResponse created = productService.save(request);
        return ResponseEntity.created(Locations.of("/products/{id}", created.id())).body(created);
    }

    @Operation(summary = "Atualizar produto")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @Valid @RequestBody CreateProductRequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    @Operation(summary = "Excluir produto")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
