package br.com.george.commerce.service.impl;

import br.com.george.commerce.dto.address.AddressResponse;
import br.com.george.commerce.dto.address.CreateAddressRequest;
import br.com.george.commerce.entity.Address;
import br.com.george.commerce.entity.User;
import br.com.george.commerce.exception.AddressNotFoundException;
import br.com.george.commerce.exception.UserNotFoundException;
import br.com.george.commerce.mapper.AddressMapper;
import br.com.george.commerce.repository.AddressRepository;
import br.com.george.commerce.repository.UserRepository;
import br.com.george.commerce.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository repository;
    private final UserRepository userRepository;
    private final AddressMapper mapper;

    @Override
    public List<AddressResponse> findAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public AddressResponse findById(Long id) {
        Address address = repository.findById(id).orElseThrow(() -> new AddressNotFoundException(id));
        return mapper.toResponse(address);
    }

    @Override
    public AddressResponse save(CreateAddressRequest request) {

        User user = userRepository.findById(request.userId()).orElseThrow(() -> new UserNotFoundException(request.userId()));

        if (Boolean.TRUE.equals(request.primaryAddress())) {
            List<Address> addresses = repository.findByUserId(request.userId());
            addresses.forEach(address -> address.setPrimaryAddress(false));
            repository.saveAll(addresses);
        }

        Address address = Address.builder()
                .street(request.street())
                .number(request.number())
                .complement(request.complement())
                .neighborhood(request.neighborhood())
                .city(request.city())
                .state(request.state())
                .zipCode(request.zipCode())
                .primaryAddress(request.primaryAddress())
                .user(user)
                .build();

        address = repository.save(address);

        return mapper.toResponse(address);
    }

    @Override
    public AddressResponse update(Long id, CreateAddressRequest request) {

        Address address = repository.findById(id).orElseThrow(() -> new AddressNotFoundException(id));

        User user = userRepository.findById(request.userId()).orElseThrow(() -> new UserNotFoundException(request.userId()));

        if (Boolean.TRUE.equals(request.primaryAddress())) {
            List<Address> addresses = repository.findByUserId(request.userId());

            addresses.forEach(existingAddress -> {
                if (!existingAddress.getId().equals(id)) {
                    existingAddress.setPrimaryAddress(false);
                }
            });
            repository.saveAll(addresses);
        }

        address.setStreet(request.street());
        address.setNumber(request.number());
        address.setComplement(request.complement());
        address.setNeighborhood(request.neighborhood());
        address.setCity(request.city());
        address.setState(request.state());
        address.setZipCode(request.zipCode());
        address.setPrimaryAddress(request.primaryAddress());
        address.setUser(user);

        address = repository.save(address);

        return mapper.toResponse(address);
    }

    @Override
    public void delete(Long id) {
        Address address = repository.findById(id).orElseThrow(() -> new AddressNotFoundException(id));
        repository.delete(address);
    }
}
