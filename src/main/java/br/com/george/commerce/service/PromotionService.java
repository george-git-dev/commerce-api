package br.com.george.commerce.service;

import br.com.george.commerce.dto.promotion.CreatePromotionRequest;
import br.com.george.commerce.dto.promotion.LinkPromotionToProductRequest;
import br.com.george.commerce.dto.promotion.PromotionResponse;

import java.util.List;

public interface PromotionService {

    List<PromotionResponse> findAll();

    PromotionResponse findById(Long id);

    PromotionResponse save(CreatePromotionRequest request);

    PromotionResponse update(Long id, CreatePromotionRequest request);

    void delete(Long id);

    void linkToProduct(LinkPromotionToProductRequest request);
}
