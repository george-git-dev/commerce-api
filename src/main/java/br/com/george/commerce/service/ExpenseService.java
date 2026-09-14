package br.com.george.commerce.service;

import br.com.george.commerce.dto.expense.CreateExpenseRequest;
import br.com.george.commerce.dto.expense.ExpenseResponse;

import java.util.List;

public interface ExpenseService {

    List<ExpenseResponse> findAll();

    ExpenseResponse findById(Long id);

    ExpenseResponse save(CreateExpenseRequest request);

    void delete(Long id);

    ExpenseResponse update(Long id, CreateExpenseRequest request);

}
