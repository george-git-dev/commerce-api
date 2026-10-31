package br.com.george.commerce.config;

import br.com.george.commerce.service.security.KeycloakJwtAuthenticationConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

/**
 * A API só aceita token do Keycloak (resource server). Quem chama é o BFF, de servidor para
 * servidor, repassando o token do cliente — o navegador nunca fala direto com a API (por isso
 * não há CORS). Sem token: 401; token sem o papel/permissão: 403.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /** O token tem que ter sido emitido PARA esta API ({@code aud}); token de outro sistema é recusado. */
    public static final String AUDIENCE = "commerce-api";

    /**
     * Validação do token (A10): assinatura (no decoder), validade, emissor = realm da loja
     * e {@code aud} = commerce-api.
     */
    @Bean
    public OAuth2TokenValidator<Jwt> keycloakTokenValidator(@Value("${commerce.auth.issuer}") String issuer) {
        return new DelegatingOAuth2TokenValidator<>(
                JwtValidators.createDefaultWithIssuer(issuer),
                new JwtClaimValidator<List<String>>(JwtClaimNames.AUD,
                        audience -> audience != null && audience.contains(AUDIENCE)));
    }

    /**
     * Chaves públicas pelo endereço interno do Keycloak (em produção, a rede do Docker); o emissor
     * conferido continua sendo o endereço público. Nada é chamado na inicialização.
     */
    @Bean
    public JwtDecoder jwtDecoder(@Value("${commerce.auth.jwk-set-uri}") String jwkSetUri,
                                 OAuth2TokenValidator<Jwt> keycloakTokenValidator) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
        decoder.setJwtValidator(keycloakTokenValidator);
        return decoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                // Só token no cabeçalho, sem cookie nem sessão: CSRF não se aplica.
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        )
                        .permitAll()

                        // Catálogo público (somente leitura):
                        // visitante navega sem autenticar.
                        .requestMatchers(
                                HttpMethod.GET,
                                "/products",
                                "/products/**",
                                "/categories",
                                "/categories/**",
                                "/brands",
                                "/brands/**",
                                "/promotions",
                                "/promotions/**"
                        )
                        .permitAll()

                        // As demais autorizações por endpoint vivem nos
                        // controllers, via @PreAuthorize (@EnableMethodSecurity).

                        .anyRequest()
                        .authenticated()
                )

                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(new KeycloakJwtAuthenticationConverter())))

                .build();
    }
}
