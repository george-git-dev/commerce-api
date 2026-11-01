package br.com.george.commerce.dto.user;

/**
 * Perfil na loja. Papéis e permissões vêm do token do Keycloak, não daqui.
 *
 * @param cpf   sempre mascarado ({@code ***.982.247-**}); {@code null} se ainda não informado
 * @param phone celular com DDD, só números; {@code null} se não informado
 */
public record UserResponse(

        Long id,

        String name,

        String cpf,

        String email,

        String phone,

        Boolean active

) {
}
