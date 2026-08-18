package br.com.george.commerce.controller;

import br.com.george.commerce.dto.auth.AuthResponse;
import br.com.george.commerce.dto.auth.LoginRequest;
import br.com.george.commerce.dto.user.ForgotPasswordRequest;
import br.com.george.commerce.dto.user.ResetPasswordRequest;
import br.com.george.commerce.service.security.AuthService;
import br.com.george.commerce.service.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/test")
    public String test(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        return jwtService.extractUsername(token);
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
