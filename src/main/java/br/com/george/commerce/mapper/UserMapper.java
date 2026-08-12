package br.com.george.commerce.mapper;

import br.com.george.commerce.dto.user.UserResponse;
import br.com.george.commerce.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);

}
