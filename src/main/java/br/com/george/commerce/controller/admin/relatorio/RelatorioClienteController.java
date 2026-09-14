package br.com.george.commerce.controller.admin.relatorio;

import br.com.george.commerce.dto.report.customer.CustomerStatsResponse;
import br.com.george.commerce.service.SalesReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Administração - Relatórios de Clientes",
        description = "Indicadores e estatísticas de clientes."
)
@RestController
@RequestMapping("/reports/customers")
@RequiredArgsConstructor
public class RelatorioClienteController {

    private final SalesReportService salesReportService;

    @GetMapping
    @PreAuthorize("hasAnyRole('VIEWER','ADMIN','SUPER_ADMIN')")
    public List<CustomerStatsResponse> getCustomerStatistics() {
        return salesReportService.getCustomerStatistics();
    }
}



