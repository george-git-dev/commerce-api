package br.com.george.commerce.service.security;

import br.com.george.commerce.dto.auth.AuthResponse;
import br.com.george.commerce.dto.auth.LoginRequest;
import br.com.george.commerce.dto.user.ForgotPasswordRequest;
import br.com.george.commerce.dto.user.ResetPasswordRequest;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

}
