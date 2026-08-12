package br.com.george.commerce.service;

import br.com.george.commerce.dto.user.CreateUserRequest;
import br.com.george.commerce.dto.user.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> findAll();

    UserResponse findById(Long id);

    UserResponse save(CreateUserRequest request);

    UserResponse update(Long id, CreateUserRequest request);

    void delete(Long id);

}
