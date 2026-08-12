package br.com.george.commerce.dto.address;

public record AddressResponse(

        Long id,

        String street,

        String number,

        String complement,

        String neighborhood,

        String city,

        String state,

        String zipCode,

        Boolean primaryAddress,

        Long userId,

        String userName

) {
}
