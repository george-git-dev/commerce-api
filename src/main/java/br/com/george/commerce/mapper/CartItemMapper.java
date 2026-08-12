package br.com.george.commerce.mapper;

import br.com.george.commerce.dto.cart.CartItemResponse;
import br.com.george.commerce.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productPrice", source = "product.price")
    CartItemResponse toResponse(CartItem cartItem);

}
