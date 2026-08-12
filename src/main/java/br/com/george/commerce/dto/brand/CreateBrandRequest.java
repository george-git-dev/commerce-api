package br.com.george.commerce.dto.brand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateBrandRequest(

        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "Active is required")
        Boolean active

) {
}
