package br.com.george.commerce.service.impl;

import br.com.george.commerce.dto.financial.FinancialSummaryRequest;
import br.com.george.commerce.dto.financial.FinancialSummaryResponse;
import br.com.george.commerce.entity.AffiliateSale;
import br.com.george.commerce.entity.Expense;
import br.com.george.commerce.entity.Order;
import br.com.george.commerce.enums.SaleChannel;
import br.com.george.commerce.repository.AffiliateSaleRepository;
import br.com.george.commerce.repository.ExpenseRepository;
import br.com.george.commerce.repository.OrderRepository;
import br.com.george.commerce.service.FinancialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialServiceImpl implements FinancialService {

    private final OrderRepository orderRepository;
    private final ExpenseRepository expenseRepository;
    private final AffiliateSaleRepository affiliateSaleRepository;

    @Override
    public FinancialSummaryResponse summary(FinancialSummaryRequest request) {

        List<Order> orders = orderRepository.findAll();

        if (request.saleChannel() != null) {

            orders = orders.stream()
                    .filter(order -> order.getSaleChannel() == request.saleChannel())
                    .toList();
        }

        List<AffiliateSale> affiliateSales = affiliateSaleRepository.findAll();

        List<Expense> expenses = expenseRepository.findAll();

        if (request.expenseCategory() != null) {

            expenses = expenses.stream()
                    .filter(expense -> expense.getCategory() == request.expenseCategory())
                    .toList();
        }

        if (request.startDate() != null) {

            orders = orders.stream()
                    .filter(order -> !order.getCreatedAt().toLocalDate().isBefore(request.startDate()))
                    .toList();

            expenses = expenses.stream()
                    .filter(expense -> !expense.getExpenseDate().isBefore(request.startDate()))
                    .toList();

            affiliateSales = affiliateSales.stream()
                    .filter(affiliateSale -> !affiliateSale.getCreatedAt().toLocalDate().isBefore(request.startDate()))
                    .toList();
        }

        if (request.endDate() != null) {

            orders = orders.stream()
                    .filter(order -> !order.getCreatedAt().toLocalDate().isAfter(request.endDate()))
                    .toList();

            expenses = expenses.stream()
                    .filter(expense -> !expense.getExpenseDate().isAfter(request.endDate()))
                    .toList();

            affiliateSales = affiliateSales.stream()
                    .filter(affiliateSale -> !affiliateSale.getCreatedAt().toLocalDate().isAfter(request.endDate()))
                    .toList();
        }

        BigDecimal affiliateCommissions = affiliateSales.stream()
                .map(AffiliateSale::getCommissionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal onlineRevenue = orders.stream()
                .filter(order -> order.getSaleChannel() == SaleChannel.ONLINE)
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal manualRevenue = orders.stream()
                .filter(order -> order.getSaleChannel() == SaleChannel.MANUAL)
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalRevenue =
                orders.stream()
                        .map(Order::getTotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses =
                expenses.stream()
                        .map(Expense::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal resultadoBruto = totalRevenue.subtract(totalExpenses);
        BigDecimal resultadoLiquido = resultadoBruto;

        if (Boolean.TRUE.equals(request.includeAffiliateCommissions())) {
            resultadoLiquido = resultadoLiquido.subtract(affiliateCommissions);
        }

        return new FinancialSummaryResponse(
                totalRevenue,
                onlineRevenue,
                manualRevenue,
                totalExpenses,
                affiliateCommissions,
                resultadoBruto,
                resultadoLiquido
        );
    }


}
