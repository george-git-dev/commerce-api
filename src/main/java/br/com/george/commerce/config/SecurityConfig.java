package br.com.george.commerce.config;

import br.com.george.commerce.service.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();
        // TODO: quando existir front em produção, adicionar a URL real aqui também.
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)

                // H2 console usa <frame>; sem isso o navegador bloqueia
                // o carregamento (X-Frame-Options: DENY é o padrão do Spring Security).
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // Públicos
                        .requestMatchers("/auth/**")
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/users"
                        )
                        .permitAll()

                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        )
                        .permitAll()

                        // Console do H2 (só existe quando o profile "h2" está
                        // ativo — spring.h2.console.enabled). Sem risco em
                        // produção: no profile postgres esse endpoint nem existe.
                        //
                        // Precisa do matcher dedicado do Spring Boot: o H2
                        // console roda num servlet separado (não passa pelo
                        // DispatcherServlet), e o PathPatternRequestMatcher
                        // padrão do Spring Security 6.4+ não casa nesse caso
                        // (retornava 403 mesmo com "/h2-console/**" na regra).
                        .requestMatchers(PathRequest.toH2Console())
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

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }
}