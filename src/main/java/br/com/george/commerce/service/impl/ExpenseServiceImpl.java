package br.com.george.commerce.service.impl;

import br.com.george.commerce.dto.expense.CreateExpenseRequest;
import br.com.george.commerce.dto.expense.ExpenseResponse;
import br.com.george.commerce.entity.Expense;
import br.com.george.commerce.exception.ExpenseNotFoundException;
import br.com.george.commerce.mapper.ExpenseMapper;
import br.com.george.commerce.repository.ExpenseRepository;
import br.com.george.commerce.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository repository;
    private final ExpenseMapper mapper;

    @Override
    public List<ExpenseResponse> findAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public ExpenseResponse findById(Long id) {

        Expense expense = repository.findById(id).orElseThrow(() -> new ExpenseNotFoundException(id));

        return mapper.toResponse(expense);
    }

    @Override
    public ExpenseResponse save(CreateExpenseRequest request) {

        Expense expense = mapper.toEntity(request);

        expense = repository.save(expense);

        return mapper.toResponse(expense);
    }

    @Override
    public void delete(Long id) {

        Expense expense = repository.findById(id).orElseThrow(() -> new ExpenseNotFoundException(id));

        repository.delete(expense);
    }

    @Override
    public ExpenseResponse update(Long id, CreateExpenseRequest request) {

        Expense expense = repository.findById(id).orElseThrow(() -> new ExpenseNotFoundException(id));

        expense.setDescription(request.description());
        expense.setAmount(request.amount());
        expense.setCategory(request.category());
        expense.setExpenseDate(request.expenseDate());

        expense = repository.save(expense);

        return mapper.toResponse(expense);
    }




}
