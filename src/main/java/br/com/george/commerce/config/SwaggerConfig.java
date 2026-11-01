package br.com.george.commerce.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger da commerce-api (http://localhost:8080/swagger-ui.html e docs/swagger.yaml).
 * Padrão dos controllers (igual ao BFF): {@code @Tag("Área - Assunto")} na classe e
 * {@code @Operation(summary = "...")} de uma linha por rota. Respostas comuns ficam aqui.
 */
@Configuration
public class SwaggerConfig {

    private static final String BEARER_SCHEME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Commerce API")
                        .version("1.0")
                        .description("""
                                Domínio das lojas: catálogo, sacola, pedidos, pagamentos, afiliados e relatórios. \
                                Chamada pelo BFF de cada loja com o token do Keycloak. Erros no formato \
                                ProblemDetail: status, code e detail em português."""))
                .addServersItem(new Server().url("http://localhost:8080").description("Local"))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME))
                .components(new Components().addSecuritySchemes(BEARER_SCHEME, new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Access token do Keycloak (realm da loja, aud = commerce-api)")));
    }

    /** Respostas que valem para todas as rotas, num lugar só (os controllers ficam só com o resumo). */
    @Bean
    public OpenApiCustomizer respostasPadrao() {
        return openApi -> openApi.getPaths().values().forEach(item -> item.readOperations().forEach(operation -> {
            ApiResponses responses = operation.getResponses();
            responses.addApiResponse("401", new ApiResponse().description("Sem token ou token inválido"));
            responses.addApiResponse("403", new ApiResponse().description("Sem permissão (code ACCESS_DENIED)"));
            responses.addApiResponse("500", new ApiResponse().description("Erro inesperado (code INTERNAL_ERROR)"));
            responses.addApiResponse("503", new ApiResponse().description("Banco indisponível (code SERVICE_UNAVAILABLE)"));
        }));
    }
}
