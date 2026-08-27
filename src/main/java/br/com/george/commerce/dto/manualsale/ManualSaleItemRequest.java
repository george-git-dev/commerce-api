package br.com.george.commerce.dto.manualsale;

import java.math.BigDecimal;

public record ManualSaleItemRequest(

        Long productId,

        Integer quantity,

        BigDecimal unitPrice

) {
}
