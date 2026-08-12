package br.com.george.commerce.service.impl;

import br.com.george.commerce.dto.product.CreateProductRequest;
import br.com.george.commerce.dto.product.ProductResponse;
import br.com.george.commerce.entity.Brand;
import br.com.george.commerce.entity.Category;
import br.com.george.commerce.entity.Product;
import br.com.george.commerce.entity.ProductAttribute;
import br.com.george.commerce.exception.BrandNotFoundException;
import br.com.george.commerce.exception.CategoryNotFoundException;
import br.com.george.commerce.exception.ProductNotFoundException;
import br.com.george.commerce.mapper.ProductMapper;
import br.com.george.commerce.repository.BrandRepository;
import br.com.george.commerce.repository.CategoryRepository;
import br.com.george.commerce.repository.ProductRepository;
import br.com.george.commerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductMapper mapper;

    @Override
    public List<ProductResponse> findAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public ProductResponse findById(Long id) {

        Product product = repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        return mapper.toResponse(product);
    }

    @Override
    public ProductResponse save(CreateProductRequest request) {

        Category category = categoryRepository.findById(request.categoryId()).orElseThrow(() -> new CategoryNotFoundException(request.categoryId()));

        Brand brand = brandRepository.findById(request.brandId()).orElseThrow(() -> new BrandNotFoundException(request.brandId()));

        Product product = Product.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .stock(request.stock())
                .active(request.active())
                .category(category)
                .brand(brand)
                .createdAt(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))
                .updatedAt(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))
                .build();

        final Product finalProduct = product;

        List<ProductAttribute> attributes =
                Optional.ofNullable(request.attributes())
                        .orElse(List.of())
                        .stream()
                        .map(attributeRequest -> ProductAttribute.builder()
                                .name(attributeRequest.name())
                                .value(attributeRequest.value())
                                .product(finalProduct)
                                .build())
                        .toList();

        product.setAttributes(attributes);

        Product savedProduct = repository.save(product);

        return mapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponse update(Long id, CreateProductRequest request) {

        Product product = repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        Category category = categoryRepository.findById(request.categoryId()).orElseThrow(() -> new CategoryNotFoundException(request.categoryId()));

        Brand brand = brandRepository.findById(request.brandId()).orElseThrow(() -> new BrandNotFoundException(request.brandId()));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStock(request.stock());
        product.setActive(request.active());
        product.setCategory(category);
        product.setBrand(brand);
        product.setUpdatedAt(LocalDateTime.now());

        product = repository.save(product);

        return mapper.toResponse(product);
    }

    @Override
    public void delete(Long id) {

        Product product = repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        repository.delete(product);
    }
}