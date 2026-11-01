package br.com.george.commerce.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCategoryRequest(

        @NotBlank(message = "Informe o nome.")
        String name,

        @NotNull(message = "Informe se está ativo.")
        Boolean active

) {
}
