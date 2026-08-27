package br.com.george.commerce.service;

import br.com.george.commerce.dto.affiliate.*;

import java.util.List;

public interface AffiliateService {

    MyAffiliateResponse myAffiliate();

    List<AffiliateSaleResponse> mySales();

    AffiliateResponse becomeAffiliate();

    AffiliateSummaryResponse mySummary(AffiliateReportRequest request);
}
