package br.com.george.commerce.dto.financial;

import br.com.george.commerce.enums.ExpenseCategory;
import br.com.george.commerce.enums.SaleChannel;

import java.time.LocalDate;

public record FinancialSummaryRequest(

        LocalDate startDate,

        LocalDate endDate,

        SaleChannel saleChannel,

        ExpenseCategory expenseCategory,

        Boolean includeAffiliateCommissions

) {
}

