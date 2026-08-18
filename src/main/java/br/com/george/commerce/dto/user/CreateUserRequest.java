package br.com.george.commerce.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record CreateUserRequest(

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "CPF is required")
        @CPF(message = "CPF inválido")
        String cpf,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email")
        String email,

        @NotBlank(message = "Password is required")
        String password

) {
}
