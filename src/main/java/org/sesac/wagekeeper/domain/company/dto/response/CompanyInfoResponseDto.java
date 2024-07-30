package org.sesac.wagekeeper.domain.company.dto.response;

public record CompanyInfoResponseDto(
        String name,
        String address,
        String employer,
        String image
) {
}
