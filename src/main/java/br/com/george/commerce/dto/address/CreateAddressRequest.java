package br.com.george.commerce.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
        String state,

        @NotBlank(message = "Informe o CEP.")
        String zipCode,

        @NotNull(message = "Informe se é o endereço principal.")
        Boolean primaryAddress

) {
}
