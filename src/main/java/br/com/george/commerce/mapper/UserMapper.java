package br.com.george.commerce.mapper;

import br.com.george.commerce.dto.user.UserResponse;
import br.com.george.commerce.entity.User;
import br.com.george.commerce.util.CpfMask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = CpfMask.class)
public interface UserMapper {

    /** O CPF sai sempre mascarado. */
    @Mapping(target = "cpf", expression = "java(CpfMask.mask(user.getCpf()))")
    UserResponse toResponse(User user);

}
