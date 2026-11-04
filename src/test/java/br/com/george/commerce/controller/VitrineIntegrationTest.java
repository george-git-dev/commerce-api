package br.com.george.commerce.controller;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.george.commerce.TestcontainersConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Vitrine pública (/catalog) sobre os dados de teste (os 33 produtos do mock do front, todos
 * publicados, e a promoção "Seleção da semana" de 14%). Sem token.
 */
@SpringBootTest
@Import(TestcontainersConfiguration.class)
class VitrineIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    private ResultActions search(String query) throws Exception {
        return mvc.perform(get("/catalog/products" + query)).andExpect(status().isOk());
    }

    @Test
    void productPageShouldComeInTheStoreContract() throws Exception {
        mvc.perform(get("/catalog/products/lattafa-asad"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Asad"))
                .andExpect(jsonPath("$.brandName").value("Lattafa"))
                .andExpect(jsonPath("$.category").value("perfume"))
                .andExpect(jsonPath("$.gender").value("Masculino"))
                .andExpect(jsonPath("$.families", contains("Oriental", "Especiado")))
                .andExpect(jsonPath("$.notes.top", contains("Pimenta Preta", "Tabaco")))
                .andExpect(jsonPath("$.notes.base", contains("Baunilha", "Âmbar", "Benjoim")))
                .andExpect(jsonPath("$.images.length()").value(4))
                .andExpect(jsonPath("$.variants[0].id").value("lattafa-asad-100"))
                .andExpect(jsonPath("$.variants[0].kind").value("frasco"))
                .andExpect(jsonPath("$.variants[0].price").value(299.9))
                .andExpect(jsonPath("$.variants[0].promoPrice").value(nullValue()))
                .andExpect(jsonPath("$.variants[1].kind").value("decant"))
                .andExpect(jsonPath("$.bestSeller").value(true));
    }

    @Test
    void promotionPriceShouldBeCalculatedHere() throws Exception {
        mvc.perform(get("/catalog/products/lattafa-yara"))
                .andExpect(jsonPath("$.variants[0].price").value(289.9))
                .andExpect(jsonPath("$.variants[0].promoPrice").value(249.31));
    }

    @Test
    void unknownSlugShouldAnswer404() throws Exception {
        mvc.perform(get("/catalog/products/nao-existe"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("PRODUCT_NOT_FOUND"));
    }

    @Test
    void pageShouldCountEverythingThatMatches() throws Exception {
        search("?size=5&page=1")
                .andExpect(jsonPath("$.items.length()").value(5))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.totalItems").value(33))
                .andExpect(jsonPath("$.totalPages").value(7));
    }

    @Test
    void filtersShouldBeAppliedHere() throws Exception {
        search("?category=kit&size=60")
                .andExpect(jsonPath("$.totalItems").value(3))
                .andExpect(jsonPath("$.items[*].category", everyItem(org.hamcrest.Matchers.is("kit"))));
        // Busca sem acento e sem maiúscula.
        search("?q=CITRICO")
                .andExpect(jsonPath("$.items[*].slug", contains("afnan-turathi-blue")));
        search("?q=oud&size=60")
                .andExpect(jsonPath("$.items[*].slug", containsInAnyOrder("lattafa-bade-e-al-oud-oud-for-glory",
                        "al-haramain-amber-oud-gold-edition", "ard-al-zaafaran-oud-24-hours",
                        "swiss-arabian-shaghaf-oud")));
        search("?inStock=true&size=60")
                .andExpect(jsonPath("$.totalItems").value(31))
                .andExpect(jsonPath("$.items[*].slug", not(hasItem("armaf-club-de-nuit-women"))));
        search("?kind=decant&size=60").andExpect(jsonPath("$.totalItems").value(11));
        // Destaques valem como "ou": ofertas (7) + selo Exclusivo (1, sem oferta) = 8.
        search("?deal=true&badge=Exclusivo&size=60").andExpect(jsonPath("$.totalItems").value(8));
    }

    @Test
    void sortShouldUseThePriceWithPromotion() throws Exception {
        search("?sort=price_asc&size=2")
                .andExpect(jsonPath("$.items[0].slug").value("lattafa-yara"))
                .andExpect(jsonPath("$.items[1].slug").value("lattafa-khamrah"));
        search("?sort=price_desc&size=1")
                .andExpect(jsonPath("$.items[0].slug").value("al-haramain-amber-oud-gold-edition"));
        search("?sort=newest&size=1")
                .andExpect(jsonPath("$.items[0].slug").value("al-wataniah-sabah-al-ward-creme-hidratante"));
        search("?sort=best_sellers&size=1").andExpect(jsonPath("$.items[0].slug").value("lattafa-asad"));
        // Relevância (padrão): disponíveis primeiro, depois os mais novos — diferente de "mais vendidos".
        search("?size=1")
                .andExpect(jsonPath("$.items[0].slug").value("al-wataniah-sabah-al-ward-creme-hidratante"));
        search("?size=60").andExpect(jsonPath("$.items[31].slug").value("lattafa-yara-locao-hidratante"));
    }

    @Test
    void filterOptionsShouldComeFromThePublishedProducts() throws Exception {
        mvc.perform(get("/catalog/filters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories", containsInAnyOrder("perfume", "kit", "hidratante", "body-splash")))
                .andExpect(jsonPath("$.brands.length()").value(9))
                .andExpect(jsonPath("$.occasions", contains("dia", "noite")))
                .andExpect(jsonPath("$.badges", contains("Edição limitada", "Exclusivo")))
                .andExpect(jsonPath("$.minPrice").value(15.39));
    }

    @Test
    void invalidPageSizeShouldComeBackPerField() throws Exception {
        mvc.perform(get("/catalog/products?size=500"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.size").value("Informe de 1 a 60 itens por página."));
    }
}
