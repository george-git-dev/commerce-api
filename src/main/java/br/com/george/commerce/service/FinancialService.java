package br.com.george.commerce.service;

import br.com.george.commerce.dto.financial.FinancialSummaryRequest;
import br.com.george.commerce.dto.financial.FinancialSummaryResponse;

public interface FinancialService {

    FinancialSummaryResponse summary(FinancialSummaryRequest request);

}
