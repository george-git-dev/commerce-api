package br.com.george.commerce.controller.admin.relatorio;

import org.springframework.http.ResponseEntity;
import br.com.george.commerce.dto.report.affiliate.AffiliateRankingResponse;
import br.com.george.commerce.dto.report.sales.SalesReportRequest;
import br.com.george.commerce.service.SalesReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Administração - Relatórios de Afiliados",
        description = "Rankings e estatísticas de afiliados."
)
@RestController
@RequestMapping("/reports/affiliates")
@RequiredArgsConstructor
public class RelatorioAfiliadoController {

    private final SalesReportService salesReportService;

    @Operation(summary = "Ranking de afiliados")
    @PostMapping("/ranking")
    @PreAuthorize("hasAnyRole('VIEWER','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<List<AffiliateRankingResponse>> ranking(@RequestBody SalesReportRequest request) {
        return ResponseEntity.ok(salesReportService.getAffiliateRanking(request));
    }
}



