package br.com.george.commerce.service.impl;

import br.com.george.commerce.dto.report.affiliate.AffiliateRankingResponse;
import br.com.george.commerce.dto.report.customer.CustomerStatsResponse;
import br.com.george.commerce.dto.report.financial.ExpenseCategoryStatsResponse;
import br.com.george.commerce.dto.report.sales.*;
import br.com.george.commerce.entity.*;
import br.com.george.commerce.enums.SaleChannel;
import br.com.george.commerce.repository.AffiliateSaleRepository;
import br.com.george.commerce.repository.ExpenseRepository;
import br.com.george.commerce.repository.OrderRepository;
import br.com.george.commerce.service.SalesReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesReportServiceImpl implements SalesReportService {

    private final OrderRepository orderRepository;
    private final AffiliateSaleRepository affiliateSaleRepository;
    private final ExpenseRepository expenseRepository;

    @Override
    public SalesSummaryResponse summary(SalesReportRequest request) {

        List<Order> orders = getFilteredOrders(request);

        long totalSales = orders.size();

        long onlineSales = orders.stream()
                .filter(order ->
                        order.getSaleChannel() == SaleChannel.ONLINE)
                .count();

        long manualSales = orders.stream()
                .filter(order ->
                        order.getSaleChannel() == SaleChannel.MANUAL)
                .count();

        BigDecimal totalRevenue =
                orders.stream()
                        .map(Order::getTotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal onlineRevenue = orders.stream()
                .filter(order ->
                        order.getSaleChannel() == SaleChannel.ONLINE)
                .map(Order::getTotal)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );

        BigDecimal manualRevenue = orders.stream()
                .filter(order ->
                        order.getSaleChannel() == SaleChannel.MANUAL)
                .map(Order::getTotal)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );

        BigDecimal averageTicket = totalSales == 0 ? BigDecimal.ZERO : totalRevenue.divide(BigDecimal.valueOf(totalSales), 2, RoundingMode.HALF_UP);

        return new SalesSummaryResponse(
                totalSales,
                onlineSales,
                manualSales,
                totalRevenue,
                onlineRevenue,
                manualRevenue,
                averageTicket
        );
    }

    @Override
    public List<TopProductResponse> getTopProducts(SalesReportRequest request) {

        List<Order> orders = getFilteredOrders(request);

        return orders.stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(OrderItem::getProductName))
                .entrySet()
                .stream()
                .map(entry -> {

                    Long quantitySold = entry.getValue()
                            .stream()
                            .mapToLong(OrderItem::getQuantity)
                            .sum();

                    BigDecimal revenue = entry.getValue()
                            .stream()
                            .map(OrderItem::getSubtotal)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return new TopProductResponse(
                            entry.getKey(),
                            quantitySold,
                            revenue
                    );
                })
                .sorted(Comparator.comparing(TopProductResponse::quantitySold).reversed())
                .toList();
    }

    private List<Order> getFilteredOrders(SalesReportRequest request) {

        List<Order> orders = orderRepository.findAll();

        if (request.saleChannel() != null) {

            orders = orders.stream()
                    .filter(order -> order.getSaleChannel() == request.saleChannel())
                    .toList();
        }

        if (request.startDate() != null) {

            orders = orders.stream()
                    .filter(order -> !order.getCreatedAt().toLocalDate().isBefore(request.startDate()))
                    .toList();
        }

        if (request.endDate() != null) {

            orders = orders.stream()
                    .filter(order -> !order.getCreatedAt().toLocalDate().isAfter(request.endDate()))
                    .toList();
        }

        return orders;
    }

    @Override
    public List<TopCategoryResponse> getTopCategories(SalesReportRequest request) {

        List<Order> orders = getFilteredOrders(request);

        return orders.stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(item ->
                        item.getCategoryName() == null
                                ? "SEM CATEGORIA"
                                : item.getCategoryName()
                ))
                .entrySet()
                .stream()
                .map(entry -> {

                    Long quantitySold = entry.getValue()
                            .stream()
                            .mapToLong(OrderItem::getQuantity)
                            .sum();

                    BigDecimal revenue = entry.getValue()
                            .stream()
                            .map(OrderItem::getSubtotal)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return new TopCategoryResponse(
                            entry.getKey(),
                            quantitySold,
                            revenue
                    );
                })
                .sorted(Comparator.comparing(
                                TopCategoryResponse::quantitySold)
                        .reversed())
                .toList();
    }

    @Override
    public List<TopBrandResponse> getTopBrands(SalesReportRequest request) {

        List<Order> orders = getFilteredOrders(request);

        return orders.stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(item ->
                        item.getBrandName() == null
                                ? "SEM MARCA"
                                : item.getBrandName()
                ))
                .entrySet()
                .stream()
                .map(entry -> {

                    Long quantitySold = entry.getValue()
                            .stream()
                            .mapToLong(OrderItem::getQuantity)
                            .sum();

                    BigDecimal revenue = entry.getValue()
                            .stream()
                            .map(OrderItem::getSubtotal)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return new TopBrandResponse(
                            entry.getKey(),
                            quantitySold,
                            revenue
                    );
                })
                .sorted(Comparator.comparing(
                                TopBrandResponse::quantitySold)
                        .reversed())
                .toList();
    }

    @Override
    public List<AffiliateRankingResponse> getAffiliateRanking(SalesReportRequest request) {

        List<AffiliateSale> affiliateSales = affiliateSaleRepository.findAll();

        if (request.startDate() != null) {

            affiliateSales = affiliateSales.stream()
                    .filter(sale ->
                            !sale.getCreatedAt()
                                    .toLocalDate()
                                    .isBefore(request.startDate()))
                    .toList();
        }

        if (request.endDate() != null) {

            affiliateSales = affiliateSales.stream()
                    .filter(sale ->
                            !sale.getCreatedAt()
                                    .toLocalDate()
                                    .isAfter(request.endDate()))
                    .toList();
        }

        Map<Affiliate, List<AffiliateSale>> salesByAffiliate = affiliateSales.stream().collect(Collectors.groupingBy(AffiliateSale::getAffiliate));

        return salesByAffiliate.entrySet()
                .stream()
                .map(entry -> {

                    Affiliate affiliate = entry.getKey();

                    Long totalSales = (long) entry.getValue().size();

                    BigDecimal totalRevenue = entry.getValue()
                            .stream()
                            .map(AffiliateSale::getSaleAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal totalCommission = entry.getValue()
                            .stream()
                            .map(AffiliateSale::getCommissionAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return new AffiliateRankingResponse(
                            affiliate.getId(),
                            affiliate.getUser().getName(),
                            totalSales,
                            totalRevenue,
                            totalCommission
                    );
                })
                .sorted(Comparator.comparing(AffiliateRankingResponse::totalSales).reversed())
                .toList();
    }

    @Override
    public List<CustomerStatsResponse> getCustomerStatistics() {

        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .filter(order -> order.getUser() != null)
                .collect(Collectors.groupingBy(Order::getUser))
                .entrySet()
                .stream()
                .map(entry -> {

                    User user = entry.getKey();

                    Long totalOrders = (long) entry.getValue().size();

                    BigDecimal totalSpent = entry.getValue()
                            .stream()
                            .map(Order::getTotal)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    LocalDateTime lastPurchase = entry.getValue()
                            .stream()
                            .map(Order::getCreatedAt)
                            .max(LocalDateTime::compareTo)
                            .orElse(null);

                    return new CustomerStatsResponse(
                            user.getId(),
                            user.getName(),
                            totalOrders,
                            totalSpent,
                            lastPurchase
                    );
                })
                .sorted(
                        Comparator.comparing(
                                CustomerStatsResponse::totalSpent
                        ).reversed()
                )
                .toList();
    }

    @Override
    public List<ExpenseCategoryStatsResponse> getExpenseCategoryStatistics() {

        return expenseRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory
                ))
                .entrySet()
                .stream()
                .map(entry -> {

                    BigDecimal totalAmount = entry.getValue()
                            .stream()
                            .map(Expense::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return new ExpenseCategoryStatsResponse(
                            entry.getKey().name(),
                            totalAmount
                    );
                })
                .sorted(
                        Comparator.comparing(
                                ExpenseCategoryStatsResponse::totalAmount
                        ).reversed()
                )
                .toList();
    }
}