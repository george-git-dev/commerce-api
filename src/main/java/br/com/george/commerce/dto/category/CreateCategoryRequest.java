package br.com.george.commerce.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCategoryRequest(

        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "Active is required")
        Boolean active

) {
}
