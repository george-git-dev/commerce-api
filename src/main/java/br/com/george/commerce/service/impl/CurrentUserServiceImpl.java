package br.com.george.commerce.service.impl;

import br.com.george.commerce.entity.User;
import br.com.george.commerce.exception.UserInactiveException;
import br.com.george.commerce.repository.UserRepository;
import br.com.george.commerce.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CurrentUserServiceImpl implements CurrentUserService {

    private final UserRepository repository;

    @Override
    @Transactional
    public User get() {
        Jwt token = currentToken();
        User user = repository.findByKeycloakId(token.getSubject())
                .orElseGet(() -> linkOrCreate(token));

        if (Boolean.FALSE.equals(user.getActive())) {
            throw new UserInactiveException();
        }

        // E-mail trocado no Keycloak (já confirmado lá): acompanha aqui.
        String email = token.getClaimAsString("email");
        if (email != null && !email.equalsIgnoreCase(user.getEmail())) {
            user.setEmail(email.toLowerCase());
        }
        return user;
    }

    /**
     * Primeiro acesso deste login. Só com e-mail confirmado (o Keycloak já exige; aqui é a
     * segunda barreira): liga o perfil que já existe com esse e-mail ou cria um novo, sem CPF.
     */
    private User linkOrCreate(Jwt token) {
        String email = token.getClaimAsString("email");
        if (email == null || !Boolean.TRUE.equals(token.getClaimAsBoolean("email_verified"))) {
            throw new AccessDeniedException("E-mail não confirmado.");
        }
        String normalized = email.toLowerCase();

        User user = repository.findByEmail(normalized)
                .orElseGet(() -> User.builder().email(normalized).active(true).build());

        // E-mail já ligado a OUTRO login (ex.: conta apagada e recriada no Keycloak): não assume.
        if (user.getKeycloakId() != null) {
            throw new AccessDeniedException("Conta ligada a outro login.");
        }

        user.setKeycloakId(token.getSubject());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(name(token, normalized));
        }
        return repository.save(user);
    }

    private static String name(Jwt token, String email) {
        String name = token.getClaimAsString("name");
        return name == null || name.isBlank() ? email : name;
    }

    private static Jwt currentToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuthentication) {
            return jwtAuthentication.getToken();
        }
        throw new AccessDeniedException("Sem token do Keycloak.");
    }

}
