package br.com.george.commerce.mapper;

import br.com.george.commerce.dto.order.OrderResponse;
import br.com.george.commerce.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = OrderItemMapper.class)
public interface OrderMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.name")
    @Mapping(target = "addressId", source = "address.id")
    OrderResponse toResponse(Order order);

}
