package br.com.george.commerce.service.impl;

import br.com.george.commerce.dto.user.CreateUserRequest;
import br.com.george.commerce.dto.user.UpdateUserRoleRequest;
import br.com.george.commerce.dto.user.UserResponse;
import br.com.george.commerce.entity.User;
import br.com.george.commerce.enums.Role;
import br.com.george.commerce.exception.EmailAlreadyExistsException;
import br.com.george.commerce.exception.UserNotFoundException;
import br.com.george.commerce.mapper.UserMapper;
import br.com.george.commerce.repository.UserRepository;
import br.com.george.commerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;

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
    public UserResponse save(CreateUserRequest request) {

        if (repository.findByEmail(request.email()).isPresent()) {
            throw new EmailAlreadyExistsException(request.email());
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .active(true)
                .role(Role.ROLE_CUSTOMER)
                .build();

        user = repository.save(user);

        return mapper.toResponse(user);
    }

    @Override
    public UserResponse update(Long id, CreateUserRequest request) {

        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        user = repository.save(user);

        return mapper.toResponse(user);
    }

    @Override
    public void delete(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        repository.delete(user);
    }

    @Override
    public UserResponse updateRole(Long id, UpdateUserRoleRequest request) {

        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        user.setRole(request.role());

        user = repository.save(user);

        return mapper.toResponse(user);
    }
}
