package br.com.george.commerce.controller;

import br.com.george.commerce.dto.user.CreateUserRequest;
import br.com.george.commerce.dto.user.UpdateUserRoleRequest;
import br.com.george.commerce.dto.user.UserResponse;
import br.com.george.commerce.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponse> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping
    public UserResponse save(@Valid @RequestBody CreateUserRequest request) {
        return userService.save(request);
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @Valid @RequestBody CreateUserRequest request) {
        return userService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @PatchMapping("/{id}/role")
    public UserResponse updateRole(@PathVariable Long id, @RequestBody UpdateUserRoleRequest request) {
        return userService.updateRole(id, request);
    }

}
