package br.com.george.commerce.service;

import br.com.george.commerce.entity.User;

/** Quem está chamando a API, a partir do token do Keycloak. */
public interface CurrentUserService {

    /**
     * Perfil da loja do usuário do token. No primeiro acesso, liga a conta existente pelo
     * e-mail confirmado ou cria o perfil. Conta bloqueada na loja: {@code UserInactiveException} (403).
     */
    User get();

}
