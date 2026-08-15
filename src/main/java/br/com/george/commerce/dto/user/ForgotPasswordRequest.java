package br.com.george.commerce.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(

        @Email(message = "Invalid email")
        @NotBlank(message = "Email is required")
        String email

) {
}
