package br.com.george.commerce.mapper;

import br.com.george.commerce.dto.promotion.CreatePromotionRequest;
import br.com.george.commerce.dto.promotion.PromotionResponse;
import br.com.george.commerce.entity.Promotion;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PromotionMapper {

    Promotion toEntity(CreatePromotionRequest request);

    PromotionResponse toResponse(Promotion promotion);
}
