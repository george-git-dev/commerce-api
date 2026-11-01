package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

public class ExpenseNotFoundException extends BusinessException {

    public ExpenseNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "EXPENSE_NOT_FOUND", "Despesa não encontrada.");
    }
}
