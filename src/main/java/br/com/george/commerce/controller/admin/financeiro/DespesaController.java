package br.com.george.commerce.controller.admin.financeiro;

import br.com.george.commerce.util.Locations;
import org.springframework.http.ResponseEntity;
import br.com.george.commerce.dto.expense.CreateExpenseRequest;
import br.com.george.commerce.dto.expense.ExpenseResponse;
import br.com.george.commerce.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Listar despesas")
    @GetMapping
    @PreAuthorize("hasAnyRole('VIEWER','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<List<ExpenseResponse>> findAll() {
        return ResponseEntity.ok(expenseService.findAll());
    }

    @Operation(summary = "Consultar despesa")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('VIEWER','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ExpenseResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(expenseService.findById(id));
    }

    @Operation(summary = "Cadastrar despesa")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ExpenseResponse> save(@Valid @RequestBody CreateExpenseRequest request) {
        ExpenseResponse created = expenseService.save(request);
        return ResponseEntity.created(Locations.of("/expenses/{id}", created.id())).body(created);
    }

    @Operation(summary = "Excluir despesa")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        expenseService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualizar despesa")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ExpenseResponse> update(@PathVariable Long id, @Valid @RequestBody CreateExpenseRequest request) {
        return ResponseEntity.ok(expenseService.update(id, request));
    }
}


