package br.com.george.commerce.controller.publico.autenticacao;

import br.com.george.commerce.dto.auth.AuthResponse;
import br.com.george.commerce.dto.auth.LoginRequest;
import br.com.george.commerce.dto.user.ForgotPasswordRequest;
import br.com.george.commerce.dto.user.ResetPasswordRequest;
import br.com.george.commerce.service.security.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Público - Autenticação",
        description = "Login, recuperação e redefinição de senha."
)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AutenticacaoController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }


    @PostMapping("/forgot-password")
    public void forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
    }

    @PostMapping("/reset-password")
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
    }
}


