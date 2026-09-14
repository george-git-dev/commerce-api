package br.com.george.commerce.dto.report.sales;

import br.com.george.commerce.enums.SaleChannel;

import java.time.LocalDate;

public record SalesReportRequest(

        LocalDate startDate,

        LocalDate endDate,

        SaleChannel saleChannel

) {
}

