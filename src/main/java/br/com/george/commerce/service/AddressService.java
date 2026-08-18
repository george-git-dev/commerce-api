package br.com.george.commerce.service;

import br.com.george.commerce.dto.address.AddressResponse;
import br.com.george.commerce.dto.address.CreateAddressRequest;

import java.util.List;

public interface AddressService {

    List<AddressResponse> findAll();

    AddressResponse findById(Long id);

    AddressResponse save(CreateAddressRequest request);

    AddressResponse update(Long id, CreateAddressRequest request);

    void delete(Long id);

    List<AddressResponse> myAddresses();
}
