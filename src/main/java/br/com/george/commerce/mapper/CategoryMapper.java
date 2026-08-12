package br.com.george.commerce.mapper;

import br.com.george.commerce.dto.category.CategoryResponse;
import br.com.george.commerce.dto.category.CreateCategoryRequest;
import br.com.george.commerce.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CreateCategoryRequest request);

    CategoryResponse toResponse(Category category);
}
