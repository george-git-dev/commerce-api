package br.com.george.commerce.service.impl;

import br.com.george.commerce.dto.user.UpdateProfileRequest;
import br.com.george.commerce.dto.user.UserResponse;
import br.com.george.commerce.entity.User;
import br.com.george.commerce.exception.CpfAlreadyInformedException;
import br.com.george.commerce.exception.CpfInUseException;
import br.com.george.commerce.exception.UserNotFoundException;
import br.com.george.commerce.mapper.UserMapper;
import br.com.george.commerce.repository.UserRepository;
import br.com.george.commerce.service.UserService;
import br.com.george.commerce.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * Celular: troca (ou apaga, se vier vazio). CPF: só se a conta ainda não tem — depois de
     * informado, muda só pelo atendimento (vai na nota fiscal). Mesmo CPF em outra conta: recusa.
     */
    @Override
    @Transactional
    public UserResponse updateMe(UpdateProfileRequest request) {
        User user = currentUserService.get();

        if (request.phone() != null) {
            user.setPhone(request.phone().isBlank() ? null : request.phone());
        }

        String cpf = digits(request.cpf());
        if (cpf != null && !cpf.equals(user.getCpf())) {
            if (user.getCpf() != null) {
                throw new CpfAlreadyInformedException();
            }
            repository.findByCpf(cpf).ifPresent(other -> {
                throw new CpfInUseException();
            });
            user.setCpf(cpf);
        }

        return mapper.toResponse(repository.save(user));
    }

    /** "529.982.247-25" → "52998224725"; vazio ou nulo → {@code null}. */
    private static String digits(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.replaceAll("\\D", "");
    }

}
