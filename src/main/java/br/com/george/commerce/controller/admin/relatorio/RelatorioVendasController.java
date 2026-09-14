package br.com.george.commerce.controller.admin.relatorio;

import br.com.george.commerce.dto.report.sales.*;
import br.com.george.commerce.service.SalesReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Administração - Relatórios de Vendas",
        description = "Indicadores e métricas de vendas."
)
@RestController
@RequestMapping("/reports/sales")
@RequiredArgsConstructor
public class RelatorioVendasController {

    private final SalesReportService salesReportService;

    @PostMapping
    @PreAuthorize("hasAnyRole('VIEWER','ADMIN','SUPER_ADMIN')")
    public SalesSummaryResponse summary(@RequestBody SalesReportRequest request) {
        return salesReportService.summary(request);
    }

    @PostMapping("/top-products")
    @PreAuthorize("hasAnyRole('VIEWER','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<List<TopProductResponse>> topProducts(@RequestBody SalesReportRequest request) {
        return ResponseEntity.ok(salesReportService.getTopProducts(request));
    }

    @PostMapping("/top-categories")
    @PreAuthorize("hasAnyRole('VIEWER','ADMIN','SUPER_ADMIN')")
    public List<TopCategoryResponse> topCategories(@RequestBody SalesReportRequest request) {
        return salesReportService.getTopCategories(request);
    }

    @PostMapping("/top-brands")
    @PreAuthorize("hasAnyRole('VIEWER','ADMIN','SUPER_ADMIN')")
    public List<TopBrandResponse> topBrands(@RequestBody SalesReportRequest request) {
        return salesReportService.getTopBrands(request);
    }
}


