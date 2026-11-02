package br.com.george.commerce.controller.me;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import br.com.george.commerce.dto.address.AddressResponse;
import br.com.george.commerce.dto.address.CreateAddressRequest;
import br.com.george.commerce.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Área do Cliente - Endereços",
        description = "Endereços cadastrados pelo usuário autenticado."
)
@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class MeusEnderecosController {

    private final AddressService addressService;

    @Operation(summary = "Listar meus endereços")
    @GetMapping("/addresses")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AddressResponse>> myAddresses() {
        return ResponseEntity.ok(addressService.myAddresses());
    }

    @Operation(summary = "Cadastrar endereço")
    @PostMapping("/addresses")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AddressResponse> saveAddress(@Valid @RequestBody CreateAddressRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.save(request));
    }

    @Operation(summary = "Atualizar endereço")
    @PutMapping("/addresses/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AddressResponse> updateAddress(@PathVariable Long id, @Valid @RequestBody CreateAddressRequest request) {
        return ResponseEntity.ok(addressService.update(id, request));
    }

    @Operation(summary = "Excluir endereço")
    @DeleteMapping("/addresses/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

