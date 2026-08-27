package br.com.george.commerce.service;

import br.com.george.commerce.dto.manualsale.ManualSaleRequest;
import br.com.george.commerce.dto.order.OrderResponse;

public interface ManualSaleService {

    OrderResponse createSale(ManualSaleRequest request);

}
