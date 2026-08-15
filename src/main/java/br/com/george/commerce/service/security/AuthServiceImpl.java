package br.com.george.commerce.service.security;

import br.com.george.commerce.dto.auth.AuthResponse;
import br.com.george.commerce.dto.auth.LoginRequest;
import br.com.george.commerce.dto.user.ForgotPasswordRequest;
import br.com.george.commerce.dto.user.ResetPasswordRequest;
import br.com.george.commerce.entity.PasswordResetToken;
import br.com.george.commerce.entity.User;
import br.com.george.commerce.exception.UserInactiveException;
import br.com.george.commerce.repository.PasswordResetTokenRepository;
import br.com.george.commerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final PasswordResetTokenRepository tokenRepository;

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new UserInactiveException();
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token);
    }

    @Override
    public void forgotPassword(
            ForgotPasswordRequest request) {

        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new RuntimeException("User not found"));

        PasswordResetToken token =
                PasswordResetToken.builder()
                        .token(UUID.randomUUID().toString())
                        .expiresAt(LocalDateTime.now().plusMinutes(15))
                        .used(false)
                        .user(user)
                        .build();

        tokenRepository.save(token);

        log.info("RESET TOKEN: {}", token.getToken());
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {

        PasswordResetToken token = tokenRepository.findByToken(request.token()).orElseThrow(() -> new RuntimeException("Invalid token"));

        if (Boolean.TRUE.equals(token.getUsed())) {
            throw new RuntimeException("Token already used");
        }

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        User user = token.getUser();

        user.setPassword(passwordEncoder.encode(request.newPassword()));

        userRepository.save(user);

        token.setUsed(true);

        tokenRepository.save(token);
    }
}
