package br.com.george.commerce.controller.me;

import br.com.george.commerce.dto.affiliate.*;
import br.com.george.commerce.service.AffiliateSaleService;
import br.com.george.commerce.service.AffiliateService;
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

    @PostMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public AffiliateResponse becomeAffiliate() {
        return affiliateService.becomeAffiliate();
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public MyAffiliateResponse myAffiliate() {
        return affiliateService.myAffiliate();
    }

    @GetMapping("/me/sales")
    @PreAuthorize("isAuthenticated()")
    public List<AffiliateSaleResponse> mySales() {
        return affiliateService.mySales();
    }

    @PostMapping("/me/summary")
    @PreAuthorize("isAuthenticated()")
    public AffiliateSummaryResponse mySummary(@RequestBody AffiliateReportRequest request) {
        return affiliateService.mySummary(request);
    }
}


