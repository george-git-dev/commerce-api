package br.com.george.commerce.service.impl;

import br.com.george.commerce.dto.promotion.CreatePromotionRequest;
import br.com.george.commerce.dto.promotion.LinkPromotionToProductRequest;
import br.com.george.commerce.dto.promotion.PromotionResponse;
import br.com.george.commerce.entity.Product;
import br.com.george.commerce.entity.ProductPromotion;
import br.com.george.commerce.entity.Promotion;
import br.com.george.commerce.exception.ProductAlreadyHasPromotionException;
import br.com.george.commerce.exception.ProductNotFoundException;
import br.com.george.commerce.exception.PromotionNotFoundException;
import br.com.george.commerce.mapper.PromotionMapper;
import br.com.george.commerce.repository.ProductPromotionRepository;
import br.com.george.commerce.repository.ProductRepository;
import br.com.george.commerce.repository.PromotionRepository;
import br.com.george.commerce.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionMapper mapper;
    private final PromotionRepository repository;
    private final ProductRepository productRepository;
    private final ProductPromotionRepository productPromotionRepository;

    @Override
    public List<PromotionResponse> findAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public PromotionResponse findById(Long id) {
        Promotion promotion = repository.findById(id).orElseThrow(() -> new PromotionNotFoundException(id));
        return mapper.toResponse(promotion);
    }

    @Override
    public PromotionResponse save(CreatePromotionRequest request) {
        Promotion promotion = mapper.toEntity(request);
        promotion = repository.save(promotion);
        return mapper.toResponse(promotion);
    }

    @Override
    public PromotionResponse update(Long id, CreatePromotionRequest request) {

        Promotion promotion = repository.findById(id).orElseThrow(() -> new PromotionNotFoundException(id));

        promotion.setName(request.name());
        promotion.setDescription(request.description());
        promotion.setActive(request.active());
        promotion.setDiscountType(request.discountType());
        promotion.setDiscountValue(request.discountValue());
        promotion.setStartAt(request.startAt());
        promotion.setEndAt(request.endAt());

        promotion = repository.save(promotion);

        return mapper.toResponse(promotion);
    }

    @Override
    public void delete(Long id) {
        Promotion promotion = repository.findById(id).orElseThrow(() -> new PromotionNotFoundException(id));
        repository.delete(promotion);
    }

    @Override
    public void linkToProduct(LinkPromotionToProductRequest request) {

        Product product = productRepository.findById(request.productId()).orElseThrow(() -> new ProductNotFoundException(request.productId()));

        Promotion promotion = repository.findById(request.promotionId()).orElseThrow(() -> new PromotionNotFoundException(request.promotionId()));

        ProductPromotion productPromotion = ProductPromotion.builder().product(product).promotion(promotion).build();

        if (productPromotionRepository.findByProductId(request.productId()).isPresent()) {
            throw new ProductAlreadyHasPromotionException(request.productId());
        }

        productPromotionRepository.save(productPromotion);
    }
}
