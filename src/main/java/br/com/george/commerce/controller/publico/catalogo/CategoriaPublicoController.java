package br.com.george.commerce.controller.publico.catalogo;

import br.com.george.commerce.dto.category.CategoryResponse;
import br.com.george.commerce.service.CategoryService;
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
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoriaPublicoController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Listar categorias")
    public List<CategoryResponse> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar categoria por ID")
    public CategoryResponse findById(@PathVariable Long id) {
        return categoryService.findById(id);
    }
}
