package br.com.george.commerce.dto.user;

/** Perfil na loja. Papéis e permissões vêm do token do Keycloak, não daqui. */
public record UserResponse(

        Long id,

        String name,

        String cpf,

        String email,

        Boolean active

) {
}
