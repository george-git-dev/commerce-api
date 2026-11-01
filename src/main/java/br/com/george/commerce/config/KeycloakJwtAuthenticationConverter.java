package br.com.george.commerce.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Token do Keycloak → authorities do Spring (A10):
 * <ul>
 *   <li>papéis da loja do realm ({@code realm_access.roles} com prefixo {@code ROLE_}), para os
 *       {@code @PreAuthorize("hasRole('ADMIN')")} que já existem;</li>
 *   <li>permissões do cliente {@code commerce-api} ({@code resource_access.commerce-api.roles},
 *       ex.: {@code admin:access}), para {@code hasAuthority(...)}.</li>
 * </ul>
 * Papéis internos do Keycloak (default-roles-nani, offline_access…) ficam de fora.
 * Não é bean de propósito: um {@code Converter} registrado no contexto entraria nas conversões do MVC.
 */
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    static final String API_CLIENT = "commerce-api";

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        realmRoles(jwt).stream()
                .filter(role -> role.startsWith("ROLE_"))
                .map(SimpleGrantedAuthority::new)
                .forEach(authorities::add);
        clientRoles(jwt).stream()
                .map(SimpleGrantedAuthority::new)
                .forEach(authorities::add);
        return new JwtAuthenticationToken(jwt, authorities, jwt.getSubject());
    }

    private static Collection<String> realmRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        return strings(realmAccess == null ? null : realmAccess.get("roles"));
    }

    private static Collection<String> clientRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
        if (resourceAccess == null || !(resourceAccess.get(API_CLIENT) instanceof Map<?, ?> client)) {
            return List.of();
        }
        return strings(client.get("roles"));
    }

    private static Collection<String> strings(Object value) {
        if (!(value instanceof Collection<?> values)) {
            return List.of();
        }
        return values.stream().filter(String.class::isInstance).map(String.class::cast).toList();
    }

}
