package br.com.george.commerce.exception;

public class ExpenseNotFoundException extends RuntimeException {

    public ExpenseNotFoundException(Long id) {
        super("Expense not found with id: " + id);
    }
}
