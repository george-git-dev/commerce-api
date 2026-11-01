package br.com.george.commerce.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import br.com.george.commerce.service.security.KeycloakJwtAuthenticationConverter;
import java.util.List;
import java.util.Map;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;

/** Tokens como o Keycloak emite para a commerce-api, para os testes com MockMvc. */
public final class TestTokens {

    private TestTokens() {
    }

    /** Papéis do realm + permissões do cliente commerce-api; authorities iguais às da produção. */
    public static JwtRequestPostProcessor token(String sub, String email, boolean verified, List<String> realmRoles,
                                                List<String> permissions) {
        Jwt token = Jwt.withTokenValue("t").header("alg", "RS256")
                .subject(sub)
                .claim("email", email)
                .claim("email_verified", verified)
                .claim("name", "Pessoa Teste")
                .claim("realm_access", Map.of("roles", realmRoles))
                .claim("resource_access", Map.of("commerce-api", Map.of("roles", permissions)))
                .build();
        return jwt().jwt(token).authorities(new KeycloakJwtAuthenticationConverter().convert(token).getAuthorities());
    }

    public static JwtRequestPostProcessor customer(String sub, String email) {
        return token(sub, email, true, List.of("ROLE_CUSTOMER", "default-roles-nani"), List.of("shop:checkout"));
    }
}
