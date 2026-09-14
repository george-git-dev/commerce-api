package br.com.george.commerce.mapper;

import br.com.george.commerce.dto.expense.CreateExpenseRequest;
import br.com.george.commerce.dto.expense.ExpenseResponse;
import br.com.george.commerce.entity.Expense;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    Expense toEntity(CreateExpenseRequest request);

    ExpenseResponse toResponse(Expense expense);

}
