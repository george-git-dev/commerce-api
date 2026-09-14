package br.com.george.commerce.controller.admin.vendamanual;

import br.com.george.commerce.dto.manualsale.ManualSaleRequest;
import br.com.george.commerce.dto.order.OrderResponse;
import br.com.george.commerce.service.ManualSaleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Administração - Venda Manual",
        description = "Registro de vendas realizadas manualmente."
)
@RestController
@RequestMapping("/manual-sales")
@RequiredArgsConstructor
public class VendaManualController {

    private final ManualSaleService manualSaleService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public OrderResponse createSale(@Valid @RequestBody ManualSaleRequest request) {
        return manualSaleService.createSale(request);
    }
}


