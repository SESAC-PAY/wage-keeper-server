package org.sesac.wagekeeper.domain.review.dto.request;

public record ReviewRequestDto(
        Long userId,
        Long companyId,
        String title,
        String content,
        Long properPaymentScore,
        Long environmentScore,
        Long domitoryScore,
        Long fullfillContractScore
) {
}
