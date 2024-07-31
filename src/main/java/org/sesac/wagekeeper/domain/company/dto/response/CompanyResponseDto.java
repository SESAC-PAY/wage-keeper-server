package org.sesac.wagekeeper.domain.company.dto.response;

public record CompanyResponseDto(
        CompanyInfoResponseDto companyInfo,
        CompanyLocationDto companyLocation
) {
}
