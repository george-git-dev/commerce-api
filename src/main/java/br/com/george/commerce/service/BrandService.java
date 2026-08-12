package br.com.george.commerce.service;

import br.com.george.commerce.dto.brand.BrandResponse;
import br.com.george.commerce.dto.brand.CreateBrandRequest;

import java.util.List;

public interface BrandService {

    List<BrandResponse> findAll();

    BrandResponse findById(Long id);

    BrandResponse save(CreateBrandRequest request);

    BrandResponse update(Long id, CreateBrandRequest request);

    void delete(Long id);
}
