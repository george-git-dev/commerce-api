package br.com.george.commerce.controller.admin.relatorio;

import br.com.george.commerce.dto.report.financial.ExpenseCategoryStatsResponse;
import br.com.george.commerce.service.SalesReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Administração - Relatórios Financeiros",
        description = "Indicadores financeiros consolidados."
)
@RestController
@RequestMapping("/reports/financial")
@RequiredArgsConstructor
public class RelatorioFinanceiroController {

    private final SalesReportService salesReportService;

    @GetMapping("/expenses/categories")
    @PreAuthorize("hasAnyRole('VIEWER','ADMIN','SUPER_ADMIN')")
    public List<ExpenseCategoryStatsResponse> getExpenseCategoryStatistics() {
        return salesReportService.getExpenseCategoryStatistics();
    }
}



