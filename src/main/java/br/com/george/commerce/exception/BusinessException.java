package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

/**
 * Erro de regra de negócio, já com tudo que o front precisa (B3.2):
 * <ul>
 *   <li>{@code status}: o tipo do erro (404 não encontrado, 409 conflito, 422 regra recusada…);</li>
 *   <li>{@code code}: código estável para a tela decidir o que fazer (ex.: {@code OUT_OF_STOCK}) —
 *       nunca para exibir;</li>
 *   <li>mensagem: frase pronta para o cliente, em português (vira o {@code detail}).</li>
 * </ul>
 * Toda exceção de negócio nova estende esta classe; o {@link GlobalExceptionHandler} cuida do resto.
 */
public abstract class BusinessException extends RuntimeException {

    private final HttpStatus status;
    private final String code;

    protected BusinessException(HttpStatus status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
