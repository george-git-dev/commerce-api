package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

public class UserInactiveException extends BusinessException {

    public UserInactiveException() {
        super(HttpStatus.FORBIDDEN, "ACCOUNT_BLOCKED", "Não foi possível acessar esta conta. Fale com o nosso atendimento.");
    }
}
