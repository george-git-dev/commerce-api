package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "Usuário não encontrado.");
    }

    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "Usuário não encontrado.");
    }

    public UserNotFoundException(String email) {
        super(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "Usuário não encontrado.");
    }
}
