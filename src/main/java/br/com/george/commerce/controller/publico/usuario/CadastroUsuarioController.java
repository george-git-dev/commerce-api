package br.com.george.commerce.controller.publico.usuario;

import br.com.george.commerce.dto.user.CreateUserRequest;
import br.com.george.commerce.dto.user.UserResponse;
import br.com.george.commerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Público - Cadastro",
        description = "Cadastro de novos usuários na plataforma."
)
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class CadastroUsuarioController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Cadastrar usuário")
    public UserResponse save(@Valid @RequestBody CreateUserRequest request) {
        return userService.save(request);
    }
}
