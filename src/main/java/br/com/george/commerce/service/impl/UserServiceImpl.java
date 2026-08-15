package br.com.george.commerce.service.impl;

import br.com.george.commerce.dto.user.*;
import br.com.george.commerce.entity.User;
import br.com.george.commerce.enums.Role;
import br.com.george.commerce.exception.*;
import br.com.george.commerce.mapper.UserMapper;
import br.com.george.commerce.repository.UserRepository;
import br.com.george.commerce.service.UserService;
import br.com.george.commerce.service.security.JwtService;
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
    private final JwtService jwtService;

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

        if (repository.findByCpf(request.cpf()).isPresent()) {
            throw new CpfAlreadyExistsException(request.cpf());
        }

        User user = User.builder()
                .name(request.name())
                .cpf(request.cpf())
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

        if (user.getRole() == Role.ROLE_SUPER_ADMIN) {

            long totalSuperAdmins = repository.countByRole(Role.ROLE_SUPER_ADMIN);

            if (totalSuperAdmins == 1) {
                throw new LastSuperAdminDeletionException();
            }
        }

        repository.delete(user);
    }

    @Override
    public UserResponse updateRole(Long id, UpdateUserRoleRequest request) {

        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        if (user.getRole() == Role.ROLE_SUPER_ADMIN && request.role() != Role.ROLE_SUPER_ADMIN) {

            long totalSuperAdmins = repository.countByRole(Role.ROLE_SUPER_ADMIN);

            if (totalSuperAdmins == 1) {
                throw new LastSuperAdminException();
            }
        }

        user.setRole(request.role());

        user = repository.save(user);

        return mapper.toResponse(user);
    }

    @Override
    public UserResponse me() {

        String email = jwtService.getCurrentUserEmail();

        User user = repository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        return mapper.toResponse(user);
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {

        String email = jwtService.getCurrentUserEmail();

        User user = repository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));

        repository.save(user);
    }


    @Override
    public void changeEmail(ChangeEmailRequest request) {

        String email = jwtService.getCurrentUserEmail();

        User user = repository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getEmail().equals(request.currentEmail())) {
            throw new InvalidEmailChangeException("Current email does not match");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidEmailChangeException("Invalid password");
        }

        if (repository.findByEmail(request.newEmail()).isPresent()) {
            throw new EmailAlreadyExistsException(request.newEmail());
        }

        user.setEmail(request.newEmail());

        repository.save(user);
    }

}
