package br.com.george.commerce.controller.admin.financeiro;

import org.springframework.http.ResponseEntity;
import br.com.george.commerce.dto.financial.FinancialSummaryRequest;
import br.com.george.commerce.dto.financial.FinancialSummaryResponse;
import br.com.george.commerce.service.FinancialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Administração - Financeiro",
        description = "Resumo financeiro e indicadores."
)
@RestController
@RequestMapping("/financial")
@RequiredArgsConstructor
public class FinanceiroController {

    private final FinancialService financialService;

    @Operation(summary = "Resumo financeiro do período")
    @PostMapping("/summary")
    @PreAuthorize("hasAnyRole('VIEWER','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<FinancialSummaryResponse> summary(@RequestBody FinancialSummaryRequest request) {
        return ResponseEntity.ok(financialService.summary(request));
    }
}

