package br.com.george.commerce.controller.me;

import br.com.george.commerce.util.Locations;
import org.springframework.http.ResponseEntity;
import br.com.george.commerce.dto.affiliate.*;
import br.com.george.commerce.service.AffiliateSaleService;
import br.com.george.commerce.service.AffiliateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Área do Cliente - Afiliado",
        description = "Área do afiliado autenticado, vendas e comissões."
)
@RestController
@RequestMapping("/affiliates")
@RequiredArgsConstructor

public class MeuAfiliadoController {

    private final AffiliateService affiliateService;
    private final AffiliateSaleService affiliateSaleService;

    @Operation(summary = "Tornar-se afiliado")
    @PostMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AffiliateResponse> becomeAffiliate() {
        AffiliateResponse created = affiliateService.becomeAffiliate();
        return ResponseEntity.created(Locations.of("/affiliates/me")).body(created);
    }

    @Operation(summary = "Meus dados de afiliado")
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MyAffiliateResponse> myAffiliate() {
        return ResponseEntity.ok(affiliateService.myAffiliate());
    }

    @Operation(summary = "Minhas vendas como afiliado")
    @GetMapping("/me/sales")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AffiliateSaleResponse>> mySales() {
        return ResponseEntity.ok(affiliateService.mySales());
    }

    @Operation(summary = "Resumo das minhas comissões")
    @PostMapping("/me/summary")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AffiliateSummaryResponse> mySummary(@RequestBody AffiliateReportRequest request) {
        return ResponseEntity.ok(affiliateService.mySummary(request));
    }
}


