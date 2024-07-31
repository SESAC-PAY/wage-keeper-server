package org.sesac.wagekeeper.domain.review.dto.response;

import lombok.Builder;

@Builder
public record ReviewScoreResponseDto(
        Double totalAvgScore,
        Double totalAvgProperPaymentScore,
        Double totalAvgEnvironmentScore,
        Double totalAvgDomitoryScore,
        Double totalAvgFullfillContractScore

) {
}
