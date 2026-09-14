package br.com.george.commerce.controller.admin.afiliado;

import br.com.george.commerce.service.AffiliateSaleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Administração - Afiliados",
        description = "Operações administrativas relacionadas a afiliados."
)
@RestController
@RequestMapping("/affiliates")
@RequiredArgsConstructor
public class AfiliadoController {

    private final AffiliateSaleService affiliateSaleService;

    @PatchMapping("/sales/{saleId}/pay")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public void markAsPaid(@PathVariable Long saleId) {
        affiliateSaleService.markAsPaid(saleId);
    }

}


