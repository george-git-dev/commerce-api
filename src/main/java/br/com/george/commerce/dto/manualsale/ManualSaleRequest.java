package br.com.george.commerce.dto.manualsale;

import java.util.List;

public record ManualSaleRequest(

        List<ManualSaleItemRequest> items,

        String notes

) {
}
