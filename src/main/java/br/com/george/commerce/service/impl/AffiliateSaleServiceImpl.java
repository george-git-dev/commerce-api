package br.com.george.commerce.service.impl;

import br.com.george.commerce.entity.AffiliateSale;
import br.com.george.commerce.exception.AffiliateSaleAlreadyPaidException;
import br.com.george.commerce.exception.AffiliateSaleNotFoundException;
import br.com.george.commerce.repository.AffiliateSaleRepository;
import br.com.george.commerce.service.AffiliateSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AffiliateSaleServiceImpl implements AffiliateSaleService {

    private final AffiliateSaleRepository repository;

    @Override
    public void markAsPaid(Long saleId) {
        AffiliateSale sale = repository.findById(saleId).orElseThrow(() -> new AffiliateSaleNotFoundException(saleId));

        if (sale.getPaid()) {
            throw new AffiliateSaleAlreadyPaidException();
        }

        sale.setPaid(true);
        sale.setPaidAt(LocalDateTime.now());

        repository.save(sale);
    }
}
