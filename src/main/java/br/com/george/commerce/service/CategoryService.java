package br.com.george.commerce.service;

import br.com.george.commerce.dto.category.CategoryResponse;
import br.com.george.commerce.dto.category.CreateCategoryRequest;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> findAll();

    CategoryResponse save(CreateCategoryRequest request);

    CategoryResponse findById(Long id);

    void delete(Long id);

    CategoryResponse update(Long id, CreateCategoryRequest request);
}
