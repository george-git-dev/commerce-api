package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

public class CpfInUseException extends BusinessException {

    public CpfInUseException() {
        super(HttpStatus.CONFLICT, "CPF_IN_USE",
                "Este CPF já está em outra conta. Fale com o nosso atendimento.");
    }
}
