package br.com.george.commerce.service;

import br.com.george.commerce.dto.product.CreateProductRequest;
import br.com.george.commerce.dto.product.ProductResponse;

import java.util.List;

public interface ProductService {

    List<ProductResponse> findAll();

    ProductResponse findById(Long id);

    ProductResponse save(CreateProductRequest request);

    ProductResponse update(Long id, CreateProductRequest request);

    void delete(Long id);
}
