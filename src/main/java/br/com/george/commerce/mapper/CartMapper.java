package br.com.george.commerce.mapper;

import br.com.george.commerce.dto.cart.CartResponse;
import br.com.george.commerce.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CartItemMapper.class)
public interface CartMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.name")
    CartResponse toResponse(Cart cart);

}
