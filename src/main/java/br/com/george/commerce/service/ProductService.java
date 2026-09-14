package br.com.george.commerce.service;

import br.com.george.commerce.dto.product.CreateProductRequest;
import br.com.george.commerce.dto.product.ProductResponse;
import br.com.george.commerce.dto.report.inventory.BrandInventoryStatsResponse;
import br.com.george.commerce.dto.report.inventory.CategoryInventoryStatsResponse;
import br.com.george.commerce.dto.report.inventory.InventoryFilterRequest;
import br.com.george.commerce.dto.report.inventory.InventoryProductResponse;
import br.com.george.commerce.dto.report.inventory.InventorySummaryResponse;

import java.util.List;

public interface ProductService {

    List<ProductResponse> findAll();

    ProductResponse findById(Long id);

    ProductResponse save(CreateProductRequest request);

    ProductResponse update(Long id, CreateProductRequest request);

    void delete(Long id);

    List<InventoryProductResponse> findInventoryProducts(InventoryFilterRequest request);

    InventorySummaryResponse getInventorySummary();

    List<CategoryInventoryStatsResponse> getCategoryStatistics();

    List<BrandInventoryStatsResponse> getBrandStatistics();
}
