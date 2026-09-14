package br.com.george.commerce.controller.admin.catalogo;

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

    @PostMapping
    @Operation(summary = "Cadastrar produto")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ProductResponse save(@Valid @RequestBody CreateProductRequest request) {
        return productService.save(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody CreateProductRequest request) {
        return productService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir produto")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}
