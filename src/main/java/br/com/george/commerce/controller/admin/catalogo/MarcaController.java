package br.com.george.commerce.controller.admin.catalogo;

import br.com.george.commerce.dto.brand.BrandResponse;
import br.com.george.commerce.dto.brand.CreateBrandRequest;
import br.com.george.commerce.service.BrandService;
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
@RequestMapping("/brands")
@RequiredArgsConstructor
public class MarcaController {

    private final BrandService brandService;

    @PostMapping
    @Operation(summary = "Cadastrar marca")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public BrandResponse save(@Valid @RequestBody CreateBrandRequest request) {
        return brandService.save(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar marca")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public BrandResponse update(@PathVariable Long id, @Valid @RequestBody CreateBrandRequest request) {
        return brandService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir marca")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public void delete(@PathVariable Long id) {
        brandService.delete(id);
    }
}
