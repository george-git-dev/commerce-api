package br.com.george.commerce.controller.admin.usuario;

import br.com.george.commerce.dto.user.CreateUserRequest;
import br.com.george.commerce.dto.user.UpdateUserRoleRequest;
import br.com.george.commerce.dto.user.UserResponse;
import br.com.george.commerce.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Administração - Usuários",
        description = "Gestão administrativa de usuários e permissões."
)
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsuarioController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyRole('VIEWER','ADMIN','SUPER_ADMIN')")
    public List<UserResponse> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('VIEWER','ADMIN','SUPER_ADMIN')")
    public UserResponse findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public UserResponse update(@PathVariable Long id, @Valid @RequestBody CreateUserRequest request) {
        return userService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public UserResponse updateRole(@PathVariable Long id, @RequestBody UpdateUserRoleRequest request) {
        return userService.updateRole(id, request);
    }

}


