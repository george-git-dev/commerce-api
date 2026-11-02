package br.com.george.commerce.controller.admin.catalogo;

import br.com.george.commerce.util.Locations;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "Cadastrar marca")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<BrandResponse> save(@Valid @RequestBody CreateBrandRequest request) {
        BrandResponse created = brandService.save(request);
        return ResponseEntity.created(Locations.of("/brands/{id}", created.id())).body(created);
    }

    @Operation(summary = "Atualizar marca")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<BrandResponse> update(@PathVariable Long id, @Valid @RequestBody CreateBrandRequest request) {
        return ResponseEntity.ok(brandService.update(id, request));
    }

    @Operation(summary = "Excluir marca")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        brandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
