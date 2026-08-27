package br.com.george.commerce.controller;

import br.com.george.commerce.dto.promotion.CreatePromotionRequest;
import br.com.george.commerce.dto.promotion.LinkPromotionToProductRequest;
import br.com.george.commerce.dto.promotion.PromotionResponse;
import br.com.george.commerce.service.PromotionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @GetMapping
    public List<PromotionResponse> findAll() {
        return promotionService.findAll();
    }

    @GetMapping("/{id}")
    public PromotionResponse findById(@PathVariable Long id) {
        return promotionService.findById(id);
    }

    @PostMapping
    public PromotionResponse save(@Valid @RequestBody CreatePromotionRequest request) {
        return promotionService.save(request);
    }

    @PutMapping("/{id}")
    public PromotionResponse update(@PathVariable Long id, @Valid @RequestBody CreatePromotionRequest request) {
        return promotionService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        promotionService.delete(id);
    }

    @PostMapping("/link-product")
    public void linkToProduct(@RequestBody LinkPromotionToProductRequest request) {
        promotionService.linkToProduct(request);
    }
}
