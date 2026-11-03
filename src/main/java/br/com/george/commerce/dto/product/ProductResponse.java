package br.com.george.commerce.dto.product;

import lombok.Builder;
import java.math.BigDecimal;
import java.util.List;

@Builder
public record ProductResponse(

        Long id,

        String name,

        String description,

        BigDecimal price,

        Integer stock,

        Boolean active,

        Long categoryId,

        String categoryName,

        Long brandId,

        String brandName,

        List<ProductAttributeResponse> attributes,

        BigDecimal finalPrice,

        Boolean promotionActive

) {
}
