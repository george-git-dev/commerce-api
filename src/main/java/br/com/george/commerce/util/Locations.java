package br.com.george.commerce.util;

import java.net.URI;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Endereço do recurso recém-criado, para o cabeçalho {@code Location} do 201 Created
 * (ex.: {@code Locations.of("/products/{id}", 42) → /products/42}). Relativo: quem chama (o BFF)
 * resolve contra o próprio endereço da API.
 */
public final class Locations {

    private Locations() {
    }

    public static URI of(String path, Object... variables) {
        return UriComponentsBuilder.fromPath(path).buildAndExpand(variables).toUri();
    }
}
