package br.com.george.commerce.controller.admin.catalogo;

import br.com.george.commerce.util.Locations;
import org.springframework.http.ResponseEntity;
import br.com.george.commerce.dto.category.CategoryResponse;
import br.com.george.commerce.dto.category.CreateCategoryRequest;
import br.com.george.commerce.service.CategoryService;
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
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoryService categoryService;

    @Operation(summary = "Cadastrar categoria")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<CategoryResponse> save(@Valid @RequestBody CreateCategoryRequest request) {
        CategoryResponse created = categoryService.save(request);
        return ResponseEntity.created(Locations.of("/categories/{id}", created.id())).body(created);
    }

    @Operation(summary = "Excluir categoria")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualizar categoria")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id,@Valid @RequestBody CreateCategoryRequest request) {
        return ResponseEntity.ok(categoryService.update(id, request));
    }
}
