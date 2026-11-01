package br.com.george.commerce.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.List;

public record CreateProductRequest(

        @NotBlank(message = "Informe o nome.")
        String name,

        @NotBlank(message = "Informe a descrição.")
        String description,

        @NotNull(message = "Informe o preço.")
        @Positive(message = "O preço precisa ser maior que zero.")
        BigDecimal price,

        @NotNull(message = "Informe o estoque.")
        @PositiveOrZero(message = "O estoque não pode ser negativo.")
        Integer stock,

        @NotNull(message = "Informe se está ativo.")
        Boolean active,

        @NotNull(message = "Informe a categoria.")
        Long categoryId,

        @NotNull(message = "Informe a marca.")
        Long brandId,

        List<CreateProductAttributeRequest> attributes

) {
}
