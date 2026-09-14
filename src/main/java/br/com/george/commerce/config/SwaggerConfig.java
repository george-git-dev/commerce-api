package br.com.george.commerce.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String BEARER_SCHEME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Commerce API")
                        .version("1.0")
                        .description("REST API para gestao de operacoes de comercio: catalogo, carrinho, pedidos, pagamentos e relatorios.")
                        .contact(new Contact()
                                .name("Equipe Commerce API")
                                .email("commerce-api@empresa.com")
                                .url("https://empresa.com/commerce-api"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("Ambiente local"))
                .addServersItem(new Server()
                        .url("https://api-dev.empresa.com")
                        .description("Ambiente de desenvolvimento"))
                .externalDocs(new ExternalDocumentation()
                        .description("Guia funcional e regras de negocio")
                        .url("https://empresa.com/docs/commerce"))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME))
                .components(new Components()
                        .addSecuritySchemes(BEARER_SCHEME,
                                new SecurityScheme()
                                        .name(BEARER_SCHEME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
