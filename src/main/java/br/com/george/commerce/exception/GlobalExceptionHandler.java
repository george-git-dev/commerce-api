package br.com.george.commerce.exception;

import lombok.extern.slf4j.Slf4j;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Todo erro sai no mesmo formato (RFC 9457 {@code ProblemDetail}), que é o que o front espera:
 * <pre>
 * { "status": 422, "code": "OUT_OF_STOCK", "detail": "Não temos mais unidades suficientes de Asad." }
 * </pre>
 * {@code detail} é a frase que a tela mostra (em português); {@code code} é para a tela decidir;
 * {@code errors} (só em validação) traz campo → mensagem. Nada de stack trace, SQL ou nome de classe.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    static final String CODE = "code";

    /** Frase única para qualquer falha do servidor (5xx): nada de detalhe técnico para o cliente. */
    static final String TRY_AGAIN = "Não conseguimos concluir agora. Tente de novo em alguns instantes.";

    /** Regras de negócio: status, código e mensagem vêm da própria exceção. */
    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusiness(BusinessException ex) {
        return problem(ex.getStatus(), ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException ex) {
        return problem(HttpStatus.FORBIDDEN, "ACCESS_DENIED", "Você não tem permissão para fazer isso.");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthentication(AuthenticationException ex) {
        return problem(HttpStatus.UNAUTHORIZED, "SESSION_EXPIRED", "Sua sessão expirou. Entre de novo para continuar.");
    }

    /**
     * Banco fora do ar ou sem conexão: passageiro (503 — o front tenta de novo as leituras sozinho).
     * Mesma frase genérica do 500: o cliente nunca fica sabendo que foi o banco.
     */
    @ExceptionHandler({DataAccessResourceFailureException.class, CannotCreateTransactionException.class})
    public ProblemDetail handleDatabaseUnavailable(Exception ex) {
        log.error("Banco de dados indisponível", ex);
        return problem(HttpStatus.SERVICE_UNAVAILABLE, "SERVICE_UNAVAILABLE", TRY_AGAIN);
    }

    /** Erro inesperado: registra no log (com o detalhe) e devolve só uma frase genérica. */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception ex) {
        log.error("Erro inesperado", ex);
        return problem(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", TRY_AGAIN);
    }

    /** Campos inválidos ({@code @Valid}): uma mensagem geral + campo → mensagem para o formulário. */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.putIfAbsent(error.getField(), error.getDefaultMessage());
        }
        ProblemDetail problem = problem(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Confira os dados e tente de novo.");
        problem.setProperty("errors", errors);
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    /**
     * Erros do próprio Spring (rota inexistente, JSON malformado, método errado…): mesmo formato,
     * com a frase em português no lugar da mensagem técnica em inglês.
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        ProblemDetail problem = body instanceof ProblemDetail detail ? detail : ProblemDetail.forStatus(status);
        // Sem propriedades extras o Spring devolve null (não um mapa vazio).
        if (problem.getProperties() == null || !problem.getProperties().containsKey(CODE)) {
            Framework mapped = Framework.of(status);
            problem.setDetail(mapped.detail);
            problem.setProperty(CODE, mapped.code);
        }
        return super.handleExceptionInternal(ex, problem, headers, status, request);
    }

    private static ProblemDetail problem(HttpStatusCode status, String code, String detail) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setProperty(CODE, code);
        return problem;
    }

    /** Frase e código para os erros do framework, por status. */
    private enum Framework {
        NOT_FOUND("NOT_FOUND", "Não encontramos o que você procurou."),
        METHOD_NOT_ALLOWED("METHOD_NOT_ALLOWED", "Esta operação não é permitida aqui."),
        UNSUPPORTED_MEDIA_TYPE("UNSUPPORTED_MEDIA_TYPE", "Formato de dados não aceito."),
        BAD_REQUEST("BAD_REQUEST", "Confira os dados e tente de novo."),
        INTERNAL_ERROR("INTERNAL_ERROR", TRY_AGAIN),
        OTHER("REQUEST_ERROR", "Algo deu errado. Tente de novo.");

        private final String code;
        private final String detail;

        Framework(String code, String detail) {
            this.code = code;
            this.detail = detail;
        }

        static Framework of(HttpStatusCode status) {
            return switch (status.value()) {
                case 404 -> NOT_FOUND;
                case 405 -> METHOD_NOT_ALLOWED;
                case 406, 415 -> UNSUPPORTED_MEDIA_TYPE;
                case 400 -> BAD_REQUEST;
                default -> status.is5xxServerError() ? INTERNAL_ERROR : OTHER;
            };
        }
    }
}
