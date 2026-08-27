package br.com.george.commerce.dto.promotion;

public record LinkPromotionToProductRequest(

        Long productId,

        Long promotionId

) {
}
