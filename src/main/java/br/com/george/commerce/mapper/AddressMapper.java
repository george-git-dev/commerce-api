package br.com.george.commerce.mapper;

import br.com.george.commerce.dto.address.AddressResponse;
import br.com.george.commerce.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressResponse toResponse(Address address);

}
