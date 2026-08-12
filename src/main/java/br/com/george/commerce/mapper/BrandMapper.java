package br.com.george.commerce.mapper;

import br.com.george.commerce.dto.brand.BrandResponse;
import br.com.george.commerce.dto.brand.CreateBrandRequest;
import br.com.george.commerce.entity.Brand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    Brand toEntity(CreateBrandRequest request);

    BrandResponse toResponse(Brand brand);
}
