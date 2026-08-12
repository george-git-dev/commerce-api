package br.com.george.commerce.service.impl;

import br.com.george.commerce.dto.brand.BrandResponse;
import br.com.george.commerce.dto.brand.CreateBrandRequest;
import br.com.george.commerce.entity.Brand;
import br.com.george.commerce.exception.BrandNotFoundException;
import br.com.george.commerce.mapper.BrandMapper;
import br.com.george.commerce.repository.BrandRepository;
import br.com.george.commerce.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository repository;
    private final BrandMapper mapper;

    @Override
    public List<BrandResponse> findAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public BrandResponse findById(Long id) {

        Brand brand = repository.findById(id).orElseThrow(() -> new BrandNotFoundException(id));

        return mapper.toResponse(brand);
    }

    @Override
    public BrandResponse save(CreateBrandRequest request) {

        Brand brand = mapper.toEntity(request);

        brand = repository.save(brand);

        return mapper.toResponse(brand);
    }

    @Override
    public BrandResponse update(Long id, CreateBrandRequest request) {

        Brand brand = repository.findById(id).orElseThrow(() -> new BrandNotFoundException(id));

        brand.setName(request.name());
        brand.setActive(request.active());

        brand = repository.save(brand);

        return mapper.toResponse(brand);
    }

    @Override
    public void delete(Long id) {
        Brand brand = repository.findById(id).orElseThrow(() -> new BrandNotFoundException(id));
        repository.delete(brand);
    }
}
