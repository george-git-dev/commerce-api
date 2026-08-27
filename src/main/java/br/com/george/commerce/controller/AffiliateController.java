package br.com.george.commerce.controller;

import br.com.george.commerce.dto.affiliate.*;
import br.com.george.commerce.service.AffiliateSaleService;
import br.com.george.commerce.service.AffiliateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/affiliates")
@RequiredArgsConstructor
public class AffiliateController {

    private final AffiliateService affiliateService;
    private final AffiliateSaleService affiliateSaleService;

    @PostMapping("/me")
    public AffiliateResponse becomeAffiliate() {
        return affiliateService.becomeAffiliate();
    }

    @GetMapping("/me")
    public MyAffiliateResponse myAffiliate() {
        return affiliateService.myAffiliate();
    }

    @GetMapping("/me/sales")
    public List<AffiliateSaleResponse> mySales() {
        return affiliateService.mySales();
    }

    @PostMapping("/me/summary")
    public AffiliateSummaryResponse mySummary(@RequestBody AffiliateReportRequest request) {
        return affiliateService.mySummary(request);
    }

    @PatchMapping("/sales/{saleId}/pay")
    public void markAsPaid(@PathVariable Long saleId) {
        affiliateSaleService.markAsPaid(saleId);
    }

}
