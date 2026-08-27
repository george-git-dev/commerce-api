package br.com.george.commerce.dto.affiliate;

import java.time.LocalDateTime;

public record MyAffiliateResponse(

        String couponCode,

        Boolean active,

        LocalDateTime expiresAt

) {
}
