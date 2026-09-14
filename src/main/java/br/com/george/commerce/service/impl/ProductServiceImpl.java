package br.com.george.commerce.service.impl;

import br.com.george.commerce.dto.product.CreateProductRequest;
import br.com.george.commerce.dto.product.ProductResponse;
import br.com.george.commerce.dto.report.inventory.BrandInventoryStatsResponse;
import br.com.george.commerce.dto.report.inventory.CategoryInventoryStatsResponse;
import br.com.george.commerce.dto.report.inventory.InventoryFilterRequest;
import br.com.george.commerce.dto.report.inventory.InventoryProductResponse;
import br.com.george.commerce.dto.report.inventory.InventorySummaryResponse;
import br.com.george.commerce.entity.*;
import br.com.george.commerce.enums.DiscountType;
import br.com.george.commerce.exception.BrandNotFoundException;
import br.com.george.commerce.exception.CategoryNotFoundException;
import br.com.george.commerce.exception.ProductNotFoundException;
import br.com.george.commerce.mapper.ProductMapper;
import br.com.george.commerce.repository.BrandRepository;
import br.com.george.commerce.repository.CategoryRepository;
import br.com.george.commerce.repository.ProductPromotionRepository;
import br.com.george.commerce.repository.ProductRepository;
import br.com.george.commerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper mapper;
    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductPromotionRepository productPromotionRepository;

    @Override
    public List<ProductResponse> findAll() {

        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProductResponse findById(Long id) {

        Product product = repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        return toResponse(product);
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

        return toResponse(savedProduct);
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
        product.setUpdatedAt(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));

        product = repository.save(product);

        return toResponse(product);
    }

    @Override
    public void delete(Long id) {

        Product product = repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        repository.delete(product);
    }

    private BigDecimal calculateFinalPrice(Product product) {

        Optional<ProductPromotion> productPromotion =
                productPromotionRepository.findByProductId(product.getId());

        if (productPromotion.isEmpty()) {
            return product.getPrice();
        }

        Promotion promotion = productPromotion.get().getPromotion();

        if (!promotion.isActiveNow()) {
            return product.getPrice();
        }

        if (promotion.getDiscountType() == DiscountType.PERCENTAGE) {

            BigDecimal discountPercentage = promotion.getDiscountValue().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            BigDecimal discount = product.getPrice().multiply(discountPercentage);

            return product.getPrice().subtract(discount).setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal finalPrice = product.getPrice().subtract(promotion.getDiscountValue());

        return finalPrice.max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
    }

    private ProductResponse toResponse(Product product) {

        BigDecimal finalPrice = calculateFinalPrice(product);

        boolean promotionActive = productPromotionRepository
                .findByProductId(product.getId())
                .map(ProductPromotion::getPromotion)
                .map(Promotion::isActiveNow)
                .orElse(false);

        ProductResponse response = mapper.toResponse(product);

        return new ProductResponse(
                response.id(),
                response.name(),
                response.description(),
                response.price(),
                response.stock(),
                response.active(),
                response.categoryId(),
                response.categoryName(),
                response.brandId(),
                response.brandName(),
                response.attributes(),
                finalPrice,
                promotionActive
        );
    }

    @Override
    public List<InventoryProductResponse> findInventoryProducts(InventoryFilterRequest request) {

        List<Product> products = repository.findAll();

        if (request.name() != null && !request.name().isBlank()) {

            products = products.stream()
                    .filter(product ->
                            product.getName()
                                    .toLowerCase()
                                    .contains(request.name().toLowerCase()))
                    .toList();
        }

        if (request.categoryId() != null) {

            products = products.stream()
                    .filter(product ->
                            product.getCategory() != null
                                    && product.getCategory().getId().equals(request.categoryId()))
                    .toList();
        }

        if (request.brandId() != null) {

            products = products.stream()
                    .filter(product ->
                            product.getBrand() != null
                                    && product.getBrand().getId().equals(request.brandId()))
                    .toList();
        }

        if (request.active() != null) {

            products = products.stream()
                    .filter(product ->
                            product.getActive().equals(request.active()))
                    .toList();
        }

        if (Boolean.TRUE.equals(request.outOfStock())) {

            products = products.stream()
                    .filter(product -> product.getStock() <= 0)
                    .toList();
        }

        return products.stream()
                .map(product -> new InventoryProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getCategory().getName(),
                        product.getBrand().getName(),
                        product.getStock(),
                        product.getActive()
                ))
                .toList();
    }

    @Override
    public InventorySummaryResponse getInventorySummary() {

        List<Product> products = repository.findAll();

        long totalProducts = products.size();

        long activeProducts = products.stream()
                .filter(Product::getActive)
                .count();

        long inactiveProducts = products.stream()
                .filter(product -> !product.getActive())
                .count();

        long outOfStockProducts = products.stream()
                .filter(product -> product.getStock() <= 0)
                .count();

        return new InventorySummaryResponse(
                totalProducts,
                activeProducts,
                inactiveProducts,
                outOfStockProducts
        );
    }

    @Override
    public List<CategoryInventoryStatsResponse> getCategoryStatistics() {

        return repository.findAll()
                .stream()
                .collect(Collectors.groupingBy(product -> product.getCategory().getName()))
                .entrySet()
                .stream()
                .map(entry -> new CategoryInventoryStatsResponse(entry.getKey(), (long) entry.getValue().size()))
                .sorted(Comparator.comparing(CategoryInventoryStatsResponse::totalProducts).reversed())
                .toList();
    }

    @Override
    public List<BrandInventoryStatsResponse> getBrandStatistics() {

        return repository.findAll()
                .stream()
                .collect(Collectors.groupingBy(product -> product.getBrand().getName()))
                .entrySet()
                .stream()
                .map(entry -> new BrandInventoryStatsResponse(entry.getKey(), (long) entry.getValue().size()))
                .sorted(Comparator.comparing(BrandInventoryStatsResponse::totalProducts).reversed())
                .toList();
    }

}