package br.com.george.commerce.service.impl;

import br.com.george.commerce.dto.user.UserResponse;
import br.com.george.commerce.entity.User;
import br.com.george.commerce.exception.UserNotFoundException;
import br.com.george.commerce.mapper.UserMapper;
import br.com.george.commerce.repository.UserRepository;
import br.com.george.commerce.service.UserService;
import br.com.george.commerce.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Perfis da loja. Cadastro, senha, e-mail e papéis são do Keycloak; editar perfil,
 * bloquear e gerenciar a equipe (via API de administração do Keycloak) entram na B7.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final CurrentUserService currentUserService;

    @Override
    public List<UserResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse findById(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return mapper.toResponse(user);
    }

    @Override
    public UserResponse me() {
        return mapper.toResponse(currentUserService.get());
    }

}
