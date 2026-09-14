package br.com.george.commerce.dto.expense;

import br.com.george.commerce.enums.ExpenseCategory;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateExpenseRequest(

        String description,

        BigDecimal amount,

        ExpenseCategory category,

        LocalDate expenseDate

) {
}
