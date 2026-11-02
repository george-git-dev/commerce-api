package br.com.george.commerce.controller;

import static br.com.george.commerce.security.TestTokens.customer;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/** PATCH /me: celular e CPF do cliente logado. */
@SpringBootTest
@Import(TestcontainersConfiguration.class)
class MinhaContaIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    /** Conta nova (criada no primeiro acesso, sem CPF), com um e-mail só dela. */
    private ResultActions update(String email, String json) throws Exception {
        return mvc.perform(patch("/me").with(customer("sub-" + email, email))
                .contentType(MediaType.APPLICATION_JSON).content(json));
    }

    private static String newEmail() {
        return UUID.randomUUID() + "@email.com";
    }

    @Test
    void shouldSaveThePhoneAndTheCpfOnceAndReturnTheCpfMasked() throws Exception {
        String email = newEmail();
        update(email, "{\"phone\":\"11912345678\",\"cpf\":\"529.982.247-25\"}")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("11912345678"))
                .andExpect(jsonPath("$.cpf").value("***.982.247-**"));

        // Mandar o mesmo CPF de novo não é erro; trocar por outro é.
        update(email, "{\"cpf\":\"52998224725\"}").andExpect(status().isOk());
        update(email, "{\"cpf\":\"111.444.777-35\"}")
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("CPF_ALREADY_INFORMED"));
    }

    @Test
    void emptyPhoneShouldRemoveIt() throws Exception {
        String email = newEmail();
        update(email, "{\"phone\":\"11912345678\"}").andExpect(jsonPath("$.phone").value("11912345678"));
        update(email, "{\"phone\":\"\"}").andExpect(jsonPath("$.phone").value(org.hamcrest.Matchers.nullValue()));
    }

    @Test
    void cpfOfAnotherAccountShouldBeRefused() throws Exception {
        update(newEmail(), "{\"cpf\":\"390.533.447-05\"}").andExpect(status().isOk());
        update(newEmail(), "{\"cpf\":\"39053344705\"}")
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("CPF_IN_USE"))
                .andExpect(jsonPath("$.detail").value("Este CPF já está em outra conta. Fale com o nosso atendimento."));
    }

    @Test
    void invalidFieldsShouldComeBackPerField() throws Exception {
        update(newEmail(), "{\"phone\":\"123\",\"cpf\":\"123.456.789-00\"}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.errors.phone").value("Informe um celular válido, com DDD."))
                .andExpect(jsonPath("$.errors.cpf").value("Informe um CPF válido."));
    }
}
