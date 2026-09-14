package br.com.george.commerce.controller.admin.financeiro;

import br.com.george.commerce.dto.expense.CreateExpenseRequest;
import br.com.george.commerce.dto.expense.ExpenseResponse;
import br.com.george.commerce.service.ExpenseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Administração - Despesas",
        description = "Cadastro e manutenção de despesas."
)
@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class DespesaController {

    private final ExpenseService expenseService;

    @GetMapping
    @PreAuthorize("hasAnyRole('VIEWER','ADMIN','SUPER_ADMIN')")
    public List<ExpenseResponse> findAll() {
        return expenseService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('VIEWER','ADMIN','SUPER_ADMIN')")
    public ExpenseResponse findById(@PathVariable Long id) {
        return expenseService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ExpenseResponse save(@Valid @RequestBody CreateExpenseRequest request) {
        return expenseService.save(request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public void delete(@PathVariable Long id) {
        expenseService.delete(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ExpenseResponse update(@PathVariable Long id, @Valid @RequestBody CreateExpenseRequest request) {
        return expenseService.update(id, request);
    }
}


