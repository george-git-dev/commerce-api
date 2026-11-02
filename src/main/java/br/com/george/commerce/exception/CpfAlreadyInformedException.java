package br.com.george.commerce.exception;

import org.springframework.http.HttpStatus;

public class CpfAlreadyInformedException extends BusinessException {

    public CpfAlreadyInformedException() {
        super(HttpStatus.CONFLICT, "CPF_ALREADY_INFORMED",
                "O CPF já foi informado e não pode ser alterado. Fale com o nosso atendimento.");
    }
}
