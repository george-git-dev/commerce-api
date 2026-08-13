package br.com.george.commerce.mapper;

import br.com.george.commerce.dto.payment.PaymentResponse;
import br.com.george.commerce.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "orderId", source = "order.id")
    PaymentResponse toResponse(Payment payment);

}
