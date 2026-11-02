package br.com.george.commerce.controller.admin.inventario;

import org.springframework.http.ResponseEntity;
import br.com.george.commerce.dto.report.inventory.BrandInventoryStatsResponse;
import br.com.george.commerce.dto.report.inventory.CategoryInventoryStatsResponse;
import br.com.george.commerce.dto.report.inventory.InventoryFilterRequest;
import br.com.george.commerce.dto.report.inventory.InventoryProductResponse;
import br.com.george.commerce.dto.report.inventory.InventorySummaryResponse;
import br.com.george.commerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Administração - Inventário",
        description = "Estoque e indicadores de inventário."
)
@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventarioController {

    private final ProductService productService;

    @Operation(summary = "Listar produtos do estoque")
    @PostMapping("/products")
    @PreAuthorize("hasAnyRole('VIEWER','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<List<InventoryProductResponse>> findProducts(@Valid @RequestBody InventoryFilterRequest request) {
        return ResponseEntity.ok(productService.findInventoryProducts(request));
    }

    @Operation(summary = "Resumo do estoque")
    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('VIEWER','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<InventorySummaryResponse> getSummary() {
        return ResponseEntity.ok(productService.getInventorySummary());
    }

    @Operation(summary = "Estoque por categoria")
    @GetMapping("/statistics/categories")
    @PreAuthorize("hasAnyRole('VIEWER','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<List<CategoryInventoryStatsResponse>> getCategoryStatistics() {
        return ResponseEntity.ok(productService.getCategoryStatistics());
    }

    @Operation(summary = "Estoque por marca")
    @GetMapping("/statistics/brands")
    @PreAuthorize("hasAnyRole('VIEWER','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<List<BrandInventoryStatsResponse>> getBrandStatistics() {
        return ResponseEntity.ok(productService.getBrandStatistics());
    }

}


