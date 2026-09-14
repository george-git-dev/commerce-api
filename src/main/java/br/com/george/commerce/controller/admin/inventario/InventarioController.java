package br.com.george.commerce.controller.admin.inventario;

import br.com.george.commerce.dto.report.inventory.BrandInventoryStatsResponse;
import br.com.george.commerce.dto.report.inventory.CategoryInventoryStatsResponse;
import br.com.george.commerce.dto.report.inventory.InventoryFilterRequest;
import br.com.george.commerce.dto.report.inventory.InventoryProductResponse;
import br.com.george.commerce.dto.report.inventory.InventorySummaryResponse;
import br.com.george.commerce.service.ProductService;
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

    @PostMapping("/products")
    @PreAuthorize("hasAnyRole('VIEWER','ADMIN','SUPER_ADMIN')")
    public List<InventoryProductResponse> findProducts(@Valid @RequestBody InventoryFilterRequest request) {
        return productService.findInventoryProducts(request);
    }

    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('VIEWER','ADMIN','SUPER_ADMIN')")
    public InventorySummaryResponse getSummary() {
        return productService.getInventorySummary();
    }

    @GetMapping("/statistics/categories")
    @PreAuthorize("hasAnyRole('VIEWER','ADMIN','SUPER_ADMIN')")
    public List<CategoryInventoryStatsResponse> getCategoryStatistics() {
        return productService.getCategoryStatistics();
    }

    @GetMapping("/statistics/brands")
    @PreAuthorize("hasAnyRole('VIEWER','ADMIN','SUPER_ADMIN')")
    public List<BrandInventoryStatsResponse> getBrandStatistics() {
        return productService.getBrandStatistics();
    }

}


