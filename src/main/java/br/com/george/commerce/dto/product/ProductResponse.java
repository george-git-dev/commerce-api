package br.com.george.commerce.dto.product;

import java.math.BigDecimal;
import java.util.List;

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

        List<ProductAttributeResponse> attributes

) {
}
