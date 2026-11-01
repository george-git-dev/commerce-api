package br.com.george.commerce.security;

import static org.assertj.core.api.Assertions.assertThat;
import static br.com.george.commerce.security.TestTokens.customer;
import static br.com.george.commerce.security.TestTokens.token;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.george.commerce.TestcontainersConfiguration;
import br.com.george.commerce.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * A API com o token do Keycloak, sem Keycloak rodando: o MockMvc entrega o token já decodificado
 * e a validação (emissor, aud, validade) é testada direto no validador.
 */
@SpringBootTest
@Import(TestcontainersConfiguration.class)
class KeycloakSecurityIntegrationTest {

    private static final String ISSUER = "http://localhost:8180/auth/realms/nani";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private OAuth2TokenValidator<Jwt> keycloakTokenValidator;

    @Autowired
    private UserRepository users;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void withoutTokenShouldAnswer401() throws Exception {
        mvc.perform(get("/me")).andExpect(status().isUnauthorized());
    }

    @Test
    void publicCatalogShouldOpenWithoutToken() throws Exception {
        mvc.perform(get("/products")).andExpect(status().isOk());
    }

    @Test
    void customerShouldNotReachTheBackoffice() throws Exception {
        mvc.perform(get("/users").with(customer(UUID.randomUUID().toString(), "cliente1@email.com")))
                .andExpect(status().isForbidden());
    }

    @Test
    void staffRoleFromTheRealmShouldReachTheBackoffice() throws Exception {
        mvc.perform(get("/users").with(token(UUID.randomUUID().toString(), "viewer@email.com", true,
                        List.of("ROLE_VIEWER"), List.of("admin:access"))))
                .andExpect(status().isOk());
    }

    @Test
    void firstAccessShouldCreateTheProfileAndReuseItAfterwards() throws Exception {
        String sub = UUID.randomUUID().toString();
        mvc.perform(get("/me").with(customer(sub, "Nova.Cliente@Email.com")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("nova.cliente@email.com"))
                .andExpect(jsonPath("$.name").value("Pessoa Teste"))
                .andExpect(jsonPath("$.cpf").value(org.hamcrest.Matchers.nullValue()));
        mvc.perform(get("/me").with(customer(sub, "nova.cliente@email.com"))).andExpect(status().isOk());

        assertThat(users.findByKeycloakId(sub)).isPresent();
        assertThat(users.findAll().stream().filter(u -> "nova.cliente@email.com".equals(u.getEmail()))).hasSize(1);
    }

    @Test
    void firstAccessShouldLinkTheExistingProfileByVerifiedEmail() throws Exception {
        String sub = UUID.randomUUID().toString();
        // Ana existe nos dados de teste (db/dev) (sem keycloak_id), com CPF.
        mvc.perform(get("/me").with(customer(sub, "ana@email.com")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ana Souza"))
                .andExpect(jsonPath("$.cpf").value("44444444444"));
        assertThat(users.findByKeycloakId(sub)).isPresent();

        // Outro login com o mesmo e-mail não assume a conta da Ana.
        mvc.perform(get("/me").with(customer(UUID.randomUUID().toString(), "ana@email.com")))
                .andExpect(status().isForbidden());
    }

    @Test
    void unverifiedEmailShouldBeRefused() throws Exception {
        mvc.perform(get("/me").with(token(UUID.randomUUID().toString(), "sem.confirmar@email.com", false,
                        List.of("ROLE_CUSTOMER"), List.of())))
                .andExpect(status().isForbidden());
    }

    @Test
    void validatorShouldAcceptOnlyTokensFromTheRealmForThisApi() {
        assertThat(keycloakTokenValidator.validate(jwtFrom(ISSUER, List.of("commerce-api"))).hasErrors()).isFalse();
        assertThat(keycloakTokenValidator.validate(jwtFrom(ISSUER, List.of("outro-sistema"))).hasErrors()).isTrue();
        assertThat(keycloakTokenValidator.validate(jwtFrom("http://falso/auth/realms/nani", List.of("commerce-api")))
                .hasErrors()).isTrue();
        assertThat(keycloakTokenValidator.validate(Jwt.withTokenValue("t").header("alg", "RS256").issuer(ISSUER)
                .audience(List.of("commerce-api")).issuedAt(Instant.now().minusSeconds(7200))
                .expiresAt(Instant.now().minusSeconds(3600)).build()).hasErrors()).isTrue();
    }

    private static Jwt jwtFrom(String issuer, List<String> audience) {
        return Jwt.withTokenValue("t").header("alg", "RS256").subject("x").issuer(issuer).audience(audience)
                .issuedAt(Instant.now()).expiresAt(Instant.now().plusSeconds(300)).build();
    }
}
