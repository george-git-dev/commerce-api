package br.com.george.commerce.mapper;

import br.com.george.commerce.dto.affiliate.AffiliateSaleResponse;
import br.com.george.commerce.entity.AffiliateSale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AffiliateSaleMapper {

    @Mapping(target = "affiliateId", source = "affiliate.id")
    @Mapping(target = "orderId", source = "order.id")
    AffiliateSaleResponse toResponse(AffiliateSale affiliateSale);
}
