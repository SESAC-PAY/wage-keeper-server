package org.sesac.wagekeeper.domain.review.dto.response;

import org.sesac.wagekeeper.domain.company.dto.response.CompanyInfoResponseDto;

import java.util.List;

public record TotalCompanyInfoResponseDto(
        CompanyInfoResponseDto companyInfo,
        ReportIssueResponseDto reportIssue,
        ReviewScoreResponseDto reviewScore,
        List<ReviewResponseDto> reviewList
) {
}
