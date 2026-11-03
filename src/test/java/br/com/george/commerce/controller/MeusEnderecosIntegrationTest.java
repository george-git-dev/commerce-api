package br.com.george.commerce.controller;

import static br.com.george.commerce.security.TestTokens.customer;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.george.commerce.TestcontainersConfiguration;
import com.jayway.jsonpath.JsonPath;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/** Minha conta → Endereços: sempre um principal, limite de 5, só os da própria conta. */
@SpringBootTest
@Import(TestcontainersConfiguration.class)
class MeusEnderecosIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    /** Conta nova (criada no primeiro acesso), só dela. */
    private static String newAccount() {
        return UUID.randomUUID() + "@email.com";
    }

    private ResultActions as(String email, MockHttpServletRequestBuilder request) throws Exception {
        return mvc.perform(request.with(customer("sub-" + email, email)));
    }

    private static String body(String number, boolean primary) {
        return """
                {"street":"Avenida Paulista","number":"%s","complement":"","neighborhood":"Bela Vista",
                 "city":"São Paulo","state":"SP","zipCode":"01310100","primaryAddress":%s}"""
                .formatted(number, primary);
    }

    private long create(String email, String number, boolean primary) throws Exception {
        String json = as(email, post("/me/addresses").contentType(MediaType.APPLICATION_JSON).content(body(number, primary)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(json, "$.id")).longValue();
    }

    @Test
    void firstAddressShouldBecomePrimaryAndNotExposeTheUser() throws Exception {
        String email = newAccount();
        as(email, post("/me/addresses").contentType(MediaType.APPLICATION_JSON).content(body("1", false)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.primaryAddress").value(true))
                .andExpect(jsonPath("$.userId").doesNotExist())
                .andExpect(jsonPath("$.userName").doesNotExist());
    }

    @Test
    void newPrimaryShouldReplaceTheOldOneAndComeFirstInTheList() throws Exception {
        String email = newAccount();
        create(email, "1", false);
        long second = create(email, "2", true);

        as(email, get("/me/addresses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(second))
                .andExpect(jsonPath("$[0].primaryAddress").value(true))
                .andExpect(jsonPath("$[1].primaryAddress").value(false));
    }

    @Test
    void choosingAnotherAsPrimaryByPutShouldKeepOnlyOnePrimary() throws Exception {
        String email = newAccount();
        long first = create(email, "1", false);
        long second = create(email, "2", false);

        as(email, put("/me/addresses/" + second).contentType(MediaType.APPLICATION_JSON).content(body("2", true)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.primaryAddress").value(true));
        // Desmarcar o principal atual não deixa a conta sem nenhum.
        as(email, put("/me/addresses/" + second).contentType(MediaType.APPLICATION_JSON).content(body("2A", false)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value("2A"))
                .andExpect(jsonPath("$.primaryAddress").value(true));

        as(email, get("/me/addresses"))
                .andExpect(jsonPath("$[0].id").value(second))
                .andExpect(jsonPath("$[1].id").value(first))
                .andExpect(jsonPath("$[1].primaryAddress").value(false));
    }

    @Test
    void removingThePrimaryShouldPromoteTheOldestOne() throws Exception {
        String email = newAccount();
        long first = create(email, "1", false);
        create(email, "2", false);
        long third = create(email, "3", true);

        as(email, delete("/me/addresses/" + third)).andExpect(status().isNoContent());

        as(email, get("/me/addresses"))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(first))
                .andExpect(jsonPath("$[0].primaryAddress").value(true));
        // Removido não volta nem pode ser editado.
        as(email, delete("/me/addresses/" + third)).andExpect(status().isNotFound());
    }

    @Test
    void sixthAddressShouldBeRefused() throws Exception {
        String email = newAccount();
        for (int n = 1; n <= 5; n++) {
            create(email, String.valueOf(n), false);
        }
        as(email, post("/me/addresses").contentType(MediaType.APPLICATION_JSON).content(body("6", false)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("ADDRESS_LIMIT"))
                .andExpect(jsonPath("$.detail").value("Você chegou ao limite de 5 endereços. Remova um para cadastrar outro."));
    }

    @Test
    void addressOfAnotherAccountShouldLookLikeItDoesNotExist() throws Exception {
        long ofAna = create(newAccount(), "1", false);
        String other = newAccount();

        as(other, put("/me/addresses/" + ofAna).contentType(MediaType.APPLICATION_JSON).content(body("9", true)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("ADDRESS_NOT_FOUND"));
        as(other, delete("/me/addresses/" + ofAna)).andExpect(status().isNotFound());
        as(other, get("/me/addresses")).andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void missingFieldsShouldComeBackPerField() throws Exception {
        as(newAccount(), post("/me/addresses").contentType(MediaType.APPLICATION_JSON).content("{\"primaryAddress\":false}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.street").value("Informe a rua."));
    }

    @Test
    void zipCodeAndStateShouldHaveTheRightFormat() throws Exception {
        String invalid = body("1", false).replace("\"01310100\"", "\"abc\"").replace("\"SP\"", "\"São Paulo\"");
        as(newAccount(), post("/me/addresses").contentType(MediaType.APPLICATION_JSON).content(invalid))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.zipCode").value("Informe um CEP válido, com 8 números."))
                .andExpect(jsonPath("$.errors.state").value("Informe a UF com 2 letras (ex.: SP)."));
        // Com ou sem traço, os dois formatos valem.
        as(newAccount(), post("/me/addresses").contentType(MediaType.APPLICATION_JSON)
                        .content(body("1", false).replace("\"01310100\"", "\"01310-100\"")))
                .andExpect(status().isCreated());
    }
}
