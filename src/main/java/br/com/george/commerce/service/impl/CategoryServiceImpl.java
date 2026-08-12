package br.com.george.commerce.service.impl;

import br.com.george.commerce.dto.category.CategoryResponse;
import br.com.george.commerce.dto.category.CreateCategoryRequest;
import br.com.george.commerce.entity.Category;
import br.com.george.commerce.exception.CategoryNotFoundException;
import br.com.george.commerce.mapper.CategoryMapper;
import br.com.george.commerce.repository.CategoryRepository;
import br.com.george.commerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    @Override
    public List<CategoryResponse> findAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public CategoryResponse save(CreateCategoryRequest request) {

        Category category = mapper.toEntity(request);

        category = repository.save(category);

        return mapper.toResponse(category);
    }

    @Override
    public CategoryResponse findById(Long id) {

        Category category = repository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));

        return mapper.toResponse(category);
    }

    @Override
    public void delete(Long id) {
        Category category = repository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        repository.delete(category);
    }

    @Override
    public CategoryResponse update(Long id, CreateCategoryRequest request) {

        Category category = repository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));

        category.setName(request.name());
        category.setActive(request.active());

        category = repository.save(category);

        return mapper.toResponse(category);
    }
}
