package br.com.george.commerce.exception;

import static br.com.george.commerce.security.TestTokens.customer;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.george.commerce.TestcontainersConfiguration;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/** Todo erro no formato que o front espera: ProblemDetail com code + detail em português. */
@SpringBootTest
@Import(TestcontainersConfiguration.class)
class ProblemDetailIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void businessErrorShouldComeWithCodeAndPortugueseMessage() throws Exception {
        mvc.perform(get("/products/999999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("PRODUCT_NOT_FOUND"))
                .andExpect(jsonPath("$.detail").value("Produto não encontrado."));
    }

    @Test
    void invalidFieldsShouldListEachFieldWithItsMessage() throws Exception {
        mvc.perform(post("/me/addresses").with(customer(UUID.randomUUID().toString(), "form@email.com"))
                        .contentType(MediaType.APPLICATION_JSON).content("{\"number\":\"10\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.detail").value("Confira os dados e tente de novo."))
                .andExpect(jsonPath("$.errors.street").value("Informe a rua."))
                .andExpect(jsonPath("$.errors.zipCode").value("Informe o CEP."))
                .andExpect(jsonPath("$.errors.number").doesNotExist());
    }

    @Test
    void malformedJsonShouldGetAPortugueseMessageInsteadOfTheTechnicalOne() throws Exception {
        mvc.perform(post("/me/addresses").with(customer(UUID.randomUUID().toString(), "json@email.com"))
                        .contentType(MediaType.APPLICATION_JSON).content("{"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.detail").value("Confira os dados e tente de novo."));
    }

    @Test
    void missingPermissionShouldAnswer403WithCode() throws Exception {
        mvc.perform(get("/users").with(customer(UUID.randomUUID().toString(), "sem.permissao@email.com")))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("ACCESS_DENIED"))
                .andExpect(jsonPath("$.detail").value("Você não tem permissão para fazer isso."));
    }

    @Test
    void unknownRouteShouldAnswer404InPortuguese() throws Exception {
        mvc.perform(get("/rota-que-nao-existe").with(customer(UUID.randomUUID().toString(), "rota@email.com")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.detail").value("Não encontramos o que você procurou."));
    }
}
