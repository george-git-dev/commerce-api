package br.com.george.commerce.controller.admin.usuario;

import br.com.george.commerce.dto.user.UserResponse;
import br.com.george.commerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Consulta de clientes e equipe. Editar, bloquear, excluir e trocar papel voltam na B7,
 * passando pela API de administração do Keycloak (onde vivem login e papéis).
 */
@Tag(
        name = "Administração - Usuários",
        description = "Consulta de usuários da loja."
)
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsuarioController {

    private final UserService userService;

    @Operation(summary = "Listar usuários")
    @GetMapping
    @PreAuthorize("hasAnyRole('VIEWER','ADMIN','SUPER_ADMIN')")
    public List<UserResponse> findAll() {
        return userService.findAll();
    }

    @Operation(summary = "Consultar usuário")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('VIEWER','ADMIN','SUPER_ADMIN')")
    public UserResponse findById(@PathVariable Long id) {
        return userService.findById(id);
    }

}
