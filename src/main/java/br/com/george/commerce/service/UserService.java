package br.com.george.commerce.service;

import br.com.george.commerce.dto.user.*;

import java.util.List;

public interface UserService {

    List<UserResponse> findAll();

    UserResponse findById(Long id);

    UserResponse save(CreateUserRequest request);

    UserResponse update(Long id, CreateUserRequest request);

    void delete(Long id);

    UserResponse updateRole(Long id, UpdateUserRoleRequest request);

    UserResponse me();

    void changePassword(ChangePasswordRequest request);

    void changeEmail(ChangeEmailRequest request);

}
