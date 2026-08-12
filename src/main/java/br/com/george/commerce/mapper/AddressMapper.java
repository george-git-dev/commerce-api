package br.com.george.commerce.mapper;

import br.com.george.commerce.dto.address.AddressResponse;
import br.com.george.commerce.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.name")
    AddressResponse toResponse(Address address);

}
