package br.com.george.commerce.controller.me;

import br.com.george.commerce.dto.user.UserResponse;
import br.com.george.commerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Senha e e-mail são trocados no Keycloak; aqui ficam os dados da loja. */
@Tag(
        name = "Área do Cliente - Minha Conta",
        description = "Dados da conta na loja."
)
@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class MinhaContaController {

    private final UserService userService;

    @Operation(summary = "Meus dados na loja")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public UserResponse user() {
        return userService.me();
    }

}
