package br.com.george.commerce.mapper;

import br.com.george.commerce.dto.affiliate.AffiliateResponse;
import br.com.george.commerce.dto.affiliate.MyAffiliateResponse;
import br.com.george.commerce.entity.Affiliate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AffiliateMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.name")
    AffiliateResponse toResponse(Affiliate affiliate);

    MyAffiliateResponse toMyAffiliateResponse(Affiliate affiliate);
}
