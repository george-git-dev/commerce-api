package br.com.george.commerce.service.impl;

import br.com.george.commerce.dto.address.AddressResponse;
import br.com.george.commerce.dto.address.CreateAddressRequest;
import br.com.george.commerce.entity.Address;
import br.com.george.commerce.entity.User;
import br.com.george.commerce.exception.AddressLimitException;
import br.com.george.commerce.exception.AddressNotFoundException;
import br.com.george.commerce.mapper.AddressMapper;
import br.com.george.commerce.repository.AddressRepository;
import br.com.george.commerce.service.AddressService;
import br.com.george.commerce.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Regra do principal: com pelo menos um endereço, a conta tem SEMPRE exatamente um principal.
 * O primeiro cadastrado vira principal; removendo o principal, o mais antigo assume; o principal
 * só muda escolhendo outro (desmarcar o atual não deixa a conta sem nenhum).
 */
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    static final int MAX_ADDRESSES = 5;

    private final AddressRepository repository;
    private final AddressMapper mapper;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public AddressResponse save(CreateAddressRequest request) {
        User user = currentUserService.get();
        List<Address> addresses = activeAddresses(user);
        if (addresses.size() >= MAX_ADDRESSES) {
            throw new AddressLimitException(MAX_ADDRESSES);
        }
        Address address = Address.builder()
                .user(user)
                .primaryAddress(false)
                .build();
        copy(request, address);
        address = repository.save(address);
        if (addresses.isEmpty() || Boolean.TRUE.equals(request.primaryAddress())) {
            makePrimary(address, addresses);
        }
        return mapper.toResponse(address);
    }

    @Override
    @Transactional
    public AddressResponse update(Long id, CreateAddressRequest request) {
        User user = currentUserService.get();
        Address address = owned(id, user);
        copy(request, address);
        if (Boolean.TRUE.equals(request.primaryAddress())) {
            makePrimary(address, activeAddresses(user));
        }
        return mapper.toResponse(address);
    }

    /** Some da conta (pedidos antigos continuam apontando para ele). Era o principal: o mais antigo assume. */
    @Override
    @Transactional
    public void delete(Long id) {
        User user = currentUserService.get();
        Address address = owned(id, user);
        boolean wasPrimary = Boolean.TRUE.equals(address.getPrimaryAddress());
        address.setActive(false);
        address.setPrimaryAddress(false);
        if (wasPrimary) {
            activeAddresses(user).stream()
                    .filter(other -> !other.getId().equals(id))
                    .findFirst()
                    .ifPresent(next -> next.setPrimaryAddress(true));
        }
    }

    /**
     * Principal primeiro, depois na ordem de cadastro. Sem {@code readOnly}: no primeiro acesso o
     * {@code CurrentUserService} cria o perfil (INSERT), e transação só de leitura recusa.
     */
    @Override
    @Transactional
    public List<AddressResponse> myAddresses() {
        return activeAddresses(currentUserService.get())
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    private List<Address> activeAddresses(User user) {
        return repository.findByUserIdAndActiveTrueOrderByPrimaryAddressDescIdAsc(user.getId());
    }

    /** Endereço de outra conta (ou já removido) responde "não encontrado": não revela que existe. */
    private Address owned(Long id, User user) {
        return repository.findByIdAndUserIdAndActiveTrue(id, user.getId())
                .orElseThrow(() -> new AddressNotFoundException(id));
    }

    private static void makePrimary(Address chosen, List<Address> addresses) {
        addresses.forEach(other -> other.setPrimaryAddress(false));
        chosen.setPrimaryAddress(true);
    }

    private static void copy(CreateAddressRequest request, Address address) {
        address.setStreet(request.street());
        address.setNumber(request.number());
        address.setComplement(request.complement());
        address.setNeighborhood(request.neighborhood());
        address.setCity(request.city());
        address.setState(request.state());
        address.setZipCode(request.zipCode());
    }
}
