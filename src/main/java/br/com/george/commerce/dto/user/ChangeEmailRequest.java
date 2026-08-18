package br.com.george.commerce.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ChangeEmailRequest(

        @NotBlank(message = "Current email is required")
        @Email(message = "Invalid email")
        String currentEmail,

        @NotBlank(message = "Password is required")
        String password,

        @NotBlank(message = "New email is required")
        @Email(message = "Invalid email")
        String newEmail

) {
}
