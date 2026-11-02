package br.com.george.commerce.dto.user;

import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

/**
 * Minha conta → Dados pessoais. Campos ausentes ({@code null}) não mudam nada.
 *
 * @param phone celular com DDD, só números; vazio ({@code ""}) apaga
 * @param cpf   só aceito enquanto a conta não tem CPF (com ou sem pontuação)
 */
public record UpdateProfileRequest(

        @Pattern(regexp = "^(\\d{10,11})?$", message = "Informe um celular válido, com DDD.")
        String phone,

        @CPF(message = "Informe um CPF válido.")
        String cpf

) {
}
