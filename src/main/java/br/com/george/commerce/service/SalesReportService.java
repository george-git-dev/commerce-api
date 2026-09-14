package br.com.george.commerce.service;

import br.com.george.commerce.dto.report.affiliate.AffiliateRankingResponse;
import br.com.george.commerce.dto.report.customer.CustomerStatsResponse;
import br.com.george.commerce.dto.report.financial.ExpenseCategoryStatsResponse;
import br.com.george.commerce.dto.report.sales.*;

import java.util.List;

public interface SalesReportService {

    SalesSummaryResponse summary(SalesReportRequest request);

    List<TopProductResponse> getTopProducts(SalesReportRequest request);

    List<TopCategoryResponse> getTopCategories(SalesReportRequest request);

    List<TopBrandResponse> getTopBrands(SalesReportRequest request);

    List<AffiliateRankingResponse> getAffiliateRanking(SalesReportRequest request);

    List<CustomerStatsResponse> getCustomerStatistics();

    List<ExpenseCategoryStatsResponse> getExpenseCategoryStatistics();
}
