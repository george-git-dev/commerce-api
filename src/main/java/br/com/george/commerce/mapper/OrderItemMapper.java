package br.com.george.commerce.mapper;

import br.com.george.commerce.dto.order.OrderItemResponse;
import br.com.george.commerce.entity.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemResponse toResponse(OrderItem orderItem);

}
