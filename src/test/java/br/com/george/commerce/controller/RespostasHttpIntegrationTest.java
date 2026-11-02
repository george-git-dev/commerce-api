package br.com.george.commerce.controller;

import static br.com.george.commerce.security.TestTokens.customer;
import static br.com.george.commerce.security.TestTokens.token;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.george.commerce.TestcontainersConfiguration;
import com.jayway.jsonpath.JsonPath;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/** Padrão de status: criar = 201 (+ Location quando há GET do recurso), excluir = 204 sem corpo. */
@SpringBootTest
@Import(TestcontainersConfiguration.class)
class RespostasHttpIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void creatingShouldAnswer201WithTheLocationOfTheNewResource() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/categories")
                        .with(token(UUID.randomUUID().toString(), "admin.teste@email.com", true,
                                List.of("ROLE_ADMIN"), List.of("admin:access")))
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Kits\",\"active\":true}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse();
        Integer id = JsonPath.read(response.getContentAsString(), "$.id");
        assertThat(response.getHeader("Location")).isEqualTo("/categories/" + id);
    }

    @Test
    void creatingWithoutAReadRouteShouldAnswer201AndDeletingShouldAnswer204() throws Exception {
        String email = UUID.randomUUID() + "@email.com";
        String body = mvc.perform(post("/me/addresses").with(customer("sub-" + email, email))
                        .contentType(MediaType.APPLICATION_JSON).content("""
                                {"street":"Rua das Flores","number":"10","neighborhood":"Centro","city":"São Paulo",
                                 "state":"SP","zipCode":"01000-000","primaryAddress":true}"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.street").value("Rua das Flores"))
                .andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.read(body, "$.id");

        mvc.perform(delete("/me/addresses/" + id).with(customer("sub-" + email, email)))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }
}
