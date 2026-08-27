package br.com.george.commerce.dto.affiliate;

import java.time.LocalDate;

public record AffiliateReportRequest(

        LocalDate startDate,

        LocalDate endDate

) {
}
