package br.com.george.commerce.controller.admin.catalogo;

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

    @PostMapping
    @Operation(summary = "Cadastrar categoria")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public CategoryResponse save(@Valid @RequestBody CreateCategoryRequest request) {
        return categoryService.save(request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir categoria")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar categoria")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public CategoryResponse update(@PathVariable Long id,@Valid @RequestBody CreateCategoryRequest request) {
        return categoryService.update(id, request);
    }
}
