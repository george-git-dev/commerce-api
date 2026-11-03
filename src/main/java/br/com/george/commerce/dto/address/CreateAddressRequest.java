package br.com.george.commerce.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateAddressRequest(

        @NotBlank(message = "Informe a rua.")
        String street,

        @NotBlank(message = "Informe o número.")
        String number,

        String complement,

        @NotBlank(message = "Informe o bairro.")
        String neighborhood,

        @NotBlank(message = "Informe a cidade.")
        String city,

        @NotBlank(message = "Informe o estado.")
        @Pattern(regexp = "^[A-Za-z]{2}$", message = "Informe a UF com 2 letras (ex.: SP).")
        String state,

        @NotBlank(message = "Informe o CEP.")
        @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "Informe um CEP válido, com 8 números.")
        String zipCode,

        @NotNull(message = "Informe se é o endereço principal.")
        Boolean primaryAddress

) {
}
