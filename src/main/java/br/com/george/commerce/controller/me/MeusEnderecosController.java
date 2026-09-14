package br.com.george.commerce.controller.me;

import br.com.george.commerce.dto.address.AddressResponse;
import br.com.george.commerce.dto.address.CreateAddressRequest;
import br.com.george.commerce.service.AddressService;
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

    @GetMapping("/addresses")
    @PreAuthorize("isAuthenticated()")
    public List<AddressResponse> myAddresses() {
        return addressService.myAddresses();
    }

    @PostMapping("/addresses")
    @PreAuthorize("isAuthenticated()")
    public AddressResponse saveAddress(@Valid @RequestBody CreateAddressRequest request) {
        return addressService.save(request);
    }

    @PutMapping("/addresses/{id}")
    @PreAuthorize("isAuthenticated()")
    public AddressResponse updateAddress(@PathVariable Long id, @Valid @RequestBody CreateAddressRequest request) {
        return addressService.update(id, request);
    }

    @DeleteMapping("/addresses/{id}")
    @PreAuthorize("isAuthenticated()")
    public void deleteAddress(@PathVariable Long id) {
        addressService.delete(id);
    }
}

