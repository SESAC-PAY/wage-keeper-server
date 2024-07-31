package org.sesac.wagekeeper.domain.company.dto.response;

public record CompanyInfoResponseDto(
        Long id,
        String companyName,
        String address,
        String employer,
        String image
) {
}
