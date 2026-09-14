package br.com.george.commerce.controller.me;

import br.com.george.commerce.dto.user.ChangeEmailRequest;
import br.com.george.commerce.dto.user.ChangePasswordRequest;
import br.com.george.commerce.dto.user.UserResponse;
import br.com.george.commerce.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Área do Cliente - Minha Conta",
        description = "Dados da conta, alteração de senha e e-mail."
)
@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class MinhaContaController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public UserResponse user() {
        return userService.me();
    }

    @PatchMapping("/password")
    @PreAuthorize("isAuthenticated()")
    public void changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
    }

    @PatchMapping("/email")
    @PreAuthorize("isAuthenticated()")
    public void changeEmail(@Valid @RequestBody ChangeEmailRequest request) {
        userService.changeEmail(request);
    }

}

