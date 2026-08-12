package br.com.george.commerce.mapper;

import br.com.george.commerce.dto.product.ProductAttributeResponse;
import br.com.george.commerce.entity.ProductAttribute;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductAttributeMapper {

    ProductAttributeResponse toResponse(ProductAttribute attribute);

}
