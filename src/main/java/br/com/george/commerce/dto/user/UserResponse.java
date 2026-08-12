package br.com.george.commerce.dto.user;

import br.com.george.commerce.enums.Role;

public record UserResponse(

        Long id,

        String name,

        String email,

        Boolean active,

        Role role

) {
}
