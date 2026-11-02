package br.com.george.commerce.service;

import br.com.george.commerce.dto.user.UpdateProfileRequest;
import br.com.george.commerce.dto.user.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> findAll();

    UserResponse findById(Long id);

    UserResponse me();

    /** Celular e CPF do usuário do token (o CPF só uma vez). */
    UserResponse updateMe(UpdateProfileRequest request);

}
