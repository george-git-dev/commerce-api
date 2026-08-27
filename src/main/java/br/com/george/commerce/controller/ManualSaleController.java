package br.com.george.commerce.controller;

import br.com.george.commerce.dto.manualsale.ManualSaleRequest;
import br.com.george.commerce.dto.order.OrderResponse;
import br.com.george.commerce.service.ManualSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manual-sales")
@RequiredArgsConstructor
public class ManualSaleController {

    private final ManualSaleService manualSaleService;

    @PostMapping
    public OrderResponse createSale(@RequestBody ManualSaleRequest request) {
        return manualSaleService.createSale(request);
    }
}
