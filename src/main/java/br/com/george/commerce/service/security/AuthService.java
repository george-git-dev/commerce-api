package br.com.george.commerce.service.security;

import br.com.george.commerce.dto.auth.AuthResponse;
import br.com.george.commerce.dto.auth.LoginRequest;

public interface AuthService {

    AuthResponse login(LoginRequest request);

}
